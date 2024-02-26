package pt.isel.pdm.gomokuroyale.game.matchmake.domain

import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfo

data class StartGameInfo(
    val gameId: Int,
    val localPlayer : UserInfo
)
