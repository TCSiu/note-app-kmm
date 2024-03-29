package projectDetail.data.remote

import io.ktor.client.statement.HttpResponse

interface ProjectDetailApi {
    suspend fun getProjectDetail(projectId: Int): HttpResponse
    suspend fun getProjectTasks(projectId: Int, workflowUuid: String? = null): HttpResponse
}