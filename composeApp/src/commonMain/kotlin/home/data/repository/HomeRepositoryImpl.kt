package home.data.repository

import core.domain.ProjectDetailError
import core.domain.Result
import core.model.Project
import home.data.remote.HomeRouter
import home.domain.repository.HomeRepository
import home.domain.response.ProjectsResponseSuccess
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.SerializationException

class HomeRepositoryImpl(private val homeRouter: HomeRouter) : HomeRepository {
    override suspend fun getProjects(): Result<List<Project>, ProjectDetailError> {
        return try {
            val response = homeRouter.getProjects()
            val body = response.body<ProjectsResponseSuccess>()
            Result.Success(body.data)
        } catch (e: SerializationException) {
            Result.Error(ProjectDetailError.Serialization)
        } catch (e: RedirectResponseException) {
            println(e.response.bodyAsText())
            Result.Error(ProjectDetailError.Unknown)
        } catch (e: ServerResponseException) {
            println(e.response.bodyAsText())
            Result.Error(ProjectDetailError.Unknown)
        } catch (e: ClientRequestException) {
            println(e.response.bodyAsText())
            when (e.response.status.value) {
                400 -> {
                    Result.Error(ProjectDetailError.BadRequest(message = "fk"))
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