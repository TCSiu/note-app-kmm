package signUp.domain.useCase

import core.response.Response
import signUp.data.repository.RegisterRepositoryImpl
import signUp.domain.model.Register

class RegisterUseCase(private val registerRepositoryImpl: RegisterRepositoryImpl) {
    suspend fun register(register: Register): Response {
        return registerRepositoryImpl.register(register)
    }
}