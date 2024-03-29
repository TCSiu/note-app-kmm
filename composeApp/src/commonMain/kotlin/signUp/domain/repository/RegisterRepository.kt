package signUp.domain.repository

import core.response.Response
import signUp.domain.model.Register

interface RegisterRepository {
    suspend fun register(register: Register): Response
}