package pt.isel.pdm.gomokuroyale.game.play.domain

import pt.isel.pdm.gomokuroyale.authentication.domain.User
import pt.isel.pdm.gomokuroyale.game.play.domain.board.Board
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant

data class Game(
    val id: Int,
    val userBlack: User,
    val userWhite: User,
    val board: Board,
    val state: String,
    val variant: Variant
) {
    val isOver: Boolean
        get() = state == "PLAYER_BLACK_WON" || state == "PLAYER_WHITE_WON" || state == "DRAW"

    val winner: User?
        get() = when (state) {
            GameState.PLAYER_BLACK_WON.toString() -> userBlack
            GameState.PLAYER_WHITE_WON.toString() -> userWhite
            else -> null
        }

    fun isMyTurn(username : String): Boolean = when (state) {
        GameState.NEXT_PLAYER_BLACK.toString() -> username == userBlack.username
        GameState.NEXT_PLAYER_WHITE.toString() -> username == userWhite.username
        else -> false
    }
}
