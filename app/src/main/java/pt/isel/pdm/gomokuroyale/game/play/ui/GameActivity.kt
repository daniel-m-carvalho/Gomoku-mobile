package pt.isel.pdm.gomokuroyale.game.play.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import pt.isel.pdm.gomokuroyale.DependenciesContainer
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfo
import pt.isel.pdm.gomokuroyale.game.lobby.ui.LobbyActivity
import pt.isel.pdm.gomokuroyale.game.matchmake.domain.StartGameInfo
import pt.isel.pdm.gomokuroyale.game.play.domain.GameState
import pt.isel.pdm.gomokuroyale.ui.ErrorAlert

private const val START_GAME_INFO_EXTRA = "StartGameInfo"

class GameActivity : ComponentActivity() {

    companion object {
        fun navigateTo(ctx: Context, startGameInfo: StartGameInfo) {
            ctx.startActivity(createIntent(ctx, startGameInfo))
        }

        /**
         * Builds the intent used to navigate to the [GameActivity] activity.
         * @param ctx the context to be used.
         * @param startGameInfo the information required to start the game.
         * @return the intent used to navigate to the [GameActivity] activity.
         */
        private fun createIntent(ctx: Context, startGameInfo: StartGameInfo): Intent {
            val intent = Intent(ctx, GameActivity::class.java)
            intent.putExtra(
                START_GAME_INFO_EXTRA, StartGameInfoExtra(
                    UserInfo(
                        startGameInfo.localPlayer.accessToken,
                        startGameInfo.localPlayer.username
                    ),
                    startGameInfo.gameId,
                )
            )
            return intent
        }
    }

    private val viewModel by viewModels<GameScreenViewModel> {
        GameScreenViewModel.factory(
            (application as DependenciesContainer).gomokuService.gameService,
            StartGameInfo(gameId, userInfo)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.screenStateFlow.collect {
                if (it !is GameScreenState.MyTurn && it !is GameScreenState.GameOver
                    && it !is GameScreenState.Error
                ) {
                    viewModel.monitorGame()
                }
            }
        }
        setContent {
            val currentState = viewModel.screenStateFlow.collectAsState().value
            GameScreen(
                currentState,
                onPlayRequested = viewModel::makeMove,
                onForfeitRequested = viewModel::forfeit,
                onHelpRequested = {}
            )
            currentState.let { state ->
                if (state is GameScreenState.BadMove)
                    ErrorAlert(
                        title = stringResource(id = R.string.match_bad_move),
                        message = state.error.message ?: "Unknown error",
                        onDismiss = { viewModel.keepOnPlaying() }
                    )
                if (state is GameScreenState.Error)
                    ErrorAlert(
                        title = "Error",
                        message = state.error.message ?: "Unknown error",
                        onDismiss = { viewModel.resetToLoading() }
                    )
                if (state is GameScreenState.GameOver)
                    GameOverDialog(
                        isWin = state.winner?.username == userInfo.username,
                        result = GameState.valueOf(state.game.state),
                        points = state.points,
                        onDismissRequested = { LobbyActivity.navigateTo(this) }
                    )
            }
        }

        onBackPressedDispatcher.addCallback(owner = this, enabled = true) {
            viewModel.forfeit()
            finish()
        }
    }

    private val userInfo: UserInfo by lazy {
        UserInfo(
            accessToken = startGameInfoExtra.accessToken,
            username = startGameInfoExtra.username
        )
    }

    private val gameId: Int by lazy {
        startGameInfoExtra.gameId
    }

    @Suppress("DEPRECATION")
    private val startGameInfoExtra: StartGameInfoExtra by lazy {
        val extra = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(START_GAME_INFO_EXTRA, StartGameInfoExtra::class.java)
        else
            intent.getParcelableExtra(START_GAME_INFO_EXTRA)

        checkNotNull(extra) { "The start game info extra must be provided." }
    }
}

@Parcelize
internal data class StartGameInfoExtra(
    val gameId: Int,
    val accessToken: String,
    val username: String
) : Parcelable

internal fun StartGameInfoExtra(userInfo: UserInfo, gameId: Int): StartGameInfoExtra {
    return StartGameInfoExtra(gameId, userInfo.accessToken, userInfo.username)
}