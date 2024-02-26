package pt.isel.pdm.gomokuroyale.game.play.domain.board

enum class Piece {
    BLACK, WHITE;
    fun other() = if (this == BLACK) WHITE else BLACK
}