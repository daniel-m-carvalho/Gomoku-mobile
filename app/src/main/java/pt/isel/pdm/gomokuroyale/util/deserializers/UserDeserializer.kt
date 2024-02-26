package pt.isel.pdm.gomokuroyale.util.deserializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import pt.isel.pdm.gomokuroyale.authentication.domain.Email
import pt.isel.pdm.gomokuroyale.authentication.domain.Id
import pt.isel.pdm.gomokuroyale.authentication.domain.PasswordValidationInfo
import pt.isel.pdm.gomokuroyale.http.domain.users.UserModel
import java.lang.reflect.Type

class UserDeserializer : JsonDeserializer<UserModel> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): UserModel {
        val jsonObject = json?.asJsonObject ?: throw JsonParseException("Invalid JSON")
        val id =
            context?.deserialize<Id>(jsonObject["id"], Id::class.java) ?: throw JsonParseException(
                "Invalid JSON"
            )
        val username = jsonObject["username"].asString
        val email = context.deserialize<Email>(jsonObject["email"], Email::class.java)
        val passwordValidationInfo = context.deserialize<PasswordValidationInfo>(
            jsonObject["passwordValidation"],
            PasswordValidationInfo::class.java
        )
        return UserModel(
            id = id,
            username = username,
            email = email,
            passwordValidationInfo = passwordValidationInfo
        )
    }
}