package signIn.domain.repository

import core.response.Response
import signIn.domain.model.Login

interface LoginRepository {
    suspend fun login(login: Login): Response?
}