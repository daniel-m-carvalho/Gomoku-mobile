package pt.isel.pdm.gomokuroyale.matchHistory.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.game.play.domain.board.Piece
import pt.isel.pdm.gomokuroyale.matchHistory.domain.MatchHistoryInfo
import pt.isel.pdm.gomokuroyale.matchHistory.domain.toColor
import pt.isel.pdm.gomokuroyale.matchHistory.domain.toOther
import pt.isel.pdm.gomokuroyale.ui.NavigationHandlers
import pt.isel.pdm.gomokuroyale.ui.TopBar
import pt.isel.pdm.gomokuroyale.ui.components.MyIcon
import pt.isel.pdm.gomokuroyale.ui.theme.DarkViolet
import pt.isel.pdm.gomokuroyale.matchHistory.domain.Result
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme

const val MatchHistoryScreenTestTag = "MatchHistoryScreen"
const val MatchHistoryListTestTag = "MatchHistoryHeader"

@Composable
fun MatchHistoryScreen(
    modifier: Modifier = Modifier,
    onBackRequested: () -> Unit = {},
    username: String,
    matches: List<MatchHistoryInfo>
) {
    GomokuRoyaleTheme {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .testTag(MatchHistoryScreenTestTag),
            topBar = {
                TopBar(
                    title = {
                        Spacer(modifier = Modifier.padding(20.dp))
                        Text(
                            text = stringResource(id = R.string.match_history_title),
                            textAlign = TextAlign.Center
                        )
                    },
                    navigation = NavigationHandlers(onBackRequested = onBackRequested)
                )
            },
        ) { innerPadding ->
            MatchHistoryContent(
                modifier = Modifier.padding(innerPadding),
                username = username,
                matches = matches
            )
        }
    }
}

@Composable
private fun MatchHistoryContent(modifier: Modifier, username: String, matches: List<MatchHistoryInfo>) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkViolet),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MatchHistoryHeader(username = username)
        MatchHistoryBody(matches)
    }
}

@Composable
private fun MatchHistoryBody(matches: List<MatchHistoryInfo>) {
    LazyColumn(
        userScrollEnabled = true,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 35.dp)
            .testTag(MatchHistoryListTestTag)
    ) {
        items(matches.size) {
            val color = matches[it].myPiece.toColor()
            Card(
                shape = RectangleShape,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(10.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = color,
                    contentColor = color.toOther()
                )
            ) {
                MatchView(matches[it])
            }
            Spacer(modifier = Modifier.padding(5.dp))
        }
    }
}

@Composable
private fun MatchHistoryHeader(username: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.padding(15.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            UsersProfilePic()
            Spacer(modifier = Modifier.padding(35.dp))
            UsersProfileName(username = username)
        }
        Spacer(modifier = Modifier.padding(5.dp))
        Canvas(
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .drawBehind {
                    drawLine(
                        color = Color.Black,
                        start = Offset(100f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 5.dp.toPx()
                    )
                },
        ) {}
        Spacer(modifier = Modifier.padding(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Matches", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun UsersProfilePic() {
    Column(
        modifier = Modifier.height(75.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.chapeleiro_louco),
            contentDescription = "Profile_picture",
        )
    }
}

@Composable
private fun UsersProfileName(username: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = username,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Monospace,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
private fun MatchView(match: MatchHistoryInfo) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        when (match.result) {
            Result.Win -> {
                if (match.myPiece == Piece.BLACK) Match(resultId = R.drawable.ic_win_white, match)
                else Match(resultId = R.drawable.ic_win_black, match)
            }

            else -> {
                if (match.myPiece == Piece.WHITE) Match(resultId = R.drawable.ic_loss_black, match)
                else Match(resultId = R.drawable.ic_loss_white, match)
            }
        }
    }
}

@Composable
private fun Match(resultId: Int, match: MatchHistoryInfo) {
    MyIcon(resultId = resultId)
    Text(
        text = "${match.variant} | You vs ${match.opponent}",
        style = MaterialTheme.typography.titleSmall,
        textAlign = TextAlign.Center,
        maxLines = 1,
    )
}

@Preview
@Composable
fun MatchesPreview() {
    MatchHistoryScreen(
        onBackRequested = {},
        username = "Username",
        matches = matchesList
    )
}

val matchesList = buildList {
    repeat(15) {
        if (it % 2 == 0)
            if (it == 4)
                add(MatchHistoryInfo(Result.Win, "STANDARD", "Opponent", Piece.WHITE))
            else
                add(MatchHistoryInfo(Result.Win, "STANDARD", "Opponent", Piece.BLACK))
        else {
            if (it == 1)
                add(MatchHistoryInfo(Result.Loss, "STANDARD", "Opponent", Piece.BLACK))
            else
                add(MatchHistoryInfo(Result.Loss, "STANDARD", "Opponent", Piece.WHITE))
        }

    }
}

@Preview
@Composable
fun MatchPreview() {
    MatchView(MatchHistoryInfo(Result.Win, "STANDARD", "Opponent", Piece.BLACK))
}