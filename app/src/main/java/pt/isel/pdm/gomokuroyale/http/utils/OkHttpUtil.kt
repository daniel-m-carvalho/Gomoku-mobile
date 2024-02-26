package pt.isel.pdm.gomokuroyale.http.utils

import com.google.gson.Gson
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import pt.isel.pdm.gomokuroyale.http.media.Problem
import pt.isel.pdm.gomokuroyale.http.media.Problem.Companion.problemMediaType
import pt.isel.pdm.gomokuroyale.http.media.Problem.Companion.toProblemException
import pt.isel.pdm.gomokuroyale.http.media.siren.SirenModel.Companion.sirenMediaType
import java.lang.reflect.Type
import kotlin.coroutines.resumeWithException

suspend inline fun <reified T> Request.makeAPIRequest(
    client: OkHttpClient,
    responseType: Type,
    gson: Gson
): T =
    suspendCancellableCoroutine { continuation ->
        val newCall = client.newCall(this)
        newCall.enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                continuation.resumeWithException(
                    FetchFromAPIException(
                        message = "Failed to send request to API",
                        cause = e
                    )
                )
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                val body = response.body ?: throw FetchFromAPIException(
                    "Error fetching from API, body is null. Remote service returned ${response.code}",
                    null
                )
                val contentType = body.contentType()
                val resJson = body.string()

                when {
                    response.isSuccessful && contentType == sirenMediaType -> {
                        continuation.resumeWith(
                            Result.success(gson.fromJson(resJson, responseType))
                        )
                    }

                    !response.isSuccessful && contentType == problemMediaType -> {
                        val problem = gson.fromJson(resJson, Problem::class.java)
                        continuation.resumeWith(
                            Result.failure(exception = problem.toProblemException())
                        )
                    }

                    else ->
                        continuation.resumeWithException(
                            FetchFromAPIException(
                                "Error fetching from API. Remote service returned ${response.code}",
                                null
                            )
                        )
                }
            }
        })

        continuation.invokeOnCancellation { newCall.cancel() }
    }
