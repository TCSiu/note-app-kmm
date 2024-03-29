package core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: Int = 0,
    val uuid: String = "",
    val name: String = "",
    val description: String = "",
    @SerialName("project_uuid")
    val projectUuid: String = "",
    @SerialName("workflow_uuid")
    val workflowUuid: String = "",
    @SerialName("created_by")
    val createdBy: Int? = null,
    @SerialName("created_at")
    val createdAt: String = "",
    @SerialName("updated_by")
    val updatedBy: Int? = null,
    @SerialName("updated_at")
    val updatedAt: String = "",
    @SerialName("deleted_by")
    val deletedBy: Int? = null,
    @SerialName("deleted_at")
    val deletedAt: String? = null,
    val users: List<User> = emptyList()
)

