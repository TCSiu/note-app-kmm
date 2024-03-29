package core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("uuid")
    val uuid: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("description")
    val description: String = "",
    @SerialName("status")
    val status: Int = 0,
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
    @SerialName("creator")
    val creator: User? = null,
    @SerialName("users")
    val users: List<User> = emptyList(),
    @SerialName("workflow")
    val workflow: String? = null,
    @SerialName("workflow_template")
    val workflowTemplate: WorkflowTemplate? = null,
    val canEdit: Boolean = false,
)

@Serializable
data class WorkflowTemplate(
    val id: Int = 0,
    val uuid: String = "",
    val workflow: String = "",
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
)

//@Serializable
//data class ProjectDetail(
//    @SerialName("assign")
//    val assignTaskList: List<Task> = emptyList(),
//    @SerialName("unAssign")
//    val unAssignTaskList: List<Task> = emptyList(),
//    @SerialName("canEdit")
//    val canEdit: Boolean = false,
//)
