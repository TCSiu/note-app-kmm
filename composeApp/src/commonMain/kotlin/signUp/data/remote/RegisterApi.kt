package signUp.data.remote

import io.ktor.client.statement.HttpResponse
import signUp.domain.model.Register

interface RegisterApi {
    suspend fun register(register: Register): HttpResponse
}