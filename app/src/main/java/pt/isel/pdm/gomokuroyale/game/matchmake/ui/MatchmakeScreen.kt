package pt.isel.pdm.gomokuroyale.game.matchmake.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.game.lobby.domain.Variants
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import pt.isel.pdm.gomokuroyale.http.domain.MatchmakingStatus
import pt.isel.pdm.gomokuroyale.ui.TipInfoBox
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme

const val MatchMakingScreenTestTag = "MatchMakingScreenTestTag"
const val MatchMakingCancelButtonTestTag = "MatchMakingCancelButtonTestTag"

@Composable
fun MatchmakerScreen(
    status: MatchmakingStatus,
    variant: Variant,
    onCancelingEnabled: Boolean = true,
    onCancelingMatchmaking: () -> Unit = {},
) {
    GomokuRoyaleTheme {
        Column(
            modifier = Modifier.fillMaxSize().testTag(MatchMakingScreenTestTag),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (status) {
                    MatchmakingStatus.PENDING ->
                        Text(
                            text = stringResource(id = R.string.matchmaking),
                            style = MaterialTheme.typography.titleLarge
                        )

                    MatchmakingStatus.MATCHED ->
                        Text(
                            text = stringResource(id = R.string.matchmaking_match_found),
                            style = MaterialTheme.typography.titleLarge
                        )
                }
            }
            if (status == MatchmakingStatus.PENDING)
                Button(modifier = Modifier.testTag(MatchMakingCancelButtonTestTag), onClick = onCancelingMatchmaking, enabled = onCancelingEnabled) {
                    Text(text = "Cancel")
                }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                TipInfoBox(tipText = variant.toTip())
            }
        }
    }
}

@Composable
fun Variant.toTip(): String = when (name) {
    Variants.Standard.value -> stringResource(id = R.string.standard_variant_tip)
    Variants.Omok.value -> stringResource(id = R.string.omok_variant_tip)
    Variants.Renju.value -> stringResource(id = R.string.renju_variant_tip)
    Variants.Pente.value -> stringResource(id = R.string.pente_variant_tip)
    Variants.Caro.value -> stringResource(id = R.string.caro_variant_tip)
    Variants.NinukiRenju.value -> stringResource(id = R.string.ninuki_renju_variant_tip)
    Variants.Swap.value -> stringResource(id = R.string.swap_variant_tip)
    else -> "Missing tip for variant $name"
}

@Preview(showBackground = true)
@Composable
fun MatchmakerScreenPreview() {
    MatchmakerScreen(MatchmakingStatus.PENDING, variant = stdVariant)
}

@Preview(showBackground = true)
@Composable
fun MatchmakerScreenMatchedPreview() {
    MatchmakerScreen(MatchmakingStatus.MATCHED, variant = stdVariant)
}

//Mock data for preview
val stdVariant = Variant(
    name = "STANDARD",
    boardDim = 15,
    openingRule = "STANDARD",
    playRule = "STANDARD",
    points = 0,
)