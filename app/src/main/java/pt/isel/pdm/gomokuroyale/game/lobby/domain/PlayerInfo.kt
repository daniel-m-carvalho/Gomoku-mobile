package pt.isel.pdm.gomokuroyale.game.lobby.domain

/**
 * Data class representing a player in the game.
 *
 * @property username The username of the player.
 * @property points The points accumulated by the player. Defaults to 0 if not specified.
 */
data class PlayerInfo(val username: String, val points: Int = 0)