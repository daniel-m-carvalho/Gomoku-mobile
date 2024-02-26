package pt.isel.pdm.gomokuroyale.authentication.domain

import android.util.Patterns

private const val MIN_USERNAME_LENGTH = 5
const val MAX_USERNAME_LENGTH = 20
private const val MIN_PASSWORD_LENGTH = 6
const val MAX_PASSWORD_LENGTH = 20

private const val USERNAME_REGEX = "^[a-zA-Z0-9_]*$"
private const val PASSWORD_REGEX = "^[a-zA-Z0-9_]*$"




fun validateUsername(username: String): Boolean {
    return username.length in MIN_USERNAME_LENGTH..MAX_USERNAME_LENGTH &&
            username.matches(USERNAME_REGEX.toRegex())
}

fun validatePassword(password: String): Boolean {
    return password.length in MIN_PASSWORD_LENGTH..MAX_PASSWORD_LENGTH &&
            password.matches(PASSWORD_REGEX.toRegex()) && password.any { it.isLetter() } && password.any { it.isDigit() }
}

fun validateConfirmationPassword(password: String, confirmationPassword: String): Boolean {
    return password == confirmationPassword && password.isNotBlank() && confirmationPassword.isNotBlank()
}

fun validateEmail(email: String): Boolean {
    return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}