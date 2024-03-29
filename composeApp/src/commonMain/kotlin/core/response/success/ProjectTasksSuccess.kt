package core.response.success

import core.model.Task
import core.response.Response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectTasksSuccess(
    override val success: Boolean = false,
    override val data: Map<String, List<Task>> = emptyMap(),
    override val message: String?,
) : Response

@Serializable
data class ProjectTaskData(
    @SerialName("assigned")
    val assignList: List<Task> = emptyList(),
    @SerialName("notAssign")
    val notAssignList: List<Task> = emptyList(),
    val canEdit: Boolean = false,
)
