package projectCreateOrEdit.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectCreateOrEditRequest(
    @SerialName("_method")
    val method: String = "put",
    @SerialName("name")
    val name: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("workflow")
    val workflow: Map<String, String> = emptyMap()
)

@Serializable
data class ProjectEditModifiedRequest(
    @SerialName("_method")
    val method: String = "put",
    @SerialName("name")
    val name: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("workflow")
    val workflow: String = "{}"
)
