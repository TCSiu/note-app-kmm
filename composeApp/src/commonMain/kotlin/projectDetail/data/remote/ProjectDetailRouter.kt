package projectDetail.data.remote

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import core.theme.Constants
import core.theme.SettingKeys
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

class ProjectDetailRouter(private val httpClient: HttpClient, private val settings: Settings) :
    ProjectDetailApi {
    private val jwtToken = settings[SettingKeys.TOKEN, ""]
    override suspend fun getProjectDetail(projectId: Int): HttpResponse {
        return httpClient.get("${Constants.BASE_URL}/project/${projectId}") {
            bearerAuth(jwtToken)
        }
    }

    override suspend fun getProjectTasks(projectId: Int, workflowUuid: String?): HttpResponse {
        return httpClient.get("${Constants.BASE_URL}/project/${projectId}/tasks") {
            bearerAuth(jwtToken)
            parameter("workflow_uuid", workflowUuid)
        }
    }

}