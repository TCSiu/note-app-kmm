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
sealed interface TaskDetailError : Error {
    data object Unauthorized : TaskDetailError {
        override suspend fun getMessage(): String {
            return getString(Res.string.unauthorized)
        }
    }

    data object NotFound : TaskDetailError {
        override suspend fun getMessage(): String {
            return getString(Res.string.not_found)
        }
    }

    data object NoPermission : TaskDetailError {
        override suspend fun getMessage(): String {
            return getString(Res.string.no_permission)
        }
    }

    data object Serialization : TaskDetailError {
        override suspend fun getMessage(): String {
            return getString(Res.string.serialization)
        }
    }

    data class BadRequest(val message: String = "test") : TaskDetailError {
        override suspend fun getMessage(): String {
            return this.message
        }
    }

    data object Unknown : TaskDetailError {
        override suspend fun getMessage(): String {
            return getString(Res.string.unknown)
        }
    }
}