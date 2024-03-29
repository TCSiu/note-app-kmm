package taskDetail.domain.repository

import core.domain.Result
import core.domain.TaskDetailError
import core.model.Task

interface TaskDetailRepository {
    suspend fun getTaskDetail(taskId: Int): Result<Task, TaskDetailError>
}