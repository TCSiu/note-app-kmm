package projectDetail.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import core.model.Project
import core.model.Task
import core.presentation.LoadingState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import projectDetail.domain.useCase.ChangeWorkflowUseCase
import projectDetail.domain.useCase.GetProjectDetailUseCase
import projectDetail.presentation.component.bottomSheet.ProjectOptionState

class ProjectDetailScreenModel : ScreenModel, KoinComponent {
    private val _projectDetailState = MutableStateFlow(ProjectDetailState())
    private val _projectOptionState = MutableStateFlow(ProjectOptionState())
    val projectDetailState = _projectDetailState.asStateFlow()
    val projectOptionState = _projectOptionState.asStateFlow()

    private val projectDetailUseCase by inject<GetProjectDetailUseCase>()
    private val changeWorkflowUseCase by inject<ChangeWorkflowUseCase>()

    suspend fun onEvent(event: ProjectDetailIntent) {
        when (event) {
            is ProjectDetailIntent.GetProjectDetail -> {
                getProjectDetail(projectId = event.projectId)
            }

            is ProjectDetailIntent.OpenProjectOption -> {
                updateProjectOption(isShow = true, project = event.project)
            }

            ProjectDetailIntent.DismissProjectOption -> {
                updateProjectOption(isShow = false)
            }

            is ProjectDetailIntent.ChangeWorkflow -> {
                changeWorkflow(projectId = event.projectId, workflowUuid = event.workflowUuid)
            }
        }
    }

    private suspend fun getProjectDetail(projectId: Int) {
        screenModelScope.launch {
            _projectDetailState.update {
                it.copy(
                    loadingState = LoadingState.IsLoading
                )
            }
            delay(1000L)
            val response = projectDetailUseCase(projectId = projectId)
            if (response.projectDetailError != null || response.projectTasksError != null) {
                _projectDetailState.update {
                    it.copy(
                        loadingState = LoadingState.IsFail
                    )
                }
                response.projectDetailError?.let {
                    updateProjectDetailState(projectError = it)
                }
                response.projectTasksError?.let {
                    updateProjectDetailState(projectTasksError = it)
                }
            } else {
                response.projectDetail?.let {
                    updateProjectDetailState(project = it)
                }
                response.projectTasks?.let {
                    updateProjectDetailState(tasks = it)
                }
                _projectDetailState.update {
                    it.copy(
                        loadingState = LoadingState.IsSuccess
                    )
                }
            }
        }
    }

    private fun updateProjectDetailState(
        project: Project? = null,
        tasks: Map<String, List<Task>>? = null,
        projectError: String? = null,
        projectTasksError: String? = null
    ) {
        _projectDetailState.update {
            it.copy(
                project = project ?: it.project,
                tasks = tasks ?: it.tasks,
                projectError = projectError ?: it.projectError,
                projectTasksError = projectTasksError ?: it.projectTasksError,
            )
        }
    }

    private fun updateProjectOption(isShow: Boolean? = null, project: Project? = null) {
        _projectOptionState.update {
            it.copy(
                isShow = isShow ?: it.isShow,
                project = project ?: it.project
            )
        }
    }

    private fun changeWorkflow(projectId: Int, workflowUuid: String? = null) {
        screenModelScope.launch {
            val result = changeWorkflowUseCase(projectId = projectId, workflowUuid = workflowUuid)
            if (result.projectTasksError == null) {
                _projectDetailState.update {
                    it.copy(
                        tasks = result.projectTasks ?: emptyMap()
                    )
                }
            } else {
                _projectDetailState.update {
                    it.copy(
                        projectTasksError = result.projectDetailError
                    )
                }
            }
        }
    }
}