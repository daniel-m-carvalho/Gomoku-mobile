package pt.isel.pdm.gomokuroyale

import com.google.gson.Gson
import kotlinx.datetime.Clock
import okhttp3.OkHttpClient
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfoRepository
import pt.isel.pdm.gomokuroyale.game.lobby.domain.VariantRepository
import pt.isel.pdm.gomokuroyale.http.GomokuService
import pt.isel.pdm.gomokuroyale.http.domain.UriRepository

/**
 * The contract to be supported by the application's class used to resolve dependencies.
 */
interface DependenciesContainer {
    /**
     * The JSON serializer/deserializer used to convert JSON into DTOs
     */
    val gson: Gson

    /**
     * The HTTP client used to perform HTTP requests
     */
    val client: OkHttpClient

    /**
     * The repository used to access the user's information
     */
    val userInfoRepository: UserInfoRepository

    /**
     * The service used to access the Gomoku Royale API
     */
    val gomokuService: GomokuService

    /**
     * The repository used to access API URI's templates
     */
    val uriRepository: UriRepository

    /**
     * The repository used to access the game variants
     */
    val variantRepository: VariantRepository

    /**
     * The clock used to access the current time
     */
    val clock: Clock
}