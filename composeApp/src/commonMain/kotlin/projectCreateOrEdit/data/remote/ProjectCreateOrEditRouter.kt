package projectCreateOrEdit.data.remote

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import core.theme.Constants
import core.theme.SettingKeys
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import projectCreateOrEdit.domain.model.ProjectCreateOrEditRequest
import projectCreateOrEdit.domain.model.ProjectEditModifiedRequest

class ProjectCreateOrEditRouter(
    private val httpClient: HttpClient,
    private val settings: Settings
) : ProjectCreateOrEditApi {
    private val jwtToken = settings[SettingKeys.TOKEN, ""]
    override suspend fun getProject(projectId: Int): HttpResponse {
        return httpClient.get("${Constants.BASE_URL}/project/${projectId}/edit") {
            bearerAuth(jwtToken)
        }
    }

    override suspend fun getTemplateWorkflow(): HttpResponse {
        return httpClient.get("${Constants.BASE_URL}/recommend-workflow") {
            bearerAuth(jwtToken)
        }
    }

    override suspend fun storeProject(
        projectId: Int,
        projectCreateOrEditRequest: ProjectCreateOrEditRequest
    ): HttpResponse {
        val projectEditModifiedRequest = ProjectEditModifiedRequest(
            name = projectCreateOrEditRequest.name,
            description = projectCreateOrEditRequest.description,
            workflow = Json.encodeToString(projectCreateOrEditRequest.workflow)
        )
        return httpClient.put("${Constants.BASE_URL}/project/${projectId}") {
            bearerAuth(jwtToken)
            setBody(projectEditModifiedRequest)
        }
    }

    override suspend fun createProject(projectCreateOrEditRequest: ProjectCreateOrEditRequest): HttpResponse {
        return httpClient.post("${Constants.BASE_URL}/project") {
            bearerAuth(jwtToken)
            setBody(projectCreateOrEditRequest)
        }
    }
}