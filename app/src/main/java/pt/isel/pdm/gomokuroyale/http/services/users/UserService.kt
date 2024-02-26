package pt.isel.pdm.gomokuroyale.http.services.users

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.gomokuroyale.authentication.domain.Email
import pt.isel.pdm.gomokuroyale.authentication.domain.Id
import pt.isel.pdm.gomokuroyale.authentication.domain.User
import pt.isel.pdm.gomokuroyale.http.domain.PaginationLinks
import pt.isel.pdm.gomokuroyale.http.domain.UriRepository
import pt.isel.pdm.gomokuroyale.http.domain.users.RankingList
import pt.isel.pdm.gomokuroyale.http.domain.users.UserHome
import pt.isel.pdm.gomokuroyale.http.domain.users.UserId
import pt.isel.pdm.gomokuroyale.http.domain.users.UserRanking
import pt.isel.pdm.gomokuroyale.http.domain.users.UserToken
import pt.isel.pdm.gomokuroyale.http.services.HTTPService
import pt.isel.pdm.gomokuroyale.http.services.users.dto.RankingInfoOutputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserCreateInputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserCreateOutputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserCreateTokenInputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserGetByIdOutputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserHomeOutputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserStatsOutputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserTokenCreateOutputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserTokenRemoveOutputModel
import pt.isel.pdm.gomokuroyale.http.utils.Rels
import pt.isel.pdm.gomokuroyale.util.ApiError
import pt.isel.pdm.gomokuroyale.util.HttpResult
import pt.isel.pdm.gomokuroyale.util.onFailure
import pt.isel.pdm.gomokuroyale.util.onSuccess

private const val UserServicesTag = "USER_SERVICE_TAG"

