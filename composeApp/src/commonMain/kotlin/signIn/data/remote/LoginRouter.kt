package signIn.data.remote

import core.theme.Constants
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import signIn.domain.model.Login

class LoginRouter(private val httpClient: HttpClient) : LoginApi {
    override suspend fun login(login: Login): HttpResponse {
        return httpClient.post("${Constants.BASE_URL}/login") {
            setBody(login)
        }
    }
}