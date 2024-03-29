package projectCreateOrEdit.presentation

import core.model.Project
import core.presentation.LoadingState

data class ProjectCreateOrEditState(
    val loadingState: LoadingState = LoadingState.Init,
    val project: Project = Project(),

    val projectNameError: String? = null,
    val projectDescriptionError: String? = null,
    val projectWorkflowError: String? = null,

    val isSubmit: Boolean = false,
    val isRedirect: Int? = null,
    val submitLoadingState: LoadingState = LoadingState.Init,
)
