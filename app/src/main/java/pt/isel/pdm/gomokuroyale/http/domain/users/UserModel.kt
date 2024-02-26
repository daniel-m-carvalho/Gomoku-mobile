package pt.isel.pdm.gomokuroyale.http.domain.users

import pt.isel.pdm.gomokuroyale.authentication.domain.Email
import pt.isel.pdm.gomokuroyale.authentication.domain.Id
import pt.isel.pdm.gomokuroyale.authentication.domain.PasswordValidationInfo
import pt.isel.pdm.gomokuroyale.authentication.domain.User

data class UserModel(
    val id: Id,
    val username: String,
    val email: Email,
    val passwordValidationInfo: PasswordValidationInfo
) {
    companion object {
        fun UserModel.toUser() = User(id, username, email)
    }
}