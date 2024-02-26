package pt.isel.pdm.gomokuroyale.http.services.games

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.gomokuroyale.game.play.domain.Game
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import pt.isel.pdm.gomokuroyale.http.domain.UriRepository
import pt.isel.pdm.gomokuroyale.http.domain.games.MatchmakerStatus
import pt.isel.pdm.gomokuroyale.http.domain.games.QueueEntry
import pt.isel.pdm.gomokuroyale.http.services.HTTPService
import pt.isel.pdm.gomokuroyale.http.services.games.dto.CancelMatchmakingOutputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameGetAllByUserOutputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameGetByIdOutputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameMatchmakingInputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameMatchmakingOutputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameMatchmakingStatusOutputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameOutputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GamePlayInputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameRoundOutputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GetVariantsOutputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.SurrenderGameOutputModel
import pt.isel.pdm.gomokuroyale.http.utils.Rels
import pt.isel.pdm.gomokuroyale.util.ApiError
import pt.isel.pdm.gomokuroyale.util.HttpResult
import pt.isel.pdm.gomokuroyale.util.onFailure
import pt.isel.pdm.gomokuroyale.util.onSuccess

class GameService(
    client: OkHttpClient,
    gson: Gson,
    apiEndpoint: String,
    uriRepository: UriRepository
) : HTTPService(client, gson, apiEndpoint, uriRepository) {

    suspend fun getGame(gameId: Int, token: String): HttpResult<Game> {
        val path = uriRepository.getRecipeLink(Rels.GAME) ?: return HttpResult.Failure(
            ApiError("Game link not found")
        )
        val response = get<GameGetByIdOutputModel>(
            path = path.href.replace("{gid}", gameId.toString()),
            token = token,
        )
        return response.onSuccess {
            HttpResult.Success(
                Game(
                    id = it.properties.game.id,
                    userBlack = it.properties.game.userBlack,
                    userWhite = it.properties.game.userWhite,
                    board = it.properties.game.board,
                    state = it.properties.game.state,
                    variant = it.properties.game.variant
                )
            )
        }.onFailure {
            val message = it.message.errorMessage
            HttpResult.Failure(ApiError(message))
        }
    }

    suspend fun play(
        gameId: Int,
        token: String,
        play: GamePlayInputModel
    ): HttpResult<Game> {
        val path = uriRepository.getRecipeLink(Rels.PLAY) ?: return HttpResult.Failure(
            ApiError("Game link not found")
        )
        val response = post<GameRoundOutputModel>(
            path = path.href.replace("{gid}", gameId.toString()),
            token = token,
            body = play
        )
        return response.onSuccess {
            HttpResult.Success(
                Game(
                    id = it.properties.game.id,
                    userBlack = it.properties.game.userBlack,
                    userWhite = it.properties.game.userWhite,
                    board = it.properties.game.board,
                    state = it.properties.game.state,
                    variant = it.properties.game.variant
                )
            )
        }.onFailure {
            val message = it.message.errorMessage
            HttpResult.Failure(ApiError(message))
        }
    }

    suspend fun surrender(gameId: Int, token: String): HttpResult<Unit> {
        val path = uriRepository.getRecipeLink(Rels.LEAVE) ?: return HttpResult.Failure(
            ApiError("Game link not found")
        )
        val response = put<SurrenderGameOutputModel>(
            path = path.href.replace("{gid}", gameId.toString()),
            token = token,
        )
        return response.onSuccess {
            HttpResult.Success(Unit)
        }.onFailure {
            val message = it.message.errorMessage
            HttpResult.Failure(ApiError(message))
        }
    }

    suspend fun getUserGames(token: String, userId: Int, page: Int): HttpResult<List<Game>> {
        val path =
            uriRepository.getRecipeLink(Rels.GET_ALL_GAMES_BY_USER) ?: return HttpResult.Failure(
                ApiError("Game link not found")
            )
        val response = get<GameGetAllByUserOutputModel>(
            path = path.href
                .replace("1", page.toString())
                .replace("{uid}", userId.toString()),
            token = token,
        )
        return response.onSuccess {
            HttpResult.Success(
                it.entities.map { entity ->
                    val game = gson.fromJson(
                        entity.properties.toString(), GameOutputModel::class.java
                    )
                    Game(
                        id = game.id,
                        userBlack = game.userBlack,
                        userWhite = game.userWhite,
                        board = game.board,
                        state = game.state,
                        variant = game.variant
                    )
                }
            )
        }.onFailure {
            val message = it.message.errorMessage
            HttpResult.Failure(ApiError(message))
        }
    }

    suspend fun getVariants(): HttpResult<List<Variant>> {
        val path = uriRepository.getRecipeLink(Rels.GET_ALL_VARIANTS) ?: return HttpResult.Failure(
            ApiError("Variants link not found")
        )
        val response = get<GetVariantsOutputModel>(
            path = path.href,
        )
        return response.onSuccess {
            HttpResult.Success(
                it.properties.variants
            )
        }.onFailure {
            val message = it.message.errorMessage
            HttpResult.Failure(ApiError(message))
        }
    }


    suspend fun matchmaking(
        token: String,
        input: GameMatchmakingInputModel
    ): HttpResult<QueueEntry> {
        val path = uriRepository.getRecipeLink(Rels.MATCHMAKING) ?: return HttpResult.Failure(
            ApiError("Matchmaking link not found")
        )
        val response = post<GameMatchmakingOutputModel>(
            path = path.href,
            token = token,
            body = input
        )
        return response.onSuccess {
            HttpResult.Success(
                QueueEntry(
                    id = it.properties.id,
                    idType = it.properties.idType,
                    message = it.properties.message
                )
            )
        }.onFailure {
            val message = it.message.errorMessage
            HttpResult.Failure(ApiError(message))
        }
    }

    suspend fun cancelMatchmaking(token: String, id: Int): HttpResult<Unit> {
        val path =
            uriRepository.getRecipeLink(Rels.EXIT_MATCHMAKING_QUEUE) ?: return HttpResult.Failure(
                ApiError("Matchmaking link not found")
            )
        val response = delete<CancelMatchmakingOutputModel>(
            path = path.href.replace("{mid}", id.toString()),
            token = token,
        )
        return response.onSuccess {
            HttpResult.Success(Unit)
        }.onFailure {
            val message = it.message.errorMessage
            HttpResult.Failure(ApiError(message))
        }
    }

    suspend fun getMatchmakingStatus(token: String, id: Int): HttpResult<MatchmakerStatus> {
        val path =
            uriRepository.getRecipeLink(Rels.MATCHMAKING_STATUS) ?: return HttpResult.Failure(
                ApiError("Matchmaking link not found")
            )
        val response = get<GameMatchmakingStatusOutputModel>(
            path = path.href.replace("{mid}", id.toString()),
            token = token,
        )
        return response.onSuccess {
            HttpResult.Success(
                MatchmakerStatus(
                    mid = it.properties.mid,
                    uid = it.properties.uid,
                    gid = it.properties.gid,
                    state = it.properties.state,
                    variant = it.properties.variant,
                    created = it.properties.created,
                    pollingTimeOut = it.properties.pollingTimeOut
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


