package pt.isel.pdm.gomokuroyale.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import pt.isel.pdm.gomokuroyale.matchHistory.domain.InfoParam


@Composable
fun DrawCircle(dim: Float, color: Color, infoParam: InfoParam) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier
                .size(dim.dp)
                .graphicsLayer {
                    clip = true
                    shape = CircleShape
                }
                .background(color)
        ) {
            val textColor = if(color == Color.Black) Color.White else Color.Black
            Text(
                text = "${infoParam.name}\n",
                style = TextStyle(color = textColor),
                modifier = Modifier.align(Alignment.Center)
            )
            Text(
                text = "\n${infoParam.value}",
                style = TextStyle(color = textColor),
                modifier = Modifier.align(Alignment.Center)
            )
        }

    }
}