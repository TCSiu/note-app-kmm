package core.response.success

import core.model.Task
import core.response.Response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskSuccess(
    @SerialName("success")
    override val success: Boolean = false,
    @SerialName("data")
    override val data: Task,
    @SerialName("message")
    override val message: String,
) : Response