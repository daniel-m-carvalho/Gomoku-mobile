package pt.isel.pdm.gomokuroyale.game.lobby.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfoRepository
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FailedToFetch
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchedMatchInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchedPlayerInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchingMatchInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchingPlayerInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchedVariants
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchingVariants
import pt.isel.pdm.gomokuroyale.game.lobby.domain.Idle
import pt.isel.pdm.gomokuroyale.game.lobby.domain.LobbyScreenState
import pt.isel.pdm.gomokuroyale.game.lobby.domain.MatchInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.VariantRepository
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant

class LobbyScreenViewModel(
    private val repository: UserInfoRepository,
    private val variantRepository: VariantRepository
) : ViewModel() {

    private val _state = MutableStateFlow<LobbyScreenState>(Idle)

    val state: Flow<LobbyScreenState> get() = _state.asStateFlow()

    fun fetchPlayerInfo() {
        check(_state.value is Idle) { "Cannot fetch user info while loading" }

        _state.value = FetchingPlayerInfo
        viewModelScope.launch {
            val isLogged = kotlin.runCatching { repository.isLoggedIn() }
            if (isLogged.getOrNull() == true) {
                val result = kotlin.runCatching { repository.getUserInfo() }
                val res = result.getOrNull()
                if (res == null)
                    _state.value = FailedToFetch(Exception("Failed to fetch user info"))
                else
                    _state.value = FetchedPlayerInfo(res)
            } else
                _state.value = FailedToFetch(Exception("Failure of the token, time overrun"))
        }
    }

    fun fetchMatchInfo(variant: Variant) {
        val value = _state.value
        check(value is FetchedVariants) {
            "Cannot fetch match info while loading"
        }
        _state.value = FetchingMatchInfo(checkNotNull(value.userInfo))
        viewModelScope.launch {
            val result = kotlin.runCatching { repository.getUserInfo() }
            val userInfo = checkNotNull(result.getOrNull())
            _state.value = FetchedMatchInfo(MatchInfo(userInfo, variant))
        }
    }

    fun resetToIdle() {
        check(_state.value is FetchedMatchInfo) { "Cannot reset to fetching player info while loading" }
        _state.value = Idle
    }

    fun fetchVariants() {
        check(_state.value is FetchedPlayerInfo) { "Cannot fetch variants while loading" }
        val userInfo = checkNotNull((_state.value as FetchedPlayerInfo).userInfo)
        _state.value = FetchingVariants(userInfo)

        viewModelScope.launch {
            val result = kotlin.runCatching { variantRepository.getVariants() }
            val res = result.getOrNull()
            if (res == null)
                _state.value = FailedToFetch(Exception("Failed to fetch variants"))
            else
                _state.value = FetchedVariants(variants = res, userInfo = userInfo)
        }
    }

    companion object {
        fun factory(userInfoRepository: UserInfoRepository, variantRepository: VariantRepository) =
            viewModelFactory {
                initializer { LobbyScreenViewModel(userInfoRepository, variantRepository) }
            }
    }
}