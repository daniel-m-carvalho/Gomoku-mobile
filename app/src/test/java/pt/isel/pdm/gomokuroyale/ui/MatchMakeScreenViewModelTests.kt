package pt.isel.pdm.gomokuroyale.ui

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfo
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfoRepository
import pt.isel.pdm.gomokuroyale.game.lobby.domain.MatchInfo
import pt.isel.pdm.gomokuroyale.game.matchmake.ui.MatchmakerViewModel
import pt.isel.pdm.gomokuroyale.game.matchmake.ui.MatchmakingScreenState
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import pt.isel.pdm.gomokuroyale.http.GomokuService
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameMatchmakingInputModel
import pt.isel.pdm.gomokuroyale.util.ApiError
import pt.isel.pdm.gomokuroyale.util.HttpResult
import pt.isel.pdm.gomokuroyale.utils.MockMainDispatcherRule
import pt.isel.pdm.gomokuroyale.utils.SuspendingGate

class MatchMakeScreenViewModelTests {

    @get:Rule
    val rule = MockMainDispatcherRule(testDispatcher = StandardTestDispatcher())

    private val testUserInfo = UserInfo("test", "test")
    private val userInfoRepo = mockk<UserInfoRepository> {
        coEvery { getUserInfo() } coAnswers { testUserInfo }
    }

    private val gomokuService = mockk<GomokuService> {
        coEvery {
            gameService.matchmaking(
                testUserInfo.accessToken,
                GameMatchmakingInputModel("STANDARD")
            )
        } returns HttpResult.Failure(
            ApiError("Matchmaking link not found")
        )
    }

    private val testSut = MatchmakerViewModel(
        gomokuService.gameService,
        MatchInfo(testUserInfo, Variant("STANDARD", 15, "Standard", "Standard", 5)),
        userInfoRepo
    )

    @Test
    fun initially_the_state_flow_is_idle() = runTest {
        // Arrange
        val sut = testSut
        // Act
        val gate = SuspendingGate()
        var collectedState: MatchmakingScreenState? = null
        val collectJob = launch {
            sut.screenState.collect{
                collectedState = it
                gate.open()
            }
        }

        // Lets wait for the flow to emit the first value
        withTimeout(1000) {
            gate.await()
            collectJob.cancelAndJoin()
        }

        // Assert
        Assert.assertTrue("Expected Idle bot got $collectedState instead", collectedState is MatchmakingScreenState.Idle)
    }

    @Test
    fun findGame_emits_to_the_state_flow_the_error_state() = runTest {
        // Arrange
        val sut = testSut
        // Act
        val gate = SuspendingGate()
        var lastCollectedState: MatchmakingScreenState? = null
        val collectJob = launch {
            sut.screenState.collect {
                if (it is MatchmakingScreenState.Error) {
                    lastCollectedState = it
                    gate.open()
                }
            }
        }
        sut.findGame()

        // Lets wait for the flow to emit the latest value
        withTimeout(1000) {
            gate.await()
            collectJob.cancelAndJoin()
        }

        // Assert
        val loading = lastCollectedState as? MatchmakingScreenState.Error
        Assert.assertNotNull("Expected Loading but got $lastCollectedState instead", loading)
    }
}