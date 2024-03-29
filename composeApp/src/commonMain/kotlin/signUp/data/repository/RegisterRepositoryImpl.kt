package signUp.data.repository

import core.response.Response
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import signUp.data.remote.RegisterRouter
import signUp.domain.model.Register
import signUp.domain.repository.RegisterRepository
import signUp.domain.response.RegisterResponseFail
import signUp.domain.response.RegisterResponseSuccess

class RegisterRepositoryImpl(private val registerRouter: RegisterRouter) : RegisterRepository {
    override suspend fun register(register: Register): Response {
        val response: HttpResponse = registerRouter.register(register)
        if (response.status.value in 200..299) {
            return response.body<RegisterResponseSuccess>()
        }
        return response.body<RegisterResponseFail>()
    }
}