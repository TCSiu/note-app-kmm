package projectDetail.domain.useCase

import core.domain.ProjectDetailError
import core.domain.Result
import projectDetail.data.repository.ProjectDetailRepositoryImpl
import projectDetail.data.result.ProjectDetailResult

class ChangeWorkflowUseCase(val projectDetailRepositoryImpl: ProjectDetailRepositoryImpl) {
    suspend operator fun invoke(projectId: Int, workflowUuid: String? = null): ProjectDetailResult {
        val projectDetailResult = ProjectDetailResult()
        when (val tasksResponse =
            projectDetailRepositoryImpl.getProjectTasks(projectId, workflowUuid = workflowUuid)) {
            is Result.Error -> {
                projectDetailResult.projectTasksError = when (val error = tasksResponse.error) {
                    is ProjectDetailError.BadRequest -> {
                        error.message
                    }

                    is ProjectDetailError.NoPermission -> {
                        error.getMessage()
                    }

                    is ProjectDetailError.NotFound -> {
                        error.getMessage()
                    }

                    is ProjectDetailError.Serialization -> {
                        error.getMessage()
                    }

                    is ProjectDetailError.Unauthorized -> {
                        error.getMessage()
                    }

                    is ProjectDetailError.Unknown -> {
                        error.getMessage()
                    }
                }
            }

            is Result.Success -> {
                projectDetailResult.projectTasks = tasksResponse.data
            }
        }
        return projectDetailResult
    }
}