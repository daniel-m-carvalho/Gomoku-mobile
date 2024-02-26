package pt.isel.pdm.gomokuroyale.http

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.gomokuroyale.http.domain.UriRepository
import pt.isel.pdm.gomokuroyale.http.media.siren.LinkModel
import pt.isel.pdm.gomokuroyale.http.media.siren.SirenModel
import pt.isel.pdm.gomokuroyale.http.services.HTTPService
import pt.isel.pdm.gomokuroyale.http.services.games.GameService
import pt.isel.pdm.gomokuroyale.http.services.users.UserService
import pt.isel.pdm.gomokuroyale.util.ApiError
import pt.isel.pdm.gomokuroyale.util.HttpResult
import pt.isel.pdm.gomokuroyale.util.onFailure
import pt.isel.pdm.gomokuroyale.util.onSuccess

data class HomeOutputModel(val message: String)
typealias HomeOutput = SirenModel<HomeOutputModel>

class GomokuService(
    client: OkHttpClient,
    gson: Gson,
    apiEndpoint: String,
    uriRepository: UriRepository
) : HTTPService(client, gson, apiEndpoint, uriRepository) {

    val userService = UserService(client, gson, apiEndpoint, uriRepository)

    val gameService = GameService(client, gson, apiEndpoint, uriRepository)

    suspend fun getHome(): HttpResult<List<LinkModel>> {
        val response = get<HomeOutputModel>(HOME_PATH)
        return response.onSuccess {
            HttpResult.Success(it.recipeLinks)
        }.onFailure {
            val message = it.message ?: "Unknown error"
            HttpResult.Failure(ApiError(message))
        }
    }

    companion object {
        private const val HOME_PATH = "/api/"
    }
}