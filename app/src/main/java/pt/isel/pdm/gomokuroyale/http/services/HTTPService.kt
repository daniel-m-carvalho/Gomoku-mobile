package pt.isel.pdm.gomokuroyale.http.services

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.EMPTY_REQUEST
import pt.isel.pdm.gomokuroyale.http.domain.UriRepository
import pt.isel.pdm.gomokuroyale.http.media.Problem
import pt.isel.pdm.gomokuroyale.http.media.siren.SirenModel
import pt.isel.pdm.gomokuroyale.http.utils.makeAPIRequest
import pt.isel.pdm.gomokuroyale.util.ApiError
import pt.isel.pdm.gomokuroyale.util.HttpResult

abstract class HTTPService(
    val httpClient: OkHttpClient,
    val jsonEncoder: Gson,
    val apiEndpoint: String,
    val uriRepository: UriRepository
) {
    protected suspend inline fun <reified T> Request.getResponse(): HttpResult<SirenModel<T>> =
        try {
            val res = makeAPIRequest<T>(httpClient, SirenModel.getType<T>().type, jsonEncoder)
            HttpResult.Success(res as SirenModel<T>)
        } catch (e: Problem.ProblemException) {
            HttpResult.Failure(ApiError(e.problem.detail ?: e.problem.title))
        } catch (e: Exception) {
            HttpResult.Failure(ApiError(e.message ?: "Unknown error"))
        }

    protected val gson = jsonEncoder

    protected suspend inline fun <reified T> get(path: String): HttpResult<SirenModel<T>> =
        Request.Builder()
            .url("$apiEndpoint$path")
            .addHeader("accept", APPLICATION_JSON)
            .build()
            .getResponse()

    protected suspend inline fun <reified T> get(
        path: String,
        token: String
    ): HttpResult<SirenModel<T>> =
        Request.Builder()
            .url("$apiEndpoint$path")
            .addHeader("accept", APPLICATION_JSON)
            .addHeader(AUTHORIZATION_HEADER, "$TOKEN_TYPE $token")
            .build()
            .getResponse()

    protected suspend inline fun <reified T> post(
        path: String,
        body: Any = EMPTY_REQUEST
    ): HttpResult<SirenModel<T>> =
        Request.Builder()
            .url("$apiEndpoint$path")
            .addHeader("accept", APPLICATION_JSON)
            .post(jsonEncoder.toJson(body).toRequestBody(contentType = applicationJsonMediaType))
            .build()
            .getResponse()

    protected suspend inline fun <reified T> post(
        path: String,
        token: String,
        body: Any? = null
    ): HttpResult<SirenModel<T>> =
        Request.Builder()
            .url("$apiEndpoint$path")
            .addHeader("accept", APPLICATION_JSON)
            .addHeader(AUTHORIZATION_HEADER, "$TOKEN_TYPE $token")
            .post(
                body = body?.let {
                    jsonEncoder.toJson(it).toRequestBody(contentType = applicationJsonMediaType)
                } ?: EMPTY_REQUEST
            )
            .build()
            .getResponse()

    protected suspend inline fun <reified T> put(
        path: String,
        token: String,
        body: Any = EMPTY_REQUEST
    ): HttpResult<SirenModel<T>> =
        Request.Builder()
            .url("$apiEndpoint$path")
            .addHeader("accept", APPLICATION_JSON)
            .addHeader(AUTHORIZATION_HEADER, "$TOKEN_TYPE $token")
            .put(
                jsonEncoder.toJson(body)
                    .toRequestBody(contentType = applicationJsonMediaType)
            )
            .build()
            .getResponse()

    protected suspend inline fun <reified T> delete(
        path: String,
        token: String,
        body: Any = EMPTY_REQUEST
    ): HttpResult<SirenModel<T>> =
        Request.Builder()
            .url("$apiEndpoint$path")
            .addHeader("accept", APPLICATION_JSON)
            .addHeader(AUTHORIZATION_HEADER, "$TOKEN_TYPE $token")
            .delete(
                jsonEncoder.toJson(body)
                    .toRequestBody(contentType = applicationJsonMediaType)
            )
            .build()
            .getResponse()

    companion object {
        const val APPLICATION_JSON = "application/json"
        val applicationJsonMediaType = APPLICATION_JSON.toMediaType()

        const val AUTHORIZATION_HEADER = "Authorization"
        const val TOKEN_TYPE = "Bearer"
    }
}
