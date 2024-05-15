package com.mdp.tourisview.util

sealed class ApiResult<out T> {
    data class Error(val message: String): ApiResult<Nothing>()
    data class Success<T>(val data: T): ApiResult<T>()
}