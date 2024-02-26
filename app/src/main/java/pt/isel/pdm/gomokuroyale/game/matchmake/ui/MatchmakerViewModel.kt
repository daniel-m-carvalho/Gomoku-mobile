package pt.isel.pdm.gomokuroyale.game.matchmake.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfoRepository
import pt.isel.pdm.gomokuroyale.game.lobby.domain.MatchInfo
import pt.isel.pdm.gomokuroyale.game.matchmake.ui.MatchmakingScreenState.Error
import pt.isel.pdm.gomokuroyale.game.matchmake.ui.MatchmakingScreenState.Idle
import pt.isel.pdm.gomokuroyale.game.matchmake.ui.MatchmakingScreenState.LeftQueue
import pt.isel.pdm.gomokuroyale.game.matchmake.ui.MatchmakingScreenState.LookingForMatch
import pt.isel.pdm.gomokuroyale.game.matchmake.ui.MatchmakingScreenState.Matched
import pt.isel.pdm.gomokuroyale.game.matchmake.ui.MatchmakingScreenState.Queueing
import pt.isel.pdm.gomokuroyale.http.domain.MatchmakingStatus
import pt.isel.pdm.gomokuroyale.http.services.games.GameService
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameMatchmakingInputModel
import pt.isel.pdm.gomokuroyale.util.onFailureResult
import pt.isel.pdm.gomokuroyale.util.onSuccessResult

//TODO: MOVE POLLING TO SERVICE
class MatchmakerViewModel(
    private val service: GameService,
    private val matchInfo: MatchInfo,
    private val repository: UserInfoRepository
) : ViewModel() {

    private val _screenStateFlow: MutableStateFlow<MatchmakingScreenState> =
        MutableStateFlow(Idle)

    val screenState: StateFlow<MatchmakingScreenState> get() = _screenStateFlow.asStateFlow()

    fun findGame() {
        check(_screenStateFlow.value is Idle)
        { "Cannot find a game while in state ${_screenStateFlow.value}" }

        viewModelScope.launch {
            _screenStateFlow.value = Queueing
            val isLogged = kotlin.runCatching { repository.isLoggedIn() }
            if (isLogged.getOrNull() == false) {
                _screenStateFlow.value = Error(Exception("Failure of the token, time overrun"))
                return@launch
            }
            service.matchmaking(
                matchInfo.userInfo.accessToken,
                GameMatchmakingInputModel(matchInfo.variant.name)
            ).onSuccessResult { queueEntry ->
                when (queueEntry.idType) {
                    GAME_TYPE_ID -> _screenStateFlow.value = Matched(queueEntry.id)

                    MATCH_TYPE_ID -> {
                        _screenStateFlow.value = LookingForMatch(queueEntry.id)
                        while (true) {
                            val status = service.getMatchmakingStatus(
                                matchInfo.userInfo.accessToken,
                                queueEntry.id
                            )
                            status.onSuccessResult { matchStatus ->
                                if (matchStatus.state == MatchmakingStatus.MATCHED.toString()) {
                                    _screenStateFlow.value =
                                        Matched(matchStatus.gid!!) // Safe double bang, because we know it is matched
                                    return@launch
                                }
                            }.onFailureResult { error ->
                                _screenStateFlow.value = Error(error)
                                return@launch
                            }
                            delay(POOLING_DELAY)
                        }
                    }

                    else -> _screenStateFlow.value = Error(Exception("Invalid id type"))
                }
            }.onFailureResult {
                _screenStateFlow.value = Error(it)
            }
        }
    }

    fun leaveQueue() {
        check(_screenStateFlow.value is LookingForMatch)
        { "Cannot leave queue while in state ${_screenStateFlow.value}" }
        viewModelScope.launch {
            service.cancelMatchmaking(
                matchInfo.userInfo.accessToken,
                (_screenStateFlow.value as LookingForMatch).matchId
            ).onSuccessResult {
                _screenStateFlow.value = LeftQueue
            }.onFailureResult {
                _screenStateFlow.value = Error(it)
            }
        }
    }

    companion object {
        private const val POOLING_DELAY = 5000L
        private const val GAME_TYPE_ID = "gid"
        private const val MATCH_TYPE_ID = "mid"

        fun factory(
            service: GameService,
            matchInfo: MatchInfo,
            userInfoRepository: UserInfoRepository
        ) =
            viewModelFactory {
                initializer { MatchmakerViewModel(service, matchInfo, userInfoRepository) }
            }
    }
}