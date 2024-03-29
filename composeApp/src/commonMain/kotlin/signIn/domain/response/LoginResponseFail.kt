package signIn.domain.response

import core.response.Response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseFail(
    @SerialName("success")
    override val success: Boolean,
    @SerialName("data")
    override val data: LoginResponseFailData,
    @SerialName("message")
    override val message: String,
) : Response

@Serializable
data class LoginResponseFailData(
    val error: String? = null,
    val email: List<String>? = null,
    val password: List<String>? = null,
)

