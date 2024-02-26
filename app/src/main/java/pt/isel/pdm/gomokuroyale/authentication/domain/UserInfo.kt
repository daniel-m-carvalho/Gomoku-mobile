package pt.isel.pdm.gomokuroyale.authentication.domain

data class User(
    val id: Id,
    val username: String,
    val email: Email,
) {
    private fun validateUser(user: User): Boolean {
        return validateUsername(user.username) && validateEmail(user.email.value)
    }
}

data class PasswordValidationInfo(val validationInfo: String) {
    private fun validatePasswordValidationInfo(passwordValidationInfo: PasswordValidationInfo): Boolean {
        return passwordValidationInfo.validationInfo.isNotBlank()
    }
}

data class Email(val value: String) {
    private fun validateEmail(email: Email): Boolean {
        return email.value.isNotBlank()
    }
}

data class Id(val value: Int)

data class UserInfo(val accessToken: String, val username: String) {
    private fun validateUserInfo(userInfo: UserInfo): Boolean {
        return userInfo.accessToken.isNotBlank() &&
                userInfo.username.isNotBlank()
    }
}