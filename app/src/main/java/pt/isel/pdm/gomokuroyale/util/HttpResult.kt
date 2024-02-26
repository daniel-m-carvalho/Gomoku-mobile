package pt.isel.pdm.gomokuroyale.util

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed class HttpResult<out T> {
    data class Success<T>(val value: T) : HttpResult<T>()
    data class Failure(val error: ApiError) : HttpResult<Nothing>()
}

class ApiError(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

inline fun <T, R> HttpResult<T>.onSuccess(action: (T) -> HttpResult<R>): HttpResult<R> {
    return when (this) {
        is HttpResult.Success -> action(value)
        is HttpResult.Failure -> this
    }
}

inline fun <T> HttpResult<T>.onFailure(action: (ApiError) -> HttpResult<T>): HttpResult<T> {
    return when (this) {
        is HttpResult.Success -> this
        is HttpResult.Failure -> action(error)
    }
}

inline fun <T> HttpResult<T>.onSuccessResult(action: (T) -> Unit): HttpResult<T> {
    if (this is HttpResult.Success) action(value)
    return this
}

inline fun <T> HttpResult<T>.onFailureResult(action: (ApiError) -> Unit): HttpResult<T> {
    if (this is HttpResult.Failure) action(error)
    return this
}

@OptIn(ExperimentalContracts::class)
fun <T> HttpResult<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is HttpResult.Success<T>)
    }
    return this is HttpResult.Success
}

@OptIn(ExperimentalContracts::class)
fun <T> HttpResult<T>.isFailure(): Boolean {
    contract {
        returns(true) implies (this@isFailure is HttpResult.Failure)
    }
    return this is HttpResult.Failure
}


