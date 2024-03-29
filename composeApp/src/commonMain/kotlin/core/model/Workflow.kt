package core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Workflow(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("uuid")
    val uuid: String? = null,
    @SerialName("workflow")
    val workflow: String? = null,
    @SerialName("created_by")
    val createdBy: Int? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_by")
    val updatedBy: Int? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null,
    @SerialName("deleted_by")
    val deletedBy: Int? = null,
    @SerialName("deleted_at")
    val deletedAt: String? = null,
)
