package pt.isel.pdm.gomokuroyale.util.deserializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserStatsOutputModel
import java.lang.reflect.Type

class UserStatsDeserializer : JsonDeserializer<UserStatsOutputModel> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): UserStatsOutputModel {
        val jsonObject = json?.asJsonObject ?: throw JsonParseException("Invalid JSON")
        val uid = jsonObject["uid"].asInt
        val username = jsonObject["username"].asString
        val gamesPlayed = jsonObject["gamesPlayed"].asInt
        val wins = jsonObject["wins"].asInt
        val losses = jsonObject["losses"].asInt
        val draws = gamesPlayed - wins - losses
        val rank = jsonObject["rank"].asInt
        val points = jsonObject["points"].asInt
        return UserStatsOutputModel(
            uid,
            username,
            gamesPlayed,
            wins,
            losses,
            draws,
            rank,
            points
        )
    }
}