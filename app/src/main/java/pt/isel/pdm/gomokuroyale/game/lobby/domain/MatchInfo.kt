package pt.isel.pdm.gomokuroyale.game.lobby.domain

import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfo
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variants

data class MatchInfo(
    val userInfo: UserInfo,
    val variant: Variant
)
