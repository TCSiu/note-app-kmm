package core.domain

import newkmmnoteapp.composeapp.generated.resources.Res
import newkmmnoteapp.composeapp.generated.resources.no_permission
import newkmmnoteapp.composeapp.generated.resources.not_found
import newkmmnoteapp.composeapp.generated.resources.serialization
import newkmmnoteapp.composeapp.generated.resources.unauthorized
import newkmmnoteapp.composeapp.generated.resources.unknown
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString

@OptIn(ExperimentalResourceApi::class)
sealed interface ProjectDetailError : Error {
    data object Unauthorized : ProjectDetailError {
        override suspend fun getMessage(): String {
            return getString(Res.string.unauthorized)
        }
    }

    data object NotFound : ProjectDetailError {
        override suspend fun getMessage(): String {
            return getString(Res.string.not_found)
        }
    }

    data object NoPermission : ProjectDetailError {
        override suspend fun getMessage(): String {
            return getString(Res.string.no_permission)
        }
    }

    data object Serialization : ProjectDetailError {
        override suspend fun getMessage(): String {
            return getString(Res.string.serialization)
        }
    }

    data class BadRequest(val message: String = "test") : ProjectDetailError {
        override suspend fun getMessage(): String {
            return this.message
        }
    }

    data object Unknown : ProjectDetailError {
        override suspend fun getMessage(): String {
            return getString(Res.string.unknown)
        }
    }
}