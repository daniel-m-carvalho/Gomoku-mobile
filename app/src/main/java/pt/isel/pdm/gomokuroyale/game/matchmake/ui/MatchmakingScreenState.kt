package pt.isel.pdm.gomokuroyale.game.matchmake.ui

/**
 * This is a sealed interface that represents the different states of the matchmaking screen.
 * Each state is represented by a data class or object that extends this interface.
 */
sealed interface MatchmakingScreenState {

    /**
     * Represents the idle state of the matchmaking screen.
     */
    data object Idle : MatchmakingScreenState

    /**
     * Represents the queueing state of the matchmaking screen.
     */
    data object Queueing : MatchmakingScreenState

    /**
     * Represents the state of the matchmaking screen when looking for a match.
     * @property matchId The ID of the match.
     */
    data class LookingForMatch(val matchId: Int) : MatchmakingScreenState

    /**
     * Represents the state of the matchmaking screen when a match has been found.
     * @property gameId The ID of the game.
     */
    data class Matched(val gameId: Int) : MatchmakingScreenState

    /**
     * Represents the state of the matchmaking screen when the user has left the queue.
     */
    data object LeftQueue : MatchmakingScreenState

    /**
     * Represents the state of the matchmaking screen when an error has occurred.
     * @property error The error that occurred.
     */
    data class Error(val error: Throwable) : MatchmakingScreenState
}