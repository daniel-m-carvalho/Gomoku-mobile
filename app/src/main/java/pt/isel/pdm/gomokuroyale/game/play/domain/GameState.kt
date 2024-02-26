package pt.isel.pdm.gomokuroyale.game.play.domain

enum class GameState {
    SWAPPING_PIECES,
    NEXT_PLAYER_BLACK,
    NEXT_PLAYER_WHITE,
    PLAYER_BLACK_WON,
    PLAYER_WHITE_WON,
    DRAW;
}