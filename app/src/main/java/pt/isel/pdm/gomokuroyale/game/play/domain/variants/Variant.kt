package pt.isel.pdm.gomokuroyale.game.play.domain.variants

data class Variant(
    val name : String,
    val boardDim : Int,
    val playRule : String,
    val openingRule : String,
    val points : Int
)
