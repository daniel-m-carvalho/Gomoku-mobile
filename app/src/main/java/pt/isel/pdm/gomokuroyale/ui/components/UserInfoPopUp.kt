package pt.isel.pdm.gomokuroyale.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.http.domain.users.UserRanking
import pt.isel.pdm.gomokuroyale.matchHistory.domain.InfoParam

@Composable
fun UserInfoPopUp(
    onDismissRequest: () -> Unit,
    onMatchHistoryRequested: (Int, String) -> Unit,
    playerInfo: UserRanking
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxHeight(0.7f)
                .clip(RoundedCornerShape(15.dp)),
            shape = RectangleShape,
        ) {

            Spacer(modifier = Modifier.padding(10.dp))
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.White)
                                .size(100.dp),
                            painter = painterResource(id = R.drawable.chapeleiro_louco),
                            contentDescription = "pfp"
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = playerInfo.username,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(15.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DrawCircle(
                        dim = 60f,
                        color = Color.Black,
                        infoParam = InfoParam("Games", " ${playerInfo.gamesPlayed}")
                    )
                    DrawCircle(
                        dim = 60f,
                        color = Color.White,
                        infoParam = InfoParam("Losses", " ${playerInfo.losses}")
                    )
                    DrawCircle(
                        dim = 60f,
                        color = Color.Black,
                        infoParam = InfoParam("Wins", " ${playerInfo.wins}")
                    )
                    DrawCircle(
                        dim = 60f,
                        color = Color.White,
                        infoParam = InfoParam("Draws", " ${playerInfo.draws}")
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { onMatchHistoryRequested(playerInfo.id, playerInfo.username) },
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp)),
                        shape = RectangleShape,
                    ) {
                        Text(text = "Match History")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserInfoPopUpPreview() {
    UserInfoPopUp(
        onDismissRequest = {},
        onMatchHistoryRequested = { _, _ -> },
        playerInfo = UserRanking(
            id = 1,
            username = "username",
            points = 1000,
            wins = 10,
            losses = 10,
            draws = 10,
            gamesPlayed = 30,
            rank = 1
        )
    )
}