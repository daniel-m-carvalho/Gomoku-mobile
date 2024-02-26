package pt.isel.pdm.gomokuroyale.ui

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfo
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfoRepository
import pt.isel.pdm.gomokuroyale.http.GomokuService
import pt.isel.pdm.gomokuroyale.matchHistory.ui.FetchedPlayerInfo
import pt.isel.pdm.gomokuroyale.matchHistory.ui.FetchingPlayerInfo
import pt.isel.pdm.gomokuroyale.matchHistory.ui.Idle
import pt.isel.pdm.gomokuroyale.matchHistory.ui.MatchHistoryScreenState
import pt.isel.pdm.gomokuroyale.matchHistory.ui.MatchHistoryViewModel
import pt.isel.pdm.gomokuroyale.util.HttpResult
import pt.isel.pdm.gomokuroyale.utils.MockMainDispatcherRule
import pt.isel.pdm.gomokuroyale.utils.SuspendingGate

@OptIn(ExperimentalCoroutinesApi::class)
class MatchHistoryScreenViewModelTests {

    @get:Rule
    val rule = MockMainDispatcherRule(testDispatcher = StandardTestDispatcher())

    private val testUserInfo = UserInfo("test", "test")
    private val userInfoRepo = mockk<UserInfoRepository> {
        coEvery { getUserInfo() } coAnswers { testUserInfo }
    }

    private val gomokuService = mockk<GomokuService> {
        coEvery {
            gameService.getUserGames(
                token = testUserInfo.accessToken,
                userId = 1,
                page = 1
            )
        } coAnswers {
            HttpResult.Success(
                listOf()
            )
        }
    }

    @Test
    fun initially_the_state_flow_is_idle() = runTest {
        // Arrange
        val sut = MatchHistoryViewModel(userInfoRepo, gomokuService.gameService)
        // Act
        val gate = SuspendingGate()
        var collectedState: MatchHistoryScreenState? = null
        val collectJob = launch {
            sut.state.collect {
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
        Assert.assertTrue("Expected Idle bot got $collectedState instead", collectedState is Idle)
    }

    @Test
    fun fetchPlayerInfo_emits_to_the_state_flow_the_fetchedPlayerInfo_state() = runTest {
        // Arrange
        val sut = MatchHistoryViewModel(userInfoRepo, gomokuService.gameService)
        // Act
        val gate = SuspendingGate()
        var lastCollectedState: MatchHistoryScreenState? = null
        val collectJob = launch {
            sut.state.collectLatest {
                if (it is FetchedPlayerInfo) {
                    lastCollectedState = it
                    gate.open()
                }
            }
        }
        sut.fetchPlayerInfo()

        // Lets wait for the flow to emit the latest value
        withTimeout(1000) {
            gate.await()
            collectJob.cancelAndJoin()
        }

        // Assert
        val loaded = lastCollectedState as? FetchedPlayerInfo
        Assert.assertNotNull("Expected Loaded but got $lastCollectedState instead", loaded)
    }

    @Test
    fun fetchPlayerInfo_emits_to_the_state_flow_the_fetchingPlayerInfo_state() = runTest {
        // Arrange
        val sut = MatchHistoryViewModel(userInfoRepo, gomokuService.gameService)
        // Act
        val gate = SuspendingGate()
        var lastCollectedState: MatchHistoryScreenState? = null
        val collectJob = launch {
            sut.state.collect {
                if (it is FetchingPlayerInfo) {
                    lastCollectedState = it
                    gate.open()
                }
            }
        }
        sut.fetchPlayerInfo()

        // Lets wait for the flow to emit the latest value
        withTimeout(1000) {
            gate.await()
            collectJob.cancelAndJoin()
        }

        // Assert
        val loading = lastCollectedState as? FetchingPlayerInfo
        Assert.assertNotNull("Expected Loading but got $lastCollectedState instead", loading)
    }
}