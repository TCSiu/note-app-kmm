package taskDetail.presentation

import core.model.Task
import core.presentation.LoadingState

data class TaskDetailState(
    val task: Task? = null,
    val loadingState: LoadingState = LoadingState.Init,

    val taskDetailError: String? = null,
)