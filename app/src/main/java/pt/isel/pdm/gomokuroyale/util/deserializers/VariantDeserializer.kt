package pt.isel.pdm.gomokuroyale.util.deserializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import java.lang.reflect.Type
class VariantDeserializer : JsonDeserializer<Variant> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Variant {
        val jsonObject = json?.asJsonObject ?: throw JsonParseException("Invalid JSON")
        val name = jsonObject["name"].asString
        val boardSize = jsonObject["boardDim"].asInt
        val playRule = jsonObject["playRule"].asString
        val openingRule = jsonObject["openingRule"].asString
        val points = jsonObject["points"].asInt
        return Variant(name, boardSize, playRule, openingRule, points)
    }
}