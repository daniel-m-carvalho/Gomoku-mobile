package pt.isel.pdm.gomokuroyale.game.play.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.game.play.domain.GameState

const val GameOverDialogButtonTag = "GameOverDialogButtonTag"
const val GameOverDialogTag = "GameOverDialogTag"

@Composable
fun GameOverDialog(
    isWin: Boolean,
    points: Int,
    result: GameState,
    onDismissRequested: () -> Unit = { }
) {
    val dialogText = when {
        result == GameState.DRAW -> stringResource(id = R.string.match_ended_dialog_text_tied_match)
        isWin -> stringResource(id = R.string.match_ended_dialog_text_local_won, points)
        else -> stringResource(id = R.string.match_ended_dialog_text_opponent_won, points)
    }

    AlertDialog(
        onDismissRequest = onDismissRequested,
        confirmButton = {
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp)
            ) {
                OutlinedButton(
                    border = BorderStroke(0.dp, Color.Unspecified),
                    onClick = onDismissRequested,
                    modifier = Modifier.testTag(GameOverDialogButtonTag)
                ) {
                    Text(
                        text = stringResource(id = R.string.match_ended_ok_button),
                        fontSize = 14.sp
                    )
                }
            }
        },
        title = {
            Text(
                stringResource(id = R.string.match_ended_dialog_title),
                textAlign = TextAlign.Center
            )
        },
        text = { Text(dialogText) },
        modifier = Modifier.testTag(GameOverDialogTag)
    )
}

@Preview(showBackground = true)
@Composable
fun GameOverDialogPreview() {
    GameOverDialog(
        isWin = false,
        points = 10,
        result = GameState.PLAYER_BLACK_WON
    )
}