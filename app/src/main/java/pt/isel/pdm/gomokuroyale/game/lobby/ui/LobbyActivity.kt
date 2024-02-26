package pt.isel.pdm.gomokuroyale.game.lobby.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.DependenciesContainer
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.authentication.ui.login.LoginActivity
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FailedToFetch
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchedVariants
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchedMatchInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchedPlayerInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchingMatchInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchingPlayerInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchingVariants
import pt.isel.pdm.gomokuroyale.game.lobby.domain.Idle
import pt.isel.pdm.gomokuroyale.game.lobby.domain.LobbyScreenState
import pt.isel.pdm.gomokuroyale.game.lobby.domain.PlayerInfo
import pt.isel.pdm.gomokuroyale.game.matchmake.ui.MatchmakerActivity
import pt.isel.pdm.gomokuroyale.ui.ErrorAlert

class LobbyActivity : ComponentActivity() {

    private val dependencies by lazy { application as DependenciesContainer }

    private val viewModel by viewModels<LobbyScreenViewModel> {
        LobbyScreenViewModel.factory(
            dependencies.userInfoRepository,
            dependencies.variantRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.state.collect {
                if (it is Idle) {
                    viewModel.fetchPlayerInfo()
                }
                if (it is FetchedPlayerInfo) {
                    viewModel.fetchVariants()
                }
                if (it is FetchedMatchInfo) {
                    MatchmakerActivity.navigateTo(this@LobbyActivity, it.matchInfo)
                    viewModel.resetToIdle()
                }
            }
        }

        setContent {
            val currentState = viewModel.state.collectAsState(initial = FetchingPlayerInfo).value
            LobbyScreen(
                modifier = currentState.modifier,
                variants = currentState.variants,
                onPlayEnabled = currentState !is FetchingMatchInfo && currentState !is FetchedMatchInfo,
                onFindGame = { variant ->
                    viewModel.fetchMatchInfo(currentState.variants.first { it.name == variant })
                },
                playerInfo = currentState.userInfo?.let { PlayerInfo(it.username) },
                onNavigationBackRequested = { finish() }
            )

            currentState.let {
                if (it is FailedToFetch)
                    ErrorAlert(
                        title = stringResource(id = R.string.lobby_creation_failed),
                        message = it.error.message ?: "Unknown error",
                        onDismiss = { LoginActivity.navigateTo(this) }
                    )
            }
        }
    }

    companion object {
        fun navigateTo(origin: ComponentActivity) {
            with(origin) {
                val intent = Intent(origin, LobbyActivity::class.java)
                startActivity(intent)
            }
        }

        private val LobbyScreenState.modifier
            get() = when (this) {
                is FetchingPlayerInfo, is FetchingVariants -> Modifier.shimmer()
                else -> Modifier
            }

        private val LobbyScreenState.variants
            get() = when (this) {
                is FetchedVariants -> variants
                else -> emptyList()
            }

        private val LobbyScreenState.userInfo
            get() = when (this) {
                is FetchedPlayerInfo -> userInfo
                is FetchedVariants -> userInfo
                else -> null
            }
    }
}