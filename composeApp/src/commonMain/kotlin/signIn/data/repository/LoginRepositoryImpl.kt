package signIn.data.repository

import core.response.Response
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import signIn.data.remote.LoginRouter
import signIn.domain.model.Login
import signIn.domain.repository.LoginRepository
import signIn.domain.response.LoginResponseFail
import signIn.domain.response.LoginResponseSuccess

class LoginRepositoryImpl(private val loginRouter: LoginRouter) : LoginRepository {
    override suspend fun login(login: Login): Response? {
        try {
            val response: HttpResponse = loginRouter.login(login)
            if (response.status.value in 200..299) {
                return response.body<LoginResponseSuccess>()
            }
            return response.body<LoginResponseFail>()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}