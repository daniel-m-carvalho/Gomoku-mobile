package pt.isel.pdm.gomokuroyale.http.media

import okhttp3.MediaType.Companion.toMediaType
import java.net.URI

class Problem(
    val type: URI,
    val title: String,
    val status: Int,
    val detail: String? = null,
    val instance: URI? = null
) {
    companion object {
        private const val MEDIA_TYPE = "application/problem+json"

        val problemMediaType = MEDIA_TYPE.toMediaType()

        fun Problem.toProblemException(): ProblemException = ProblemException(this)
    }

    override fun toString(): String {
        return "Problem(type=$type, title='$title', status=$status, detail=$detail, instance=$instance)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Problem) return false

        if (type != other.type) return false
        if (title != other.title) return false
        if (status != other.status) return false
        if (detail != other.detail) return false
        return instance == other.instance
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + status
        result = 31 * result + (detail?.hashCode() ?: 0)
        result = 31 * result + (instance?.hashCode() ?: 0)
        return result
    }

    class ProblemException(val problem: Problem) : Exception() {
        override fun toString(): String {
            return "ProblemException(problem=$problem)"
        }
    }
}