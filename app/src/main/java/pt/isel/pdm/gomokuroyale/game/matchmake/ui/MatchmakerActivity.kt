package pt.isel.pdm.gomokuroyale.game.matchmake.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
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
import pt.isel.pdm.gomokuroyale.game.lobby.domain.MatchInfo
import pt.isel.pdm.gomokuroyale.game.lobby.ui.LobbyActivity
import pt.isel.pdm.gomokuroyale.game.matchmake.domain.StartGameInfo
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import pt.isel.pdm.gomokuroyale.game.play.ui.GameActivity
import pt.isel.pdm.gomokuroyale.http.domain.MatchmakingStatus.MATCHED
import pt.isel.pdm.gomokuroyale.http.domain.MatchmakingStatus.PENDING
import pt.isel.pdm.gomokuroyale.ui.ErrorAlert

const val MATCHMAKER_ACTIVITY_TAG = "MATCHMAKER_ACTIVITY_TAG"
const val PLAYER_INFO_EXTRA = "PLAYER_INFO_EXTRA"

class MatchmakerActivity : ComponentActivity() {

    private val dependencies by lazy { application as DependenciesContainer }

    private val viewModel by viewModels<MatchmakerViewModel> {
        MatchmakerViewModel.factory(
            dependencies.gomokuService.gameService,
            matchInfo,
            dependencies.userInfoRepository
        )
    }

    companion object {
        fun navigateTo(ctx: Context, info: MatchInfo) {
            ctx.startActivity(createIntent(ctx, info))
        }

        private fun createIntent(ctx: Context, matchInfo: MatchInfo): Intent {
            val intent = Intent(ctx, MatchmakerActivity::class.java)
            matchInfo.let { intent.putExtra(PLAYER_INFO_EXTRA, PlayerInfoExtra(it)) }
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.screenState.collect {
                if (it is MatchmakingScreenState.Idle) {
                    viewModel.findGame()
                }
                if (it is MatchmakingScreenState.Matched) {
                    GameActivity.navigateTo(
                        this@MatchmakerActivity,
                        StartGameInfo(it.gameId, matchInfo.userInfo)
                    )
                }
                if (it is MatchmakingScreenState.LeftQueue) {
                    finish()
                }
            }

        }

        setContent {
            val currentState = viewModel.screenState.collectAsState().value
            MatchmakerScreen(
                status = currentState.status,
                onCancelingMatchmaking = viewModel::leaveQueue,
                onCancelingEnabled = currentState.isCancelable,
                variant = matchInfo.variant
            )
            currentState.let {
                if (it is MatchmakingScreenState.Error) {
                    ErrorAlert(
                        title = stringResource(id = R.string.matchmaking_error),
                        message = it.error.message ?: "Unknown error",
                        onDismiss = { LobbyActivity.navigateTo(this) }
                    )
                }
            }
        }
    }

    private val MatchmakingScreenState.status
        get() = if (this is MatchmakingScreenState.Matched) MATCHED else PENDING

    private val MatchmakingScreenState.isCancelable
        get() = this !is MatchmakingScreenState.Matched && this !is MatchmakingScreenState.Queueing

    private val matchInfo: MatchInfo by lazy {
        checkNotNull(getPlayerInfoExtra()) { "The match info must be provided." }.toMatchInfo()
    }

    @Suppress("DEPRECATION")
    private fun getPlayerInfoExtra(): PlayerInfoExtra? =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(PLAYER_INFO_EXTRA, PlayerInfoExtra::class.java)
        else
            intent.getParcelableExtra(PLAYER_INFO_EXTRA)

    @Parcelize
    data class PlayerInfoExtra(
        val username: String,
        val token: String,
        val points: Int,
        val variant: ParcelableVariant
    ) : Parcelable {
        constructor(matchInfo: MatchInfo) : this(
            username = matchInfo.userInfo.username,
            token = matchInfo.userInfo.accessToken,
            points = matchInfo.variant.points,
            variant = ParcelableVariant(matchInfo.variant)
        )
    }

    private fun PlayerInfoExtra.toMatchInfo() = MatchInfo(
        userInfo = UserInfo(token, username),
        variant = variant.toVariant()
    )

    @Parcelize
    data class ParcelableVariant(
        val name: String,
        val boardDim: Int,
        val playRule: String,
        val openingRule: String,
        val points: Int
    ) : Parcelable {
        constructor(variant: Variant) : this(
            name = variant.name,
            boardDim = variant.boardDim,
            playRule = variant.playRule,
            openingRule = variant.openingRule,
            points = variant.points
        )

        fun toVariant(): Variant {
            return Variant(name, boardDim, playRule, openingRule, points)
        }
    }
}