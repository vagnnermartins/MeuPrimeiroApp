package com.example.meuprimeiroapp.service

import retrofit2.HttpException

/**
 * A sealed class representing the result of an operation, which can be either a success or an error.
 * @param T The type of the data returned on success.
 */
sealed class Result<out T> {
    /**
     * Represents a successful result.
     * @param data The data returned by the operation.
     */
    data class Success<out T>(val data: T) : Result<T>()
    /**
     * Represents an error result.
     * @param code The error code.
     * @param message The error message.
     */
    data class Error(val code: Int, val message: String) : Result<Nothing>()
}

/**
 * A suspend function that safely executes an API call and returns a [Result] object.
 * @param apiCall The suspend function representing the API call.
 * @return A [Result] object representing the outcome of the API call.
 */
suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
    return try {
        val response = apiCall()
        Result.Success(response)
    } catch (e: Exception) {
        when (e) {
            is HttpException -> {
                val code = e.code()
                val message = e.message()
                Result.Error(code, message)
            }
            else -> {
                Result.Error(-1, e.message ?: "Unknown error")
            }
        }
    }
}
