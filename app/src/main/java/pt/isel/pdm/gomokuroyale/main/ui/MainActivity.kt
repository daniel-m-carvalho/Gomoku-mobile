package pt.isel.pdm.gomokuroyale.main.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.about.ui.AboutActivity
import pt.isel.pdm.gomokuroyale.authentication.ui.login.LoginActivity
import pt.isel.pdm.gomokuroyale.authentication.ui.register.RegisterActivity
import pt.isel.pdm.gomokuroyale.GomokuRoyaleApplication
import pt.isel.pdm.gomokuroyale.TAG
import pt.isel.pdm.gomokuroyale.game.lobby.ui.LobbyActivity
import pt.isel.pdm.gomokuroyale.main.ui.MainScreenState.FailedToLogout
import pt.isel.pdm.gomokuroyale.main.ui.MainScreenState.FailedToFetch
import pt.isel.pdm.gomokuroyale.main.ui.MainScreenState.Idle
import pt.isel.pdm.gomokuroyale.main.ui.MainScreenState.FetchedVariants
import pt.isel.pdm.gomokuroyale.main.ui.MainScreenState.FetchedRecipes
import pt.isel.pdm.gomokuroyale.main.ui.MainScreenState.FetchedPlayerInfo
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingActivity
import pt.isel.pdm.gomokuroyale.ui.ErrorAlert

class MainActivity : ComponentActivity() {

    private val app by lazy { application as GomokuRoyaleApplication }

    private val viewModel by viewModels<MainScreenViewModel> {
        MainScreenViewModel.factory(
            app.userInfoRepository,
            app.gomokuService,
            app.uriRepository,
            app.variantRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate() called")
        lifecycleScope.launch {
            viewModel.state.collect {
                if (it is Idle) {
                    viewModel.updateRecipes()
                }
                if (it is FetchedRecipes) {
                    viewModel.updateVariants()
                }
                if (it is FetchedVariants) {
                    viewModel.fetchPlayerInfo()
                }
            }
        }

        setContent {
            val currentState = viewModel.state.collectAsState(initial = Idle).value
            val (token, isLoggedIn) = getTokenAndLoginStatus(currentState)
            MainScreen(
                isLoggedIn = isLoggedIn,
                onLoginRequested = { LoginActivity.navigateTo(this) },
                onRegisterRequested = { RegisterActivity.navigateTo(this) },
                onCreateGameRequested = { LobbyActivity.navigateTo(this) },
                onInfoRequested = { AboutActivity.navigateTo(this) },
                onRankingRequested = { RankingActivity.navigateTo(this) },
                onLogoutRequested = { viewModel.logout(token) },
            )
            currentState.let {
                if (it is FailedToFetch || it is FailedToLogout)
                    ErrorAlert(
                        title = "Main Screen Error",
                        message = getErrorMessage(it),
                        buttonText = "Ok",
                        onDismiss = viewModel::resetToIdle
                    )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "onStart() called")
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy() called")
    }

    companion object {
        private fun getErrorMessage(state: MainScreenState): String = when (state) {
            is FailedToFetch -> state.error.message ?: UNKNOWN_ERROR
            is FailedToLogout -> state.error.message ?: UNKNOWN_ERROR
            else -> UNKNOWN_ERROR
        }

        private fun getTokenAndLoginStatus(state: MainScreenState): Pair<String?, Boolean> =
            if (state is FetchedPlayerInfo)
                state.userInfo.getOrNull()?.accessToken to state.isLoggedIn
            else
                null to false

        private const val UNKNOWN_ERROR = "Unknown error"

        fun navigateTo(origin: ComponentActivity) {
            val intent = Intent(origin, MainActivity::class.java)
            origin.startActivity(intent)
        }
    }
}

