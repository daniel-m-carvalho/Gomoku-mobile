package pt.isel.pdm.gomokuroyale.http.utils

data class FetchFromAPIException(override val message: String, override val cause: Throwable?) :
    Exception(message, cause)
