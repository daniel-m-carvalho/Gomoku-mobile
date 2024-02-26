package pt.isel.pdm.gomokuroyale.matchHistory.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import pt.isel.pdm.gomokuroyale.DependenciesContainer
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.ui.ErrorAlert

const val USERS_EXTRA = "USER_INFO_EXTRA"

class MatchHistoryActivity : ComponentActivity() {

    private val dependencies by lazy { application as DependenciesContainer }

    private val viewModel by viewModels<MatchHistoryViewModel> {
        MatchHistoryViewModel.factory(
            dependencies.userInfoRepository,
            dependencies.gomokuService.gameService
        )
    }

    companion object {
        fun navigateTo(origin: Activity, playerId: Int, username: String) {
            with(origin) {
                val intent = Intent(origin, MatchHistoryActivity::class.java)
                intent.putExtra(USERS_EXTRA, UsersExtra(playerId, username))
                startActivity(intent)
            }
        }

        private const val UNKNOWN_ERROR = "Unknown error"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.state.collect {
                if (it is Idle) {
                    viewModel.fetchPlayerInfo()
                }
                if (it is FetchedPlayerInfo) {
                    viewModel.fetchMatchHistory(userInfo.playerId)
                }
            }
        }
        setContent {
            val currentState = viewModel.state.collectAsState(initial = FetchingPlayerInfo).value
            val modifier = if (currentState is FetchingPlayerInfo) Modifier.shimmer() else Modifier
            val matches =
                if (currentState is FetchedMatchHistory) currentState.matchHistory else emptyList()

            MatchHistoryScreen(
                modifier = modifier,
                onBackRequested = { finish() },
                username = userInfo.username,
                matches = matches
            )
            currentState.let {
                if (it is FailedToFetch)
                    ErrorAlert(
                        title = stringResource(id = R.string.match_history_failed_to_load),
                        message = it.error.message ?: UNKNOWN_ERROR,
                        onDismiss = viewModel::resetToIdle
                    )
            }
        }
    }

    private val userInfo: PlayerInfo by lazy {
        checkNotNull(getUserExtra()) { "No user info provided." }.toPlayerInfo()
    }

    private fun UsersExtra.toPlayerInfo() = PlayerInfo(playerId, username)

    @Suppress("DEPRECATION")
    private fun getUserExtra(): UsersExtra? =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(USERS_EXTRA, UsersExtra::class.java)
        else
            intent.getParcelableExtra(USERS_EXTRA)

    @Parcelize
    data class UsersExtra(val playerId: Int, val username: String) : Parcelable

    data class PlayerInfo(val playerId: Int, val username: String)
}

