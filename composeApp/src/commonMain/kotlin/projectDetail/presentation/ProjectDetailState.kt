package projectDetail.presentation

import core.model.Project
import core.model.Task
import core.presentation.LoadingState

data class ProjectDetailState(
    val loadingState: LoadingState = LoadingState.Init,
    val project: Project? = null,
    val tasks: Map<String, List<Task>> = emptyMap(),

    val projectError: String? = null,
    val projectTasksError: String? = null,
)
