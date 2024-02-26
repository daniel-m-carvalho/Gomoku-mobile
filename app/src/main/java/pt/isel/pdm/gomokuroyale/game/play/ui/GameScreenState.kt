package pt.isel.pdm.gomokuroyale.game.play.ui

import pt.isel.pdm.gomokuroyale.authentication.domain.User
import pt.isel.pdm.gomokuroyale.game.play.domain.Game
import pt.isel.pdm.gomokuroyale.game.play.domain.GameState
import pt.isel.pdm.gomokuroyale.game.play.domain.board.Board
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant

/**
 * This is a sealed interface that represents the different states of the game screen.
 * Each state is represented by a data class that extends this interface.
 */
sealed interface GameScreenState {

    /**
     * Represents the loading state of the game screen.
     */
    data object Loading : GameScreenState

    /**
     * Represents the state of the game screen when waiting for the opponent to make a move.
     * @property game The current game.
     */
    data class WaitingForOpponent(val game: Game) : GameScreenState

    /**
     * Represents the state of the game screen when it's the user's turn to make a move.
     * This is a sealed class that is extended by other states.
     * @property game The current game.
     */
    sealed class MyTurn(open val game: Game) : GameScreenState

    /**
     * Represents the state of the game screen when the user is playing.
     * @property game The current game.
     */
    data class Playing(override val game: Game) : MyTurn(game)

    /**
     * Represents the state of the game screen when the user has made a bad move.
     * @property error The error that occurred.
     * @property game The current game.
     */
    data class BadMove(val error: Throwable, override val game: Game) : MyTurn(game)

    /**
     * Represents the state of the game screen when the game is over.
     * @property game The finished game.
     * @property points The points scored in the game.
     * @property winner The winner of the game.
     */
    data class GameOver(val game: Game, val points: Int, val winner: User?) : GameScreenState

    /**
     * Represents the state of the game screen when an error has occurred.
     * @property error The error that occurred.
     */
    data class Error(val error: Throwable) : GameScreenState

    /**
     * Represents the state of the game screen when the user has forfeited the game.
     */
    data object Forfeit : GameScreenState

    /**
     * Gets the game board. If the game is not ongoing, returns an empty board.
     */
    fun getGameBoard(): Board = when (this) {
        is MyTurn -> game.board
        is WaitingForOpponent -> game.board
        is GameOver -> game.board
        else -> Board.EMPTY
    }

    /**
     * Gets the game variant. If the game is not ongoing, returns a standard variant.
     */
    fun getGameVariant(): Variant = when (this) {
        is MyTurn -> game.variant
        is WaitingForOpponent -> game.variant
        is GameOver -> game.variant
        else -> Variant(
            "STANDARD",
            15,
            "STANDARD",
            "STANDARD",
            15,
        )
    }

    /**
     * Gets the usernames of the players. If the game is not ongoing, returns "Black" and "White".
     */
    data class Usernames(val black: String, val white: String)
    fun getPlayersUsernames(): Usernames = when (this) {
        is MyTurn -> Usernames(game.userBlack.username, game.userWhite.username)
        is WaitingForOpponent -> Usernames(game.userBlack.username, game.userWhite.username)
        is GameOver -> Usernames(game.userBlack.username, game.userWhite.username)
        else -> Usernames("Black", "White")
    }

    /**
     * Checks if it's player black's turn.
     */
    fun isBlackTurn(): Boolean = when (this) {
        is MyTurn -> game.state == GameState.NEXT_PLAYER_WHITE.name
        is WaitingForOpponent -> game.state == GameState.NEXT_PLAYER_WHITE.name
        else -> false
    }
}