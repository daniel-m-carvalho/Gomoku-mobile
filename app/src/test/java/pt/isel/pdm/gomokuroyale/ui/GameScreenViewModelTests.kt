package pt.isel.pdm.gomokuroyale.ui

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.gomokuroyale.authentication.domain.Email
import pt.isel.pdm.gomokuroyale.authentication.domain.Id
import pt.isel.pdm.gomokuroyale.authentication.domain.User
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfo
import pt.isel.pdm.gomokuroyale.game.matchmake.domain.StartGameInfo
import pt.isel.pdm.gomokuroyale.game.play.domain.Game
import pt.isel.pdm.gomokuroyale.game.play.domain.board.Board
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import pt.isel.pdm.gomokuroyale.game.play.ui.GameScreenState
import pt.isel.pdm.gomokuroyale.game.play.ui.GameScreenViewModel
import pt.isel.pdm.gomokuroyale.http.GomokuService
import pt.isel.pdm.gomokuroyale.util.HttpResult
import pt.isel.pdm.gomokuroyale.utils.MockMainDispatcherRule
import pt.isel.pdm.gomokuroyale.utils.SuspendingGate

@OptIn(ExperimentalCoroutinesApi::class)
class GameScreenViewModelTests {

    @get:Rule
    val rule = MockMainDispatcherRule(testDispatcher = StandardTestDispatcher())

    private val testUserInfo = UserInfo("test", "test")

    private val gomokuService = mockk<GomokuService> {
        coEvery {
            gameService.getGame(
                1,
                testUserInfo.accessToken
            )
        } coAnswers {
            HttpResult.Success(
                Game(
                    id = 1,
                    userBlack = User(
                        id = Id(1),
                        username = "user1",
                        email = Email("mock1@example.com"),
                    ),
                    userWhite = User(
                        id = Id(2),
                        username = "user2",
                        email = Email("mock2@example.com"),
                    ),
                    board = Board.EMPTY,
                    state = "PLAYER_BLACK_TURN",
                    variant = Variant(
                        name = "Standard",
                        boardDim = 15,
                        playRule = "Standard",
                        openingRule = "Standard",
                        points = 5
                    )
                )
            )
        }
    }

    private val testSut = GameScreenViewModel(
        gomokuService.gameService,
        StartGameInfo(
            gameId = 1,
            localPlayer = testUserInfo
        )
    )

    @Test
    fun initially_the_state_flow_is_loading() = runTest {
        // Arrange
        val sut = testSut
        // Act
        val gate = SuspendingGate()
        var collectedState: GameScreenState? = null
        val collectJob = launch {
            sut.screenStateFlow.collect {
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
        Assert.assertTrue("Expected Idle bot got $collectedState instead", collectedState is GameScreenState.Loading)
    }
}