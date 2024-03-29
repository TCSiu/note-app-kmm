package taskDetail.data.repository

import core.domain.Result
import core.domain.TaskDetailError
import core.model.Task
import core.response.success.TaskSuccess
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.serialization.SerializationException
import taskDetail.data.remote.TaskDetailRouter
import taskDetail.domain.repository.TaskDetailRepository

class TaskDetailRepositoryImpl(private val taskDetailRouter: TaskDetailRouter) :
    TaskDetailRepository {
    override suspend fun getTaskDetail(taskId: Int): Result<Task, TaskDetailError> {
        return try {
            val response = taskDetailRouter.getTaskDetail(taskId = taskId)
            val body = response.body<TaskSuccess>()
            Result.Success(body.data)
        } catch (e: SerializationException) {
            Result.Error(TaskDetailError.Serialization)
        } catch (e: RedirectResponseException) {
            Result.Error(TaskDetailError.Unknown)
        } catch (e: ServerResponseException) {
            Result.Error(TaskDetailError.Unknown)
        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                404 -> {
                    Result.Error(TaskDetailError.NotFound)
                }

                401 -> {
                    Result.Error(TaskDetailError.Unauthorized)
                }

                else -> {
                    Result.Error(TaskDetailError.Unknown)
                }
            }
        } catch (e: Exception) {
            Result.Error(TaskDetailError.Unknown)
        }
    }
}