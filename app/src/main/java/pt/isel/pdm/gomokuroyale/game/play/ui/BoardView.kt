package pt.isel.pdm.gomokuroyale.game.play.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.game.play.domain.board.Board
import pt.isel.pdm.gomokuroyale.game.play.domain.board.BoardDim
import pt.isel.pdm.gomokuroyale.game.play.domain.board.BoardRun
import pt.isel.pdm.gomokuroyale.game.play.domain.board.Cell
import pt.isel.pdm.gomokuroyale.game.play.domain.board.Piece
import pt.isel.pdm.gomokuroyale.ui.theme.AlabasterWhite
import pt.isel.pdm.gomokuroyale.ui.theme.Brown

val boarderSize = 5.dp

const val PIECE_TEST_TAG = "PIECE"
const val BOARD_TEST_TAG = "BOARD"

@Composable
fun BoardView(
    board: Board?,
    boardDim: Int,
    onPlayEnabled: Boolean = true,
    onClick: (Cell) -> Unit,
) {
    val maxWidth = LocalConfiguration.current.screenWidthDp.dp - boarderSize * 2
    Box(
        Modifier.border(width = boarderSize, color = AlabasterWhite)
    ) {
        Column(
            modifier = Modifier.testTag(BOARD_TEST_TAG),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            BorderConstructor(
                board = board,
                boardDim = boarDimWithBorder(size = boardDim),
                onClick = onClick,
                maxWidth = maxWidth,
                onPlayEnabled = onPlayEnabled
            )
        }
    }
}

@Composable
private fun BorderConstructor(
    board: Board?,
    boardDim: Int,
    onClick: (Cell) -> Unit,
    maxWidth: Dp,
    onPlayEnabled: Boolean
) {
    val roundedWidth = roundToNearestMultipleOf4(maxWidth.value.toInt())
    val cellSize: Dp = (roundedWidth / boardDim).dp
    val boardSize = cellSize * boardDim
    repeat(boardDim) { row ->
        Row(
            modifier = Modifier
                .width(boardSize)
                .background(Brown),
        ) {
            repeat(boardDim) { col ->
                val resourceId = getResourceId(row, col, boardDim)
                if (resourceId != 0) {
                    Border(resourceId, cellSize)
                } else {
                    val pos = Cell(row, col)
                    if (board == null) CellView(null, cellSize, onPlayEnabled)
                    else CellView(
                        board.moves[pos],
                        cellSize,
                        onPlayEnabled,
                    ) {
                        onClick(pos)
                    }
                }
            }
        }
    }
}

@Composable
fun CellView(
    player: Piece?,
    cellSize: Dp,
    onPlayEnabled: Boolean = true,
    modifier: Modifier = Modifier
        .size(cellSize)
        .background(Brown)
        .testTag(PIECE_TEST_TAG),
    onClick: () -> Unit = {}
) {
    if (player == null)
        Box(modifier = modifier
            .clip(CircleShape)
            .clickable(enabled = onPlayEnabled) { onClick() }
        ) {
            Image(painter = painterResource(id = R.drawable.cross), contentDescription = "Cross")
        }
    else
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            val image = when (player) {
                Piece.WHITE -> R.drawable.checkers_white
                Piece.BLACK -> R.drawable.checkers_black
            }
            AnimatedVisibility(
                visible = true,
                enter = scaleIn(initialScale = 0.1f) + expandVertically(expandFrom = Alignment.CenterVertically),
            ) {
                Image(
                    painter = painterResource(image),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "piece"
                )
            }
        }
}

@Composable
fun Border(resourceId: Int, cellSize: Dp) =
    Box(
        modifier = Modifier
            .size(cellSize)
            .background(Brown)
    ) {
        Image(painter = painterResource(id = resourceId), contentDescription = "border")
    }

private fun getResourceId(row: Int, col: Int, boardDim: Int): Int {
    val isTopRow = row == 0
    val isBottomRow = row == boardDim - 1
    val isLeftCol = col == 0
    val isRightCol = col == boardDim - 1

    return when {
        isTopRow && isLeftCol -> R.drawable.top_left_corner_border
        isTopRow && isRightCol -> R.drawable.top_right_corner_border
        isBottomRow && isLeftCol -> R.drawable.bottom_left_corner_border
        isBottomRow && isRightCol -> R.drawable.bottom_right_corner_border
        isTopRow -> R.drawable.top_border
        isBottomRow -> R.drawable.bottom_border
        isLeftCol -> R.drawable.left_border
        isRightCol -> R.drawable.right_border
        else -> 0
    }
}

private fun roundToNearestMultipleOf4(value: Int): Int {
    val remainder = value % 4
    return if (remainder < 2) {
        value - remainder
    } else {
        value + (4 - remainder)
    }
}

private fun boarDimWithBorder(size: Int) = size + 1

@Composable
@Preview
fun BoardViewPreview() {
    BoardView(
        BoardRun(emptyMap(), Piece.BLACK),
        BoardDim.STANDARD.toInt(),
        onClick = {})
}