package core.di

import cafe.adriel.voyager.core.registry.screenModule
import ui.SharedScreen
import ui.projectCreateOrEdit.ProjectCreateOrEditScreen
import ui.projectDetail.ProjectDetailScreen
import ui.taskDetail.TaskDetailScreen

val sharedScreenModule = screenModule {
    register<SharedScreen.ProjectDetailScreen> {
        ProjectDetailScreen(projectId = it.projectId)
    }
    register<SharedScreen.ProjectCreateOrEditScreen> {
        ProjectCreateOrEditScreen(projectId = it.projectId)
    }
    register<SharedScreen.TaskDetailScreen> {
        TaskDetailScreen(taskId = it.taskId)
    }
//    register<SharedScreen.TaskDetailScreen> {
//        TaskDetailScreen(task = it.task)
//    }
//    register<SharedScreen.TaskCreateOrEditScreen> {
//        TaskCreateOrEditScreen(task = it.task, projectId = it.projectId)
//    }
//    register<SharedScreen.ProjectAssignScreen> {
//        ProjectAssignScreen(projectId = it.projectId)
//    }
}