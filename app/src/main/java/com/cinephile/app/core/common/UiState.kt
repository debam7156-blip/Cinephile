package com.cinephile.app.core.common

/**
 * Wrapper for repository results that distinguishes successful outcomes from
 * recoverable failures, so UI can render proper loading / error / empty states.
 */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}
