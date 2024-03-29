package projectCreateOrEdit.data.repository

import core.response.Response
import core.response.fail.ResponseFail
import core.response.success.ProjectSuccess
import core.response.success.WorkflowGetTemplateSuccess
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import projectCreateOrEdit.data.remote.ProjectCreateOrEditRouter
import projectCreateOrEdit.domain.model.ProjectCreateOrEditRequest
import projectCreateOrEdit.domain.repository.ProjectRepository

class ProjectRepositoryImpl(private val projectCreateOrEditRouter: ProjectCreateOrEditRouter) :
    ProjectRepository {
    override suspend fun getProject(projectId: Int): Response {
        val response: HttpResponse = projectCreateOrEditRouter.getProject(projectId)
        if (response.status.value in 200..299) {
            return response.body<ProjectSuccess>()
        }
        return response.body<ResponseFail>()
    }

    override suspend fun getTemplateWorkflow(): Response {
        val response: HttpResponse = projectCreateOrEditRouter.getTemplateWorkflow()
        if (response.status.value in 200..299) {
            return response.body<WorkflowGetTemplateSuccess>()
        }
        return response.body<ResponseFail>()
    }

    override suspend fun storeProject(
        projectId: Int?,
        projectCreateOrEditRequest: ProjectCreateOrEditRequest
    ): Response {
        var response: HttpResponse? = null
        response = if (projectId != null) {
            projectCreateOrEditRouter.storeProject(
                projectId = projectId,
                projectCreateOrEditRequest = projectCreateOrEditRequest
            )
        } else {
            projectCreateOrEditRouter.createProject(projectCreateOrEditRequest = projectCreateOrEditRequest)
        }
        println("test")
        println(response.bodyAsText())
        if (response.status.value in 200..299) {
            return response.body<ProjectSuccess>()
        }
        return response.body<ResponseFail>()
    }
}