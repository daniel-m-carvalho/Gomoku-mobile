package pt.isel.pdm.gomokuroyale.game.lobby.domain

sealed class Variants(val value: String) {
    data object Standard : Variants("STANDARD")
    data object Omok : Variants("OMOK")
    data object Renju : Variants("RENJU")
    data object Pente : Variants("PENTE")
    data object Caro : Variants("CARO")
    data object NinukiRenju : Variants("NINUKI_RENJU")
    data object Swap : Variants("SWAP")
}
