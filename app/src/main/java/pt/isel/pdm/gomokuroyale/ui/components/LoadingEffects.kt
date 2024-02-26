package pt.isel.pdm.gomokuroyale.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pt.isel.pdm.gomokuroyale.ui.theme.Brown

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator(
            modifier= Modifier.fillMaxHeight(),
            color = Brown,
        )
    }
}
