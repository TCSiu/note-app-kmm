package signUp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Register(
    val email: String = "",
    val name: String = "",
    val password: String = "",
    @SerialName("password_confirmation")
    val passwordConfirmation: String = "",
)
