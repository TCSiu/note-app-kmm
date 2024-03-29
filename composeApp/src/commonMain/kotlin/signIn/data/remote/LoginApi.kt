package signIn.data.remote

import io.ktor.client.statement.HttpResponse
import signIn.domain.model.Login

interface LoginApi {
    suspend fun login(login: Login): HttpResponse
}