package projectCreateOrEdit.data.remote

import io.ktor.client.statement.HttpResponse
import projectCreateOrEdit.domain.model.ProjectCreateOrEditRequest

interface ProjectCreateOrEditApi {
    suspend fun getProject(projectId: Int): HttpResponse
    suspend fun getTemplateWorkflow(): HttpResponse
    suspend fun storeProject(
        projectId: Int,
        projectCreateOrEditRequest: ProjectCreateOrEditRequest
    ): HttpResponse

    suspend fun createProject(projectCreateOrEditRequest: ProjectCreateOrEditRequest): HttpResponse
}