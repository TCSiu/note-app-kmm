package signUp.domain.response

import core.response.Response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponseFail(
    @SerialName("success")
    override val success: Boolean,
    @SerialName("data")
    override val data: RegisterResponseFailData,
    @SerialName("message")
    override val message: String,
) : Response

@Serializable
data class RegisterResponseFailData(
    val error: String? = null,
    val email: List<String>? = null,
    val name: List<String>? = null,
    val password: List<String>? = null,
    @SerialName("password_confirmation")
    val passwordConfirmation: List<String>? = null,
)