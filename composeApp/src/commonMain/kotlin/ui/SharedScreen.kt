package ui

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class SharedScreen : ScreenProvider {
    data class ProjectDetailScreen(val projectId: Int) : SharedScreen()
    data class ProjectCreateOrEditScreen(val projectId: Int?) : SharedScreen()
    data class TaskDetailScreen(val taskId: Int) : SharedScreen()
//    data class TaskDetailScreen(val task: Task): SharedScreen()
//    data class TaskCreateOrEditScreen(val task: Task?, val projectId: Int): SharedScreen()
//    data class ProjectAssignScreen(val projectId: Int): SharedScreen()
}