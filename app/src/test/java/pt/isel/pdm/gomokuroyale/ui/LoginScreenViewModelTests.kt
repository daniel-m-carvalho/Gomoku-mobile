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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfo
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfoRepository
import pt.isel.pdm.gomokuroyale.authentication.ui.login.LoginScreenViewModel
import pt.isel.pdm.gomokuroyale.http.GomokuService
import pt.isel.pdm.gomokuroyale.http.domain.users.UserToken
import pt.isel.pdm.gomokuroyale.util.HttpResult
import pt.isel.pdm.gomokuroyale.util.IOState
import pt.isel.pdm.gomokuroyale.util.Idle
import pt.isel.pdm.gomokuroyale.util.Saved
import pt.isel.pdm.gomokuroyale.utils.MockMainDispatcherRule
import pt.isel.pdm.gomokuroyale.utils.SuspendingGate

@OptIn(ExperimentalCoroutinesApi::class)
class LoginScreenViewModelTests {

    @get:Rule
    val rule = MockMainDispatcherRule(testDispatcher = StandardTestDispatcher())

    private val testUserInfo = UserInfo("test", "test")
    private val userInfoRepo = mockk<UserInfoRepository> {
        coEvery { getUserInfo() } coAnswers { testUserInfo }
    }

    private val gomokuService = mockk<GomokuService> {
        coEvery { userService.login("test", "test") } coAnswers { HttpResult.Success(UserToken( testUserInfo.accessToken)) }
    }

    @Test
    fun initially_the_state_flow_is_idle() = runTest {
        // Arrange
        val sut = LoginScreenViewModel(userInfoRepo, gomokuService.userService)
        // Act
        val gate = SuspendingGate()
        var collectedState: IOState<UserInfo?>? = null
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
        assertTrue("Expected Idle bot got $collectedState instead", collectedState is Idle)
    }

    @Test
    fun login_emits_to_the_state_flow_the_saved_state() = runTest {
        // Arrange
        val sut = LoginScreenViewModel(userInfoRepo, gomokuService.userService)
        // Act
        val gate = SuspendingGate()
        var lastCollectedState: IOState<UserInfo?>? = null
        val collectJob = launch {
            sut.state.collectLatest {
                if (it is Saved) {
                    lastCollectedState = it
                    gate.open()
                }
            }
        }
        sut.login("test", "test")

        // Lets wait for the flow to emit the latest value
        withTimeout(1000) {
            gate.await()
            collectJob.cancelAndJoin()
        }

        // Assert
        val loading = lastCollectedState as? Saved
        assertNotNull("Expected Loading but got $lastCollectedState instead", loading)
    }
}