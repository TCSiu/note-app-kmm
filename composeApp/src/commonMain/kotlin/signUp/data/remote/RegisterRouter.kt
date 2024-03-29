package signUp.data.remote

import core.theme.Constants
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import signUp.domain.model.Register

class RegisterRouter(private val httpClient: HttpClient) : RegisterApi {
    override suspend fun register(register: Register): HttpResponse {
        return httpClient.post("${Constants.BASE_URL}/register") {
            setBody(register)
        }
    }
}