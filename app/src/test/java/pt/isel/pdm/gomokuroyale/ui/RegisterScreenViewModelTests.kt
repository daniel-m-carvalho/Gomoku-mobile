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
import pt.isel.pdm.gomokuroyale.authentication.ui.register.RegisterScreenViewModel
import pt.isel.pdm.gomokuroyale.http.GomokuService
import pt.isel.pdm.gomokuroyale.http.domain.users.UserId
import pt.isel.pdm.gomokuroyale.util.HttpResult
import pt.isel.pdm.gomokuroyale.util.IOState
import pt.isel.pdm.gomokuroyale.util.Idle
import pt.isel.pdm.gomokuroyale.util.Loaded
import pt.isel.pdm.gomokuroyale.utils.MockMainDispatcherRule
import pt.isel.pdm.gomokuroyale.utils.SuspendingGate

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterScreenViewModelTests {

    @get:Rule
    val rule = MockMainDispatcherRule(testDispatcher = StandardTestDispatcher())

    private val gomokuService = mockk<GomokuService> {
        coEvery {
            userService.register("test", "test@gmail.com", "test")
        } coAnswers {
            HttpResult.Success(UserId(1) )
        }
    }

    @Test
    fun initially_the_state_flow_is_idle() = runTest {
        // Arrange
        val sut = RegisterScreenViewModel(gomokuService.userService)
        // Act
        val gate = SuspendingGate()
        var collectedState: IOState<UserId?>? = null
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
    fun register_emits_to_the_state_flow_the_saved_state() = runTest {
        // Arrange
        val sut = RegisterScreenViewModel(gomokuService.userService)
        // Act
        val gate = SuspendingGate()
        var lastCollectedState: IOState<UserId?>? = null
        val collectJob = launch {
            sut.state.collectLatest {
                if (it is Loaded) {
                    lastCollectedState = it
                    gate.open()
                }
            }
        }
        sut.register("test", "test@gmail.com", "test")

        // Lets wait for the flow to emit the latest value
        withTimeout(1000) {
            gate.await()
            collectJob.cancelAndJoin()
        }

        // Assert
        val loading = lastCollectedState as? Loaded
        Assert.assertNotNull("Expected Loading but got $lastCollectedState instead", loading)
    }
}