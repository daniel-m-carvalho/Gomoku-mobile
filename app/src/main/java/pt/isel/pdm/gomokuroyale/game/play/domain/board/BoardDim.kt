package pt.isel.pdm.gomokuroyale.game.play.domain.board

enum class BoardDim {
    STANDARD,
    MODIFIED;
    fun toInt(): Int = when (this) {
        STANDARD -> 15
        MODIFIED -> 19
    }
}