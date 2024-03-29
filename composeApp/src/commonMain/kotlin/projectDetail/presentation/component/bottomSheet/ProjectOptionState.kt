package projectDetail.presentation.component.bottomSheet

import core.model.Project

data class ProjectOptionState(
    val isShow: Boolean = false,
    val project: Project? = null,
)