class UserService(
    client: OkHttpClient,
    gson: Gson,
    apiEndpoint: String,
    uriRepository: UriRepository
) : HTTPService(client, gson, apiEndpoint, uriRepository) {

    suspend fun register(
        username: String,
        email: String,
        password: String
    ): HttpResult<UserId> {
        val path = uriRepository.getRecipeLink(Rels.REGISTER) ?: return HttpResult.Failure(
            ApiError("Register link not found")
        )
        val response = post<UserCreateOutputModel>(
            path = path.href,
            body = UserCreateInputModel(username, email, password)
        )
        return response.onSuccess {
            HttpResult.Success(UserId(it.properties.uid))
        }.onFailure {
            val message = it.message.errorMessage
            HttpResult.Failure(ApiError(message))
        }
    }

    suspend fun login(username: String, password: String): HttpResult<UserToken> {
        val path = uriRepository.getRecipeLink(Rels.LOGIN) ?: return HttpResult.Failure(
            ApiError("Login link not found")
        )
        val response = post<UserTokenCreateOutputModel>(
            path = path.href,
            body = UserCreateTokenInputModel(username, password)
        )
        return response.onSuccess {
            HttpResult.Success(UserToken(it.properties.token))
        }.onFailure {
            val message = it.message.errorMessage
            HttpResult.Failure(ApiError(message))
        }
    }

    suspend fun logout(token: String): HttpResult<Unit> {
        val path = uriRepository.getRecipeLink(Rels.LOGOUT) ?: return HttpResult.Failure(
            ApiError("Logout link not found")
        )
        val response = post<UserTokenRemoveOutputModel>(path = path.href, token = token)
        return response.onSuccess {
            HttpResult.Success(Unit)
        }.onFailure {
            val message = it.message.errorMessage
            HttpResult.Failure(ApiError(message))
        }
    }

    suspend fun getUser(id: Int): HttpResult<User> {
        val path = uriRepository.getRecipeLink(Rels.USER) ?: return HttpResult.Failure(
            ApiError("User link not found")
        )
        val response = get<UserGetByIdOutputModel>(
            path = path.href.replace("{uid}", id.toString())
        )
        return response.onSuccess {
            HttpResult.Success(
                User(
                    id = Id(it.properties.id),
                    username = it.properties.username,
                    email = Email(it.properties.email),
                )
            )
        }.onFailure {
            val message = it.message.errorMessage
            HttpResult.Failure(ApiError(message))
        }
    }

    suspend fun getAuthHome(token: String): HttpResult<UserHome> {
        val path = uriRepository.getRecipeLink(Rels.AUTH_HOME) ?: return HttpResult.Failure(
            ApiError("User home link not found")
        )
        val response = get<UserHomeOutputModel>(
            path = path.href,
            token = token
        )
        return response.onSuccess {
            HttpResult.Success(
                UserHome(
                    id = it.properties.id,
                    username = it.properties.username,
                )
            )
        }.onFailure {
            val message = it.message.errorMessage
            HttpResult.Failure(ApiError(message))
        }
    }

    suspend fun getStatsById(id: Int, token: String): HttpResult<UserRanking> {
        val path = uriRepository.getRecipeLink(Rels.USER_STATS) ?: return HttpResult.Failure(
            ApiError("User stats link not found")
        )
        val response = get<UserStatsOutputModel>(
            path = path.href.replace("{uid}", id.toString()),
            token = token
        )
        return response.onSuccess {
            HttpResult.Success(
                UserRanking(
                    id = it.properties.uid,
                    username = it.properties.username,
                    gamesPlayed = it.properties.gamesPlayed,
                    wins = it.properties.wins,
                    losses = it.properties.losses,
                    draws = it.properties.draws,
                    rank = it.properties.rank,
                    points = it.properties.points
                )
            )
        }.onFailure {
            val message = it.message.errorMessage
            HttpResult.Failure(ApiError(message))
        }
    }

    suspend fun getStatsByUsername(username: String, token: String): HttpResult<UserRanking> {
        val path = uriRepository.getRecipeLink(Rels.USER_STATS_BY_USERNAME) ?: return HttpResult.Failure(
            ApiError("User stats link not found")
        )
        val response = get<UserStatsOutputModel>(
            path = path.href.replace("{name}", username),
            token = token
        )
        return response.onSuccess {
            HttpResult.Success(
                UserRanking(
                    id = it.properties.uid,
                    username = it.properties.username,
                    gamesPlayed = it.properties.gamesPlayed,
                    wins = it.properties.wins,
                    losses = it.properties.losses,
                    draws = it.properties.draws,
                    rank = it.properties.rank,
                    points = it.properties.points
                )
            )
        }.onFailure {
            val message = it.message.errorMessage
            HttpResult.Failure(ApiError(message))
        }
    }

    suspend fun getRankingInfo(page: Int): HttpResult<RankingList> {
        val path = uriRepository.getRecipeLink(Rels.RANKING_INFO) ?: return HttpResult.Failure(
            ApiError("Ranking link not found")
        )
        val response = get<RankingInfoOutputModel>(
            path = path.href.replace("1", page.toString())
        )
        return response.onSuccess { players ->
            val paginationLinks = players.links.associate { it.rel[0] to it.href }
            HttpResult.Success(
                RankingList(
                    players.entities.map { entity ->
                        val property = gson.fromJson(
                            entity.properties.toString(),
                            UserStatsOutputModel::class.java
                        )
                        UserRanking(
                            id = property.uid,
                            username = property.username,
                            gamesPlayed = property.gamesPlayed,
                            wins = property.wins,
                            losses = property.losses,
                            draws = property.draws,
                            rank = property.rank,
                            points = property.points
                        )
                    },
                    PaginationLinks.from(paginationLinks)
                )
            )
        }.onFailure {
            val message = it.message.errorMessage
            HttpResult.Failure(ApiError(message))
        }
    }

    suspend fun getRankingInfoByUsername(username: String, page: Int): HttpResult<RankingList> {
        val path = uriRepository.getRecipeLink(Rels.GET_STATS_BY_USERNAME_FOR_RANKING)
            ?: return HttpResult.Failure(
                ApiError("Ranking link not found")
            )
        val response = get<RankingInfoOutputModel>(
            path = path.href
                .replace("1", page.toString())
                .replace("{name}", username)
        )
        return response.onSuccess { players ->
            val paginationLinks = players.links.associate { it.rel[0] to it.href }
            HttpResult.Success(
                RankingList(
                    players.entities.map { entity ->
                        val property = gson.fromJson(
                            entity.properties.toString(),
                            UserStatsOutputModel::class.java
                        )
                        UserRanking(
                            id = property.uid,
                            username = property.username,
                            gamesPlayed = property.gamesPlayed,
                            wins = property.wins,
                            losses = property.losses,
                            draws = property.draws,
                            rank = property.rank,
                            points = property.points
                        )
                    },
                    PaginationLinks.from(paginationLinks)
                )
            )
        }.onFailure {
            val message = it.message.errorMessage
            HttpResult.Failure(ApiError(message))
        }
    }

    private val String?.errorMessage get() = this ?: unknownError
    private val unknownError get() = "Unknown error"
}
