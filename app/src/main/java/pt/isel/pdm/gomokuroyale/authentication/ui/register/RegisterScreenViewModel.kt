package pt.isel.pdm.gomokuroyale.authentication.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.http.domain.users.UserId
import pt.isel.pdm.gomokuroyale.http.services.users.UserService
import pt.isel.pdm.gomokuroyale.util.IOState
import pt.isel.pdm.gomokuroyale.util.Idle
import pt.isel.pdm.gomokuroyale.util.Loading
import pt.isel.pdm.gomokuroyale.util.idle
import pt.isel.pdm.gomokuroyale.util.loadFailure
import pt.isel.pdm.gomokuroyale.util.loaded
import pt.isel.pdm.gomokuroyale.util.loading
import pt.isel.pdm.gomokuroyale.util.onFailureResult
import pt.isel.pdm.gomokuroyale.util.onSuccessResult

class RegisterScreenViewModel(
    private val userService: UserService
) : ViewModel() {

    private val _state = MutableStateFlow<IOState<UserId?>>(idle())

    val state: Flow<IOState<UserId?>> get() = _state.asStateFlow()

    fun register(username: String, email: String, password: String) {
        if (_state.value !is Idle)
            throw IllegalStateException("Cannot register while loading")
        _state.value = loading()
        viewModelScope.launch {
            val response = userService.register(username, email, password)
            response.onSuccessResult {
                _state.value = loaded(Result.success(UserId(it.uid)))
            }.onFailureResult {
                _state.value = loadFailure(it.cause ?: Exception(it.message))
            }
        }
    }

    fun resetToIdle() {
        if (_state.value is Loading)
            throw IllegalStateException("Cannot reset while loading")
        _state.value = idle()
    }

    companion object {
        fun factory(userService: UserService) = viewModelFactory {
            initializer { RegisterScreenViewModel(userService) }
        }
    }
}