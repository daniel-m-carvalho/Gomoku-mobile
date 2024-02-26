package pt.isel.pdm.gomokuroyale.game.play.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.authentication.domain.Email
import pt.isel.pdm.gomokuroyale.authentication.domain.Id
import pt.isel.pdm.gomokuroyale.authentication.domain.User
import pt.isel.pdm.gomokuroyale.game.play.domain.Game
import pt.isel.pdm.gomokuroyale.game.play.domain.board.BoardRun
import pt.isel.pdm.gomokuroyale.game.play.domain.board.Cell
import pt.isel.pdm.gomokuroyale.game.play.domain.board.Piece
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import pt.isel.pdm.gomokuroyale.ui.theme.DarkViolet
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme
import pt.isel.pdm.gomokuroyale.ui.theme.Violet


const val GameScreenTestTag = "GameScreenTestTag"
const val GameExitButtonTestTag = "GameExitButtonTestTag"
const val GameHelpButtonTestTag = "GameHelpButtonTestTag"

@Composable
fun GameScreen(
    state: GameScreenState,
    onPlayRequested: (Cell) -> Unit = {},
    onForfeitRequested: () -> Unit = {},
    onHelpRequested: () -> Unit = {}
) {
    GomokuRoyaleTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Violet)
                .testTag(GameScreenTestTag),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            val (black, white) = state.getPlayersUsernames()
            IconComposable(
                iconResourceId = R.drawable.gomokulog,
                height = 50.dp,
            )
            RectangleComponent(black, white, state.isBlackTurn())
            BoardView(
                board = state.getGameBoard(),
                boardDim = state.getGameVariant().boardDim,
                onPlayEnabled = state is GameScreenState.MyTurn,
            ) { at -> onPlayRequested(at) }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ButtonComponent(
                    iconResourceId = R.drawable.ic_exit,
                    text = stringResource(id = R.string.match_exit),
                    onSelectEnabled = state is GameScreenState.MyTurn,
                    testTag = GameExitButtonTestTag,
                    onClick = onForfeitRequested
                )
                ButtonComponent(
                    iconResourceId = R.drawable.ic_robot,
                    text = stringResource(id = R.string.match_help),
                    testTag = GameHelpButtonTestTag,
                    onClick = onHelpRequested
                )
            }
        }
    }
}

@Composable
fun IconComposable(
    iconResourceId: Int,
    color: Color = Color.Unspecified,
    height: Dp,
    padding: Dp = 0.dp,
    rotation: Float = 0f
) {
    Icon(
        painter = painterResource(id = iconResourceId),
        contentDescription = "",
        modifier = Modifier
            .height(height)
            .padding(padding)
            .rotate(rotation),
        tint = color
    )
}

@Composable
fun ButtonComponent(
    iconResourceId: Int,
    onSelectEnabled: Boolean = true,
    text: String,
    testTag:String = "",
    onClick: () -> Unit
) {
    Button(
        modifier= Modifier.testTag(testTag),
        onClick = onClick,
        enabled = onSelectEnabled,
        colors = ButtonDefaults.buttonColors(DarkViolet)
    ) {
        IconComposable(iconResourceId = iconResourceId, height = 24.dp, padding = 4.dp)
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = text,
            textAlign = TextAlign.Start,
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
        )
    }
}

@Composable
fun RectangleComponent(
    black: String,
    white: String,
    isBlackTurn: Boolean = true
) {
    val rotation by animateFloatAsState(
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.07f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight()
                .background(DarkViolet),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconComposable(
                iconResourceId = R.drawable.icon_player1_m,
                height = 70.dp,
                padding = 4.dp,
                rotation = if (isBlackTurn) rotation else 0f
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(white, fontSize = 16.sp, color = Color.White)
                IconComposable(
                    iconResourceId = R.drawable.checkers_white,
                    height = 30.dp,
                    padding = 2.dp,
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkViolet),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(black, fontSize = 16.sp, color = Color.Black)
                IconComposable(
                    iconResourceId = R.drawable.checkers_black,
                    height = 30.dp,
                    padding = 2.dp
                )
            }
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            IconComposable(
                iconResourceId = R.drawable.icon_player2_f,
                height = 70.dp,
                padding = 4.dp,
                rotation = if (!isBlackTurn) rotation else 0f
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun GamePlayScreenLoadingPreview() {
    GameScreen(GameScreenState.Loading)
}

@Preview(showBackground = true)
@Composable
private fun GamePlayScreenMyTurnPreview() {
    GameScreen(
        GameScreenState.Playing(
            Game(
                id = 1,
                variant = variant,
                board = BoardRun(
                    moves = mapOf(
                        Cell(0, 0) to Piece.BLACK,
                        Cell(0, 1) to Piece.WHITE,
                        Cell(0, 8) to Piece.BLACK,
                        Cell(0, 9) to Piece.WHITE,
                        Cell(1, 7) to Piece.WHITE,
                        Cell(1, 8) to Piece.BLACK,
                        Cell(1, 9) to Piece.WHITE,
                        Cell(2, 0) to Piece.BLACK,
                        Cell(2, 1) to Piece.WHITE,
                        Cell(2, 2) to Piece.BLACK,
                        Cell(2, 3) to Piece.WHITE,
                        Cell(3, 2) to Piece.BLACK,
                        Cell(3, 3) to Piece.WHITE,
                        Cell(3, 4) to Piece.BLACK,
                        Cell(3, 5) to Piece.WHITE,
                        Cell(3, 6) to Piece.BLACK,
                        Cell(3, 7) to Piece.WHITE
                    ),
                    turn = Piece.BLACK,
                ),
                userBlack = mockUser1,
                userWhite = mockUser2,
                state = "PLAYER_BLACK_TURN",
            )
        )
    )
}


val mockUser1 = User(
    id = Id(1),
    username = "Ackermann",
    email = Email("mock1@example.com"),
)

val mockUser2 = User(
    id = Id(2),
    username = "BakerBoy",
    email = Email("mock2@example.com"),
)

val variant = Variant(
    name = "Standard",
    boardDim = 15,
    playRule = "Standard",
    openingRule = "Standard",
    points = 5
)

