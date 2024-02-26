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
import pt.isel.pdm.gomokuroyale.http.domain.PaginationLinks
import pt.isel.pdm.gomokuroyale.http.domain.users.RankingList
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenViewModel
import pt.isel.pdm.gomokuroyale.util.HttpResult
import pt.isel.pdm.gomokuroyale.utils.MockMainDispatcherRule
import pt.isel.pdm.gomokuroyale.utils.SuspendingGate

@OptIn(ExperimentalCoroutinesApi::class)
class RankingScreenViewModelTests {

    @get:Rule
    val rule = MockMainDispatcherRule(testDispatcher = StandardTestDispatcher())

    private val testUserInfo = UserInfo("test", "test")
    private val userInfoRepo = mockk<UserInfoRepository> {
        coEvery { getUserInfo() } coAnswers { testUserInfo }
    }

    private val gomokuService = mockk<GomokuService> {
        coEvery {
            userService.getRankingInfo(1)
        } coAnswers {
            HttpResult.Success(
                RankingList(
                    listOf(),
                    PaginationLinks(
                        null,
                        null,
                        null,
                        null
                    )
                )
            )
        }
    }

    @Test
    fun initially_the_state_flow_is_idle() = runTest {
        // Arrange
        val sut = RankingScreenViewModel(gomokuService.userService, userInfoRepo)
        // Act
        val gate = SuspendingGate()
        var collectedState: RankingScreenState? = null
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
        Assert.assertTrue("Expected Idle bot got $collectedState instead", collectedState is RankingScreenState.Idle)
    }

    @Test
    fun getPlayers_emits_to_the_state_flow_the_failedToFetchRankingInfo_state() = runTest {
        // Arrange
        val sut = RankingScreenViewModel(gomokuService.userService, userInfoRepo)
        // Act
        val gate = SuspendingGate()
        var lastCollectedState: RankingScreenState? = null
        val collectJob = launch {
            sut.state.collectLatest {
                if (it is RankingScreenState.FetchedRankingInfo) {
                    lastCollectedState = it
                    gate.open()
                }
            }
        }
        sut.getPlayers()

        // Lets wait for the flow to emit the latest value
        withTimeout(1000) {
            gate.await()
            collectJob.cancelAndJoin()
        }

        // Assert
        val loaded = lastCollectedState as? RankingScreenState.FetchedRankingInfo
        Assert.assertNotNull("Expected Loaded but got $lastCollectedState instead", loaded)
    }

    @Test
    fun getPlayers_emits_to_the_state_flow_the_fetchingRankingInfo_state() = runTest {
        // Arrange
        val sut = RankingScreenViewModel(gomokuService.userService, userInfoRepo)
        // Act
        val gate = SuspendingGate()
        var lastCollectedState: RankingScreenState? = null
        val collectJob = launch {
            sut.state.collect {
                if (it is RankingScreenState.FetchingRankingInfo) {
                    lastCollectedState = it
                    gate.open()
                }
            }
        }
        sut.getPlayers()

        // Lets wait for the flow to emit the latest value
        withTimeout(1000) {
            gate.await()
            collectJob.cancelAndJoin()
        }

        // Assert
        val loading = lastCollectedState as? RankingScreenState.FetchingRankingInfo
        Assert.assertNotNull("Expected Loading but got $lastCollectedState instead", loading)
    }
}