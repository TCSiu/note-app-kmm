package taskDetail.data.remote

import io.ktor.client.statement.HttpResponse

interface TaskDetailApi {
    suspend fun getTaskDetail(taskId: Int): HttpResponse
}