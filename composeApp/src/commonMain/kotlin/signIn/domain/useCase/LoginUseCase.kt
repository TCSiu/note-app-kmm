package signIn.domain.useCase

import core.response.Response
import signIn.data.repository.LoginRepositoryImpl
import signIn.domain.model.Login
import signIn.domain.response.LoginResponseSuccess
import signIn.domain.response.LoginResponseSuccessData

class LoginUseCase(
    private val loginRepositoryImpl: LoginRepositoryImpl
) {
    suspend fun login(login: Login): Response {
        return loginRepositoryImpl.login(login) ?: LoginResponseSuccess(
            success = false,
            data = LoginResponseSuccessData(),
            message = ""
        )
    }
}