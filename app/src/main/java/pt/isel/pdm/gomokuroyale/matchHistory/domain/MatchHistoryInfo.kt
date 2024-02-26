package pt.isel.pdm.gomokuroyale.matchHistory.domain

import androidx.compose.ui.graphics.Color
import pt.isel.pdm.gomokuroyale.game.play.domain.board.Piece

data class InfoParam(val name: String, val value: String)

data class MatchHistoryInfo(
    val result: Result,
    val variant: String,
    val opponent: String,
    val myPiece: Piece
)

enum class Result {
    Win,
    Loss,
}

fun Piece.toColor() = if (this == Piece.BLACK) Color.Black else Color.White

fun Color.toOther() = if (this == Color.Black) Color.White else Color.Black