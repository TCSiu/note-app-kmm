package projectCreateOrEdit.domain.repository

import core.response.Response
import projectCreateOrEdit.domain.model.ProjectCreateOrEditRequest

interface ProjectRepository {
    suspend fun getProject(projectId: Int): Response
    suspend fun getTemplateWorkflow(): Response
    suspend fun storeProject(
        projectId: Int? = null,
        projectCreateOrEditRequest: ProjectCreateOrEditRequest
    ): Response
}