package core.response.success

import core.response.Response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenSuccess(
    @SerialName("data")
    override val data: String,
    @SerialName("success")
    override val success: Boolean = false,
    @SerialName("message")
    override val message: String,
) : Response