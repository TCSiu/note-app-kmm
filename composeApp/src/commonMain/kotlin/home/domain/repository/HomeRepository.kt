package home.domain.repository

import core.domain.ProjectDetailError
import core.domain.Result
import core.model.Project

interface HomeRepository {
    //    suspend fun getProjects(): Response
    suspend fun getProjects(): Result<List<Project>, ProjectDetailError>
}