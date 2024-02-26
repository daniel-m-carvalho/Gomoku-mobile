package pt.isel.pdm.gomokuroyale.http.domain.users

data class RankingEntry (
    val page : Int,
    val pageSize : Int
)

fun Int.unitsConverter(): String {
    val points = this
    return when {
        points < 1000 -> points.toString()
        points < 1000000 -> String.format("%.1fK", points / 1000.0)
        else -> String.format("%.1fM", points / 1000000.0)
    }
}