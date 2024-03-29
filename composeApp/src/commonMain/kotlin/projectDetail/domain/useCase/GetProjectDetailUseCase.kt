package projectDetail.domain.useCase

import core.domain.ProjectDetailError
import core.domain.Result
import kotlinx.coroutines.runBlocking
import projectDetail.data.repository.ProjectDetailRepositoryImpl
import projectDetail.data.result.ProjectDetailResult

class GetProjectDetailUseCase(val projectDetailRepositoryImpl: ProjectDetailRepositoryImpl) {
    suspend operator fun invoke(projectId: Int): ProjectDetailResult {
        val projectDetailResult = ProjectDetailResult()
        runBlocking {
            when (val detailResult =
                projectDetailRepositoryImpl.getProjectDetail(projectId = projectId)) {
                is Result.Error -> {
                    projectDetailResult.projectDetailError = when (val error = detailResult.error) {
                        is ProjectDetailError.BadRequest -> {
                            error.getMessage()
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
                    projectDetailResult.projectDetail = detailResult.data
                }
            }
            when (val tasksResponse =
                projectDetailRepositoryImpl.getProjectTasks(projectId = projectId)) {
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
        }
        return projectDetailResult
    }
}