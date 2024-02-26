package pt.isel.pdm.gomokuroyale.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.gomokuroyale.R

/**
 * A simple alert that displays a message and a button to dismiss it.
 * For underdeveloped features.
 */
@Composable
fun DevelopingAlert(onDismiss : () -> Unit = {}) {
    ErrorAlert(
        title = R.string.developing_feature_title,
        message = R.string.developing_feature,
        onDismiss = onDismiss
    )
}

@Preview(showBackground = true)
@Composable
fun DevelopingAlertPreview() {
    DevelopingAlert()
}