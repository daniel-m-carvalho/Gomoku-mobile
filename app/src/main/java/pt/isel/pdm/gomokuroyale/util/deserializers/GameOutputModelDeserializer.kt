package pt.isel.pdm.gomokuroyale.util.deserializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import pt.isel.pdm.gomokuroyale.game.play.domain.board.Board
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import pt.isel.pdm.gomokuroyale.http.domain.users.UserModel
import pt.isel.pdm.gomokuroyale.http.domain.users.UserModel.Companion.toUser
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameOutputModel
import java.lang.reflect.Type

class GameOutputModelDeserializer : JsonDeserializer<GameOutputModel> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GameOutputModel {
        try {
            val jsonObject = json?.asJsonObject ?: throw JsonParseException("Invalid JSON")
            val id = jsonObject["id"].asInt
            val board = context?.deserialize<Board>(jsonObject["board"], Board::class.java)
            val userBlack =
                context?.deserialize<UserModel>(jsonObject["userBlack"], UserModel::class.java)
            val userWhite =
                context?.deserialize<UserModel>(jsonObject["userWhite"], UserModel::class.java)
            val state = jsonObject["state"].asString
            val variant = context?.deserialize<Variant>(jsonObject["variant"], Variant::class.java)
            val created = jsonObject["created"].asString
            if (userBlack == null || userWhite == null || variant == null || board == null)
                throw JsonParseException("Invalid JSON")
            return GameOutputModel(
                id = id,
                board = board,
                userBlack = userBlack.toUser(),
                userWhite = userWhite.toUser(),
                state = state,
                variant = variant,
                created = created
            )
        } catch (e: MalformedJsonException) {
            throw JsonParseException("Malformed JSON", e)
        }
    }
}

