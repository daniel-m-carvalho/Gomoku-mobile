package pt.isel.pdm.gomokuroyale.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MyIcon(resultId: Int, size: Dp = 40.dp, padding: Dp = 5.dp){
    Image(
        painter = painterResource(id = resultId),
        contentDescription = null,
        modifier = Modifier.size(size).padding(padding)
    )
}