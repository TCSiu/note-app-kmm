package taskDetail.domain.useCase

import core.domain.Result
import core.domain.TaskDetailError
import kotlinx.coroutines.runBlocking
import taskDetail.data.repository.TaskDetailRepositoryImpl
import taskDetail.data.result.TaskDetailResult

class TaskDetailUseCase(private val taskDetailRepositoryImpl: TaskDetailRepositoryImpl) {
    suspend operator fun invoke(taskId: Int): TaskDetailResult {
        val taskDetailResponse = TaskDetailResult()
        runBlocking {
            when (val response = taskDetailRepositoryImpl.getTaskDetail(taskId = taskId)) {
                is Result.Error -> {
                    taskDetailResponse.taskDetailError = when (val error = response.error) {
                        is TaskDetailError.BadRequest -> {
                            error.getMessage()
                        }

                        TaskDetailError.NoPermission -> {
                            error.getMessage()
                        }

                        TaskDetailError.NotFound -> {
                            error.getMessage()
                        }

                        TaskDetailError.Serialization -> {
                            error.getMessage()
                        }

                        TaskDetailError.Unauthorized -> {
                            error.getMessage()
                        }

                        TaskDetailError.Unknown -> {
                            error.getMessage()
                        }
                    }
                }

                is Result.Success -> {
                    taskDetailResponse.task = response.data
                }
            }
        }
        return taskDetailResponse
    }
}