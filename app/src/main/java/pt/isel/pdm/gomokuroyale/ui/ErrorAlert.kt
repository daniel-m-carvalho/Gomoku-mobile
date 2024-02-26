package pt.isel.pdm.gomokuroyale.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme

const val ErrorAlertTestTag = "ErrorAlertTestTag"
const val ErrorAlertDismissButtonTestTag = "ErrorAlertDismissButtonTestTag"

@Composable
fun ErrorAlert(
    @StringRes title: Int,
    @StringRes message: Int,
    @StringRes buttonText: Int = R.string.ok,
    onDismiss: () -> Unit = { }
) {
    ErrorAlertImpl(
        title = stringResource(id = title),
        message = stringResource(id = message),
        buttonText = stringResource(id = buttonText),
        onDismiss = onDismiss
    )
}

@Composable
fun ErrorAlert(
    title: String,
    message: String,
    buttonText: String = "OK",
    onDismiss: () -> Unit = { }
) {
    ErrorAlertImpl(
        title = title,
        message = message,
        buttonText = buttonText,
        onDismiss = onDismiss
    )
}

@Composable
private fun ErrorAlertImpl(
    title: String,
    message: String,
    buttonText: String ,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            OutlinedButton(
                border = BorderStroke(0.dp, Color.Unspecified),
                onClick = onDismiss,
                modifier = Modifier.testTag(ErrorAlertDismissButtonTestTag)
            ) {
                Text(text = buttonText)
            }
        },
        title = { Text(text = title) },
        text = { Text(text = message, textAlign = TextAlign.Justify) },
        icon = {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = "Warning"
            )
        },
        modifier = Modifier.testTag(ErrorAlertTestTag)
    )
}

@Preview
@Composable
private fun ErrorAlertImplPreview() {
    GomokuRoyaleTheme {
        ErrorAlertImpl(
            title = "Error accessing ... ",
            message = "Could not ...",
            buttonText = "OK",
            onDismiss = { }
        )
    }
}