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
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FailedToFetch
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchingPlayerInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.Idle
import pt.isel.pdm.gomokuroyale.game.lobby.domain.LobbyScreenState
import pt.isel.pdm.gomokuroyale.game.lobby.domain.VariantRepository
import pt.isel.pdm.gomokuroyale.game.lobby.ui.LobbyScreenViewModel
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import pt.isel.pdm.gomokuroyale.utils.MockMainDispatcherRule
import pt.isel.pdm.gomokuroyale.utils.SuspendingGate

@OptIn(ExperimentalCoroutinesApi::class)
class LobbyScreenViewModelTests {

    @get:Rule
    val rule = MockMainDispatcherRule(testDispatcher = StandardTestDispatcher())

    private val testUserInfo = UserInfo("test", "test")
    private val userInfoRepo = mockk<UserInfoRepository> {
        coEvery { getUserInfo() } coAnswers { testUserInfo }
    }

    private val testVariants = listOf(
        Variant("STANDARD", 15, "Standard", "Standard", 5),
        Variant("Pente", 15, "Standard", "Standard", 5),
        Variant("RENJU", 15, "Standard", "Standard", 5),
    )
    private val variantRepo = mockk<VariantRepository> {
        coEvery { getVariants() } coAnswers { testVariants }
    }

    @Test
    fun initially_the_state_flow_is_idle() = runTest {
        // Arrange
        val sut = LobbyScreenViewModel(userInfoRepo, variantRepo)
        // Act
        val gate = SuspendingGate()
        var collectedState: LobbyScreenState? = null
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
    fun fetchPlayerInfo_emits_to_the_state_flow_the_failedToFetch_state() = runTest {
        // Arrange
        val sut = LobbyScreenViewModel(userInfoRepo, variantRepo)
        // Act
        val gate = SuspendingGate()
        var lastCollectedState: LobbyScreenState? = null
        val collectJob = launch {
            sut.state.collectLatest {
                if (it is FailedToFetch) {
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
        val loaded = lastCollectedState as? FailedToFetch
        Assert.assertNotNull("Expected Loaded but got $lastCollectedState instead", loaded)
    }

    @Test
    fun fetchPlayerInfo_emits_to_the_state_flow_the_fetchingPlayerInfo_state() = runTest {
        // Arrange
        val sut = LobbyScreenViewModel(userInfoRepo, variantRepo)
        // Act
        val gate = SuspendingGate()
        var lastCollectedState: LobbyScreenState? = null
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

    @Test(expected = IllegalStateException::class)
    fun fetchPlayerInfo_throws_if_state_is_idle() {
        // Arrange
        val sut = LobbyScreenViewModel(userInfoRepo, variantRepo)
        sut.fetchPlayerInfo()

        // Act
        sut.fetchPlayerInfo()
    }
}