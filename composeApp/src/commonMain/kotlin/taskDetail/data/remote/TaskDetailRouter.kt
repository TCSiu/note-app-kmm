package taskDetail.data.remote

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import core.theme.Constants
import core.theme.SettingKeys
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class TaskDetailRouter(private val httpClient: HttpClient, private val settings: Settings) :
    TaskDetailApi {
    private val jwtToken = settings[SettingKeys.TOKEN, ""]
    override suspend fun getTaskDetail(taskId: Int): HttpResponse {
        return httpClient.get("${Constants.BASE_URL}/task/${taskId}") {
            bearerAuth(jwtToken)
        }
    }
}