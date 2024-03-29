package projectDetail.domain.repository

import core.domain.ProjectDetailError
import core.domain.Result
import core.model.Project
import core.model.Task

interface ProjectDetailRepository {
    suspend fun getProjectDetail(projectId: Int): Result<Project, ProjectDetailError>
    suspend fun getProjectTasks(
        projectId: Int,
        workflowUuid: String? = null
    ): Result<Map<String, List<Task>>, ProjectDetailError>
}