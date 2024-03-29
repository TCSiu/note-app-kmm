package home.data.remote

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import core.theme.Constants
import core.theme.SettingKeys
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class HomeRouter(private val httpClient: HttpClient, settings: Settings) : HomeApi {
    private val jwtToken = settings[SettingKeys.TOKEN, ""]
    override suspend fun getProjects(): HttpResponse {
        val response = httpClient.get("${Constants.BASE_URL}/project") {
            bearerAuth(jwtToken)
        }
        return response
    }
}