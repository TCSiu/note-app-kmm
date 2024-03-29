package core.response.fail

import core.response.Response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseFail(
    @SerialName("success")
    override val success: Boolean = false,
    @SerialName("data")
    override val data: ResponseFailData,
    @SerialName("message")
    override val message: String,
) : Response

@Serializable
data class ResponseFailData(
    val error: String? = null,

    @SerialName("user_id")
    val userId: List<String> = emptyList(),
    val email: List<String> = emptyList(),
    val permission: List<String> = emptyList(),

    val name: List<String> = emptyList(),
    val description: List<String> = emptyList(),
    val workflow: List<String> = emptyList(),

    val delete: List<String> = emptyList(),
    val remove: List<String> = emptyList()
)
