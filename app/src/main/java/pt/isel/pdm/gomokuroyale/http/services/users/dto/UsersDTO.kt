package pt.isel.pdm.gomokuroyale.http.services.users.dto

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import pt.isel.pdm.gomokuroyale.http.domain.users.RankingEntry
import java.lang.reflect.Type

// region Input Models

data class UserCreateInputModel(
    val username: String,
    val email: String,
    val password: String
)

data class UserCreateTokenInputModel(
    val username: String,
    val password: String
)

// endregion

// region Output Models

data class UserCreateOutputModel(
    val uid: Int
)

data class UserGetByIdOutputModel(
    val id: Int,
    val username: String,
    val email: String
)

data class UserStatsOutputModel(
    val uid: Int,
    val username: String,
    val gamesPlayed: Int,
    val wins: Int,
    val losses: Int,
    val draws: Int,
    val rank: Int,
    val points: Int
) {

//    private class UserStatsDeserializer : JsonDeserializer<UserStatsOutputModel> {
//        override fun deserialize(
//            json: JsonElement?,
//            typeOfT: Type?,
//            context: JsonDeserializationContext?
//        ): UserStatsOutputModel {
//            val jsonObject = json?.asJsonObject ?: throw JsonParseException("Invalid JSON")
//            val uid = jsonObject["uid"].asInt
//            val username = jsonObject["username"].asString
//            val gamesPlayed = jsonObject["gamesPlayed"].asInt
//            val wins = jsonObject["wins"].asInt
//            val losses = jsonObject["losses"].asInt
//            val draws = gamesPlayed - wins - losses
//            val rank = jsonObject["rank"].asInt
//            val points = jsonObject["points"].asInt
//            return UserStatsOutputModel(
//                uid,
//                username,
//                gamesPlayed,
//                wins,
//                losses,
//                draws,
//                rank,
//                points
//            )
//        }
//    }
//
//    companion object {
//        fun getCustomGson(): Gson {
//            return GsonBuilder()
//                .registerTypeAdapter(UserStatsOutputModel::class.java, UserStatsDeserializer())
//                .create()
//        }
//    }
}

data class UserHomeOutputModel(
    val id: Int,
    val username: String
)

data class UserTokenCreateOutputModel(
    val token: String
)

data class RankingInfoOutputModel(
    val rankingTable: List<RankingEntry>
)

data class UserTokenRemoveOutputModel(
    val message: String
)

// endregion