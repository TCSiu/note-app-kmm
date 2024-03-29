package home.domain.response

import core.model.Project
import core.response.Response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectsResponseSuccess(
    @SerialName("success")
    override val success: Boolean = false,
    @SerialName("data")
    override val data: List<Project>,
    @SerialName("message")
    override val message: String,
) : Response