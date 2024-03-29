package taskDetail.presentation

sealed interface TaskDetailIntent {
    data class GetTaskDetail(val taskId: Int) : TaskDetailIntent
}