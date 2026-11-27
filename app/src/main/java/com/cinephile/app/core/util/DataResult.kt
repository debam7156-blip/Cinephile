package com.cinephile.app.core.util

/**
 * Wrapper for data loading that forces every screen to explicitly handle
 * loading, success, empty and error states instead of crashing on bad data.
 */
sealed interface DataResult<out T> {
    data class Success<T>(val data: T) : DataResult<T>
    data class Failure(val message: String, val cause: Throwable? = null) : DataResult<T>
}

/**
 * Runs [block] and converts any thrown exception into a user-presentable
 * failure with a readable message. Never lets an IO/parse error crash the app.
 */
inline fun <T> dataResultOf(errorMessage: String, block: () -> T): DataResult<T> =
    try {
        DataResult.Success(block())
    } catch (t: Throwable) {
        if (t is kotlinx.coroutines.CancellationException) throw t
        DataResult.Failure(humanize(t, errorMessage), t)
    }

fun humanize(t: Throwable, fallback: String): String = when {
    t is java.net.UnknownHostException -> "No internet connection. Check your network and try again."
    t is java.net.SocketTimeoutException -> "The request timed out. Try again in a moment."
    t is retrofit2.HttpException -> when (t.code()) {
        401 -> "TMDB rejected the request — the API key may be missing or invalid."
        404 -> "The requested content could not be found."
        429 -> "Too many requests — please wait a moment and try again."
        in 500..599 -> "The movie service is temporarily unavailable. Try again later."
        else -> "Request failed (error ${t.code()})."
    }
    else -> fallback
}
