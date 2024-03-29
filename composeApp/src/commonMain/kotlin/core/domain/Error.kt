package core.domain

sealed interface Error {
    suspend fun getMessage(): String
}