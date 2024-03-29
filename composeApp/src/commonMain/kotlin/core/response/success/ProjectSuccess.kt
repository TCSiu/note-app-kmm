package core.response.success

import core.model.Project
import core.response.Response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectSuccess(
    @SerialName("success")
    override val success: Boolean = false,
    @SerialName("data")
    override val data: Project,
    @SerialName("message")
    override val message: String,
) : Response
