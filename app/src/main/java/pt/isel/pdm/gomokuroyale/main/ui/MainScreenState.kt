package pt.isel.pdm.gomokuroyale.main.ui

import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfo
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import pt.isel.pdm.gomokuroyale.http.domain.Recipe

sealed interface MainScreenState {
    data object Idle : MainScreenState
    data object FetchingRecipes : MainScreenState
    data class FetchedRecipes(val recipes: Result<List<Recipe>>) : MainScreenState
    data object FetchingPlayerInfo : MainScreenState
    data class FetchedPlayerInfo(val userInfo: Result<UserInfo?>, val isLoggedIn : Boolean) :
        MainScreenState
    data object FetchingVariants : MainScreenState
    data class FetchedVariants(val variants: Result<List<Variant>>?) : MainScreenState
    data class  FailedToFetch(val error: Throwable) : MainScreenState
    data object LoggingOut : MainScreenState
    data class LoggedOut(val result: Result<Unit>) : MainScreenState
    data class FailedToLogout(val error: Throwable) : MainScreenState
}