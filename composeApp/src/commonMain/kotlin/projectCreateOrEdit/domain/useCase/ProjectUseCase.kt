package projectCreateOrEdit.domain.useCase

import core.response.Response
import projectCreateOrEdit.data.repository.ProjectRepositoryImpl
import projectCreateOrEdit.domain.model.ProjectCreateOrEditRequest

class ProjectUseCase(
    private val projectRepositoryImpl: ProjectRepositoryImpl
) {
    suspend fun getProject(projectId: Int): Response {
        return projectRepositoryImpl.getProject(projectId)
    }

    suspend fun getTemplateWorkflow(): Response {
        return projectRepositoryImpl.getTemplateWorkflow()
    }

    suspend fun storeProject(
        projectId: Int? = null,
        projectCreateOrEditRequest: ProjectCreateOrEditRequest
    ): Response {
        return projectRepositoryImpl.storeProject(
            projectId = projectId,
            projectCreateOrEditRequest = projectCreateOrEditRequest
        )
    }
}