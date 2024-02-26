package pt.isel.pdm.gomokuroyale.game.play.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.game.matchmake.domain.StartGameInfo
import pt.isel.pdm.gomokuroyale.game.play.domain.board.Cell
import pt.isel.pdm.gomokuroyale.http.services.games.GameService
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GamePlayInputModel
import pt.isel.pdm.gomokuroyale.util.onFailureResult
import pt.isel.pdm.gomokuroyale.util.onSuccessResult

/**
 * The tag used to identify log messages on the game screen view model.
 */
const val GameScreenViewModelTag = "GAME_SCREEN_VIEW_MODEL"

class GameScreenViewModel(
    private val gameService: GameService,
    private val startGameInfo: StartGameInfo
) : ViewModel() {

    private val _screenStateFlow: MutableStateFlow<GameScreenState> =
        MutableStateFlow(GameScreenState.Loading)

    val screenStateFlow get() = _screenStateFlow

    fun monitorGame() {
        check(_screenStateFlow.value !is GameScreenState.MyTurn) {
            "Cannot start a match when the screen state is playing"
        }
        viewModelScope.launch {
            gameService.getGame(startGameInfo.gameId, startGameInfo.localPlayer.accessToken)
                .onSuccessResult { game ->
                    if (game.isOver) {
                        _screenStateFlow.value = GameScreenState.GameOver(
                            game,
                            game.variant.points,
                            game.winner
                        )
                    } else {
                        if (game.isMyTurn(startGameInfo.localPlayer.username)) {
                            _screenStateFlow.value = GameScreenState.Playing(game)
                        } else {
                            _screenStateFlow.value = GameScreenState.WaitingForOpponent(game)
                            while (true) {
                                gameService.getGame(
                                    startGameInfo.gameId,
                                    startGameInfo.localPlayer.accessToken
                                ).onSuccessResult { g ->
                                    if (g.isOver) {
                                        _screenStateFlow.value = GameScreenState.GameOver(
                                            g,
                                            g.variant.points,
                                            g.winner
                                        )
                                        return@launch
                                    } else
                                        if (g.isMyTurn(startGameInfo.localPlayer.username)) {
                                            _screenStateFlow.value = GameScreenState.Playing(g)
                                            return@launch
                                        }
                                }.onFailureResult {
                                    _screenStateFlow.value = GameScreenState.Error(it)
                                }
                                delay(POLLING_DELAY)
                            }
                        }
                    }
                }.onFailureResult {
                    _screenStateFlow.value = GameScreenState.Error(it)
                }
        }
    }

    fun makeMove(at: Cell) {
        check(_screenStateFlow.value is GameScreenState.MyTurn) {
            "Cannot make a move when the screen state is not playing"
        }
        viewModelScope.launch {
            gameService.play(
                startGameInfo.gameId,
                startGameInfo.localPlayer.accessToken,
                GamePlayInputModel(at.row.number, at.col.index)
            ).onSuccessResult { game ->
                if (game.isOver)
                    _screenStateFlow.value = GameScreenState.GameOver(
                        game,
                        game.variant.points,
                        game.winner
                    )
                else
                    _screenStateFlow.value = GameScreenState.WaitingForOpponent(game)

            }.onFailureResult { apiError ->
                Log.e(GameScreenViewModelTag, "Error making move: $apiError")
                _screenStateFlow.value = GameScreenState.BadMove(
                    apiError,
                    (screenStateFlow.value as GameScreenState.MyTurn).game
                )
            }
        }
    }

    fun forfeit() {
        check(_screenStateFlow.value is GameScreenState.MyTurn) {
            "Cannot forfeit when the screen state is not playing"
        }
        _screenStateFlow.value = GameScreenState.Forfeit
        viewModelScope.launch {
            gameService.surrender(startGameInfo.gameId, startGameInfo.localPlayer.accessToken)
                .onSuccessResult {
                    val game = (screenStateFlow.value as GameScreenState.MyTurn).game
                    _screenStateFlow.value = GameScreenState.GameOver(
                        game,
                        game.variant.points,
                        game.winner
                    )
                }.onFailureResult { apiError ->
                    Log.e(GameScreenViewModelTag, "Error making move: $apiError")
                    _screenStateFlow.value = GameScreenState.Error(apiError)
                }
        }
    }

    fun keepOnPlaying() = _screenStateFlow.value.let { screenState ->
        if (screenState is GameScreenState.BadMove)
            _screenStateFlow.value = GameScreenState.Playing(screenState.game)
    }

    fun resetToLoading() = _screenStateFlow.value.let { screenState ->
        if (screenState is GameScreenState.Error)
            _screenStateFlow.value = GameScreenState.Loading
    }

    companion object {
        private const val POLLING_DELAY = 4000L

        fun factory(gameService: GameService, startGameInfo: StartGameInfo) = viewModelFactory {
            initializer { GameScreenViewModel(gameService, startGameInfo) }
        }
    }
}