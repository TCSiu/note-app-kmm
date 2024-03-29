package projectDetail.data.repository

import core.domain.ProjectDetailError
import core.domain.Result
import core.model.Project
import core.model.Task
import core.response.success.ProjectSuccess
import core.response.success.ProjectTasksSuccess
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.serialization.SerializationException
import projectDetail.data.remote.ProjectDetailRouter
import projectDetail.domain.repository.ProjectDetailRepository

class ProjectDetailRepositoryImpl(private val projectDetailRouter: ProjectDetailRouter) :
    ProjectDetailRepository {
    override suspend fun getProjectDetail(projectId: Int): Result<Project, ProjectDetailError> {
        return try {
            val response = projectDetailRouter.getProjectDetail(projectId = projectId)
            val body = response.body<ProjectSuccess>()
            Result.Success(body.data)
        } catch (e: SerializationException) {
            Result.Error(ProjectDetailError.Serialization)
        } catch (e: RedirectResponseException) {
            Result.Error(ProjectDetailError.Unknown)
        } catch (e: ServerResponseException) {
            Result.Error(ProjectDetailError.Unknown)
        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                400 -> {
                    Result.Error(ProjectDetailError.BadRequest())
                }

                401 -> {
                    Result.Error(ProjectDetailError.Unauthorized)
                }

                404 -> {
                    Result.Error(ProjectDetailError.NotFound)
                }

                else -> {
                    Result.Error(ProjectDetailError.Unknown)
                }
            }
        } catch (e: Exception) {
            Result.Error(ProjectDetailError.Unknown)
        }
    }

    override suspend fun getProjectTasks(
        projectId: Int,
        workflowUuid: String?
    ): Result<Map<String, List<Task>>, ProjectDetailError> {
        return try {
            val response = projectDetailRouter.getProjectTasks(
                projectId = projectId,
                workflowUuid = workflowUuid
            )
            val body = response.body<ProjectTasksSuccess>()
            Result.Success(body.data)
        } catch (e: SerializationException) {
            Result.Error(ProjectDetailError.Serialization)
        } catch (e: RedirectResponseException) {
            Result.Error(ProjectDetailError.Unknown)
        } catch (e: ServerResponseException) {
            Result.Error(ProjectDetailError.Serialization)
        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                400 -> {
                    Result.Error(ProjectDetailError.BadRequest())
                }

                401 -> {
                    Result.Error(ProjectDetailError.Unauthorized)
                }

                404 -> {
                    Result.Error(ProjectDetailError.NotFound)
                }

                else -> {
                    Result.Error(ProjectDetailError.Unknown)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(ProjectDetailError.Unknown)
        }
    }
}