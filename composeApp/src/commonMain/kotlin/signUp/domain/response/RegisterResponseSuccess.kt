package signUp.domain.response

import core.model.User
import core.response.Response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponseSuccess(
    override val success: Boolean,
    override val data: RegisterResponseSuccessData,
    override val message: String,
) : Response

@Serializable
data class RegisterResponseSuccessData(
    val token: String = "",
    @SerialName("token_type")
    val tokenType: String = "",
    @SerialName("expires_in")
    val expiresIn: Long = -1,
    val user: User = User(),
)