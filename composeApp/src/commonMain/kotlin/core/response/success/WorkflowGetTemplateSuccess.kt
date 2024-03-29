package core.response.success

import core.model.Workflow
import core.response.Response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkflowGetTemplateSuccess(
    @SerialName("success")
    override val success: Boolean = false,
    @SerialName("data")
    override val data: List<Workflow>,
    @SerialName("message")
    override val message: String,
) : Response
