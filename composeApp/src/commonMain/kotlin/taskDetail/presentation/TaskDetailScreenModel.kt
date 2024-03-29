package taskDetail.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import core.presentation.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import taskDetail.domain.useCase.TaskDetailUseCase

class TaskDetailScreenModel : ScreenModel, KoinComponent {
    private val _taskDetailState = MutableStateFlow(TaskDetailState())
    val taskDetailState = _taskDetailState.asStateFlow()

    private val taskDetailUseCase by inject<TaskDetailUseCase>()

    suspend fun onEvent(event: TaskDetailIntent) {
        when (event) {
            is TaskDetailIntent.GetTaskDetail -> {
                getTaskDetail(event.taskId)
            }
        }
    }

    private suspend fun getTaskDetail(taskId: Int) {
        _taskDetailState.update {
            it.copy(
                loadingState = LoadingState.IsLoading
            )
        }
        val taskDetailResponse = taskDetailUseCase(taskId = taskId)
        if (taskDetailResponse.task != null) {
            _taskDetailState.update {
                it.copy(
                    loadingState = LoadingState.IsSuccess,
                    task = taskDetailResponse.task,
                )
            }
        } else if (!taskDetailResponse.taskDetailError.isNullOrBlank()) {
            _taskDetailState.update {
                it.copy(
                    loadingState = LoadingState.IsFail,
                    taskDetailError = taskDetailResponse.taskDetailError,
                )
            }
        }
    }
}