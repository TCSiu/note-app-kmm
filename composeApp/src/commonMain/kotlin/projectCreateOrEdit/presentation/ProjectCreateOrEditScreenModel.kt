package projectCreateOrEdit.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import core.model.Project
import core.model.Workflow
import core.presentation.LoadingState
import core.response.fail.ResponseFail
import core.response.success.ProjectSuccess
import core.response.success.WorkflowGetTemplateSuccess
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import projectCreateOrEdit.data.validator.AddWorkflowValidator
import projectCreateOrEdit.data.validator.ProjectValidator
import projectCreateOrEdit.data.validator.RenameWorkflowValidator
import projectCreateOrEdit.domain.model.ProjectCreateOrEditRequest
import projectCreateOrEdit.domain.useCase.ProjectUseCase
import projectCreateOrEdit.presentation.component.dialog.workflow.add.ProjectAddWorkflowState
import projectCreateOrEdit.presentation.component.dialog.workflow.delete.ProjectDeleteWorkflowState
import projectCreateOrEdit.presentation.component.dialog.workflow.rename.ProjectRenameWorkflowState
import projectCreateOrEdit.presentation.component.dialog.workflow.template.ProjectTemplateWorkflowState

@OptIn(ExperimentalResourceApi::class)
class ProjectCreateOrEditScreenModel : ScreenModel, KoinComponent {
    private val _projectCreateOrEditState = MutableStateFlow(ProjectCreateOrEditState())
    private val _projectAddWorkflowState = MutableStateFlow(ProjectAddWorkflowState())
    private val _projectRenameWorkflowState = MutableStateFlow(ProjectRenameWorkflowState())
    private val _projectDeleteWorkflowState = MutableStateFlow(ProjectDeleteWorkflowState())
    private val _projectTemplateWorkflowState = MutableStateFlow(ProjectTemplateWorkflowState())
    val projectCreateOrEditState = _projectCreateOrEditState.asStateFlow()
    val projectAddWorkflowState = _projectAddWorkflowState.asStateFlow()
    val projectRenameWorkflowState = _projectRenameWorkflowState.asStateFlow()
    val projectDeleteWorkflowState = _projectDeleteWorkflowState.asStateFlow()
    val projectTemplateWorkflowState = _projectTemplateWorkflowState.asStateFlow()

    var projectCreateOrEditRequest by mutableStateOf(ProjectCreateOrEditRequest())


    private val projectUseCase by inject<ProjectUseCase>()

    suspend fun onEvent(event: ProjectCreateOrEditIntent) {
        when (event) {
            is ProjectCreateOrEditIntent.OnDescriptionChange -> {
                updateRequest(description = event.description)
            }

            is ProjectCreateOrEditIntent.OnNameChange -> {
                updateRequest(name = event.name)
            }

            is ProjectCreateOrEditIntent.AddWorkflow -> {
                val name = projectAddWorkflowState.value.name
                val result = AddWorkflowValidator.validateNewWorkflowName(
                    workflow = projectCreateOrEditRequest.workflow,
                    workflowName = name
                )
                if (result.nameError.isNotBlank()) {
                    updateAddWorkflowState(error = result.nameError)
                } else {
                    addWorkflow(name)
                    resetAddWorkflowState(isShow = false)
                }
            }

            is ProjectCreateOrEditIntent.UpdateWorkflow -> {
                updateWorkflow(event.workflow)
            }

            is ProjectCreateOrEditIntent.Init -> {
                screenModelScope.launch {
                    _projectCreateOrEditState.update {
                        it.copy(
                            loadingState = LoadingState.IsLoading
                        )
                    }
                    delay(1000L)
                    if (event.projectId != null) {
                        when (val response = projectUseCase.getProject(event.projectId)) {
                            is ProjectSuccess -> {
                                initProject(response.data)
                            }

                            else -> {}
                        }
                    } else {
                        initProject()
                    }
                    _projectCreateOrEditState.update {
                        it.copy(
                            loadingState = LoadingState.IsSuccess
                        )
                    }
                    resetAddWorkflowState(resetWorkflowNumber = true)
                }
            }

            is ProjectCreateOrEditIntent.Submit -> {
                screenModelScope.launch {
                    val result = ProjectValidator.validateProject(projectCreateOrEditRequest)
                    val errors = listOfNotNull(
                        result.nameError,
                        result.descriptionError,
                        result.workflowError
                    )
                    if (errors.isEmpty()) {
                        var isSuccess: Int? = null
                        _projectCreateOrEditState.update {
                            it.copy(
                                submitLoadingState = LoadingState.IsLoading,
                                isSubmit = true
                            )
                        }
                        delay(1000L)
                        when (val response = projectUseCase.storeProject(
                            projectId = event.projectId,
                            projectCreateOrEditRequest = projectCreateOrEditRequest
                        )) {
                            is ProjectSuccess -> {
                                _projectCreateOrEditState.update { it.copy(submitLoadingState = LoadingState.IsSuccess) }
                                isSuccess = response.data.id
                            }

                            is ResponseFail -> {
                                _projectCreateOrEditState.update { it.copy(submitLoadingState = LoadingState.IsFail) }
                            }
                        }
                        delay(1000L)
                        _projectCreateOrEditState.update {
                            it.copy(
                                submitLoadingState = LoadingState.Init,
                                isSubmit = false,
                                isRedirect = isSuccess
                            )
                        }
                    }
                }
            }
            /* add workflow */
            ProjectCreateOrEditIntent.OpenAddWorkflowDialog -> {
                resetAddWorkflowState(isShow = true)
            }

            ProjectCreateOrEditIntent.DismissAddWorkflowDialog -> {
                resetAddWorkflowState(isShow = false)
            }

            is ProjectCreateOrEditIntent.UpdateAddWorkflowName -> {
                updateAddWorkflowState(name = event.name)
            }
            /* rename workflow */
            is ProjectCreateOrEditIntent.OpenRenameWorkflowDialog -> {
                updateRenameWorkflowState(
                    isShow = true,
                    workflowId = event.workflowId,
                    name = event.name
                )
            }

            ProjectCreateOrEditIntent.DismissRenameWorkflowDialog -> {
                resetRenameWorkflowState(isShow = false)
            }

            ProjectCreateOrEditIntent.RenameWorkflow -> {
                val name = _projectRenameWorkflowState.value.name
                val workflowId: String = _projectRenameWorkflowState.value.workflowId ?: ""
                val result = RenameWorkflowValidator.validateNewWorkflowName(
                    workflow = projectCreateOrEditRequest.workflow,
                    workflowName = name,
                    workflowId = workflowId
                )
                if (result.nameError.isNotBlank()) {
                    updateRenameWorkflowState(error = result.nameError)
                } else {
                    renameWorkflow(workflowId = workflowId, input = name)
                    resetRenameWorkflowState(isShow = false)
                }
            }

            is ProjectCreateOrEditIntent.UpdateRenameWorkflowName -> {
                updateRenameWorkflowState(name = event.name)
            }
            /* delete workflow */
            ProjectCreateOrEditIntent.DeleteWorkflow -> {
                deleteWorkflow()
                resetDeleteWorkflowState(isShow = false)
            }

            is ProjectCreateOrEditIntent.OpenDeleteWorkflowDialog -> {
                updateDeleteWorkflowState(isShow = true, workflowId = event.workflowId)
            }

            ProjectCreateOrEditIntent.DismissDeleteWorkflowDialog -> {
                resetDeleteWorkflowState(isShow = false)
            }

            /* template workflow */
            ProjectCreateOrEditIntent.FetchTemplateWorkflow -> {
                screenModelScope.launch {
                    _projectTemplateWorkflowState.update {
                        it.copy(
                            loadingState = LoadingState.IsLoading
                        )
                    }
                    when (val response = projectUseCase.getTemplateWorkflow()) {
                        is WorkflowGetTemplateSuccess -> {
                            updateTemplateWorkflowState(workflowList = response.data)
                            println(projectTemplateWorkflowState.value)
                        }

                        else -> {}
                    }
                    _projectTemplateWorkflowState.update {
                        it.copy(
                            loadingState = LoadingState.IsSuccess
                        )
                    }
                }
            }

            ProjectCreateOrEditIntent.OpenTemplateWorkflowDialog -> {
                _projectTemplateWorkflowState.update {
                    it.copy(
                        loadingState = LoadingState.Init
                    )
                }
                updateTemplateWorkflowState(isShow = true)
            }

            ProjectCreateOrEditIntent.DismissTemplateWorkflowDialog -> {
                updateTemplateWorkflowState(isShow = false)
            }

            is ProjectCreateOrEditIntent.UpdateTemplateWorkflow -> {
                updateTemplateWorkflow(index = event.index)
                resetTemplateWorkflowState(isShow = false)
            }

            else -> {}
        }
    }

    private fun initProject(project: Project? = null) {
        if (project != null) {
            val decodedWorkflow =
                Json.decodeFromString<Map<String, String>>(project.workflow ?: "{}")
            updateRequest(name = project.name, description = project.description)
            updateWorkflow(decodedWorkflow)
        } else {
            updateRequest(method = "post", name = null, description = null)
        }
        _projectAddWorkflowState.update { it.copy(numberOfCustomWorkflow = 0) }
    }

    private fun updateRequest(
        method: String? = null,
        name: String? = null,
        description: String? = null
    ) {
        projectCreateOrEditRequest = projectCreateOrEditRequest.copy(
            method = method ?: projectCreateOrEditRequest.method,
            name = name ?: projectCreateOrEditRequest.name,
            description = description ?: projectCreateOrEditRequest.description,
        )
    }

    private fun updateWorkflow(updatedWorkflow: Map<String, String>) {
        projectCreateOrEditRequest = projectCreateOrEditRequest.copy(
            workflow = emptyMap()
        )
        projectCreateOrEditRequest = projectCreateOrEditRequest.copy(
            workflow = updatedWorkflow
        )
    }

    /* add workflow */
    private fun addWorkflow(input: String) {
        val numberOfCustomWorkflow = _projectAddWorkflowState.value.numberOfCustomWorkflow
        val currentWorkflow = projectCreateOrEditRequest.workflow.toMutableMap()
        currentWorkflow[numberOfCustomWorkflow.toString()] = input
        updateWorkflow(currentWorkflow)
        updateAddWorkflowState(resetWorkflowNumber = false)
    }

    private fun updateAddWorkflowState(
        isShow: Boolean? = null,
        name: String? = null,
        error: String? = null,
        resetWorkflowNumber: Boolean = false
    ) {
        _projectAddWorkflowState.update {
            it.copy(
                isShow = isShow ?: it.isShow,
                name = name ?: it.name,
                error = error ?: it.error,
                numberOfCustomWorkflow = if (resetWorkflowNumber) it.numberOfCustomWorkflow else it.numberOfCustomWorkflow + 1
            )
        }
    }

    private fun resetAddWorkflowState(
        isShow: Boolean? = null,
        resetWorkflowNumber: Boolean = false
    ) {
        _projectAddWorkflowState.update {
            it.copy(
                isShow = isShow ?: it.isShow,
                name = "",
                error = "",
                numberOfCustomWorkflow = if (resetWorkflowNumber) 0 else it.numberOfCustomWorkflow
            )
        }
    }

    /* rename workflow */
    private fun renameWorkflow(workflowId: String, input: String) {
        val currentWorkflow = projectCreateOrEditRequest.workflow.toMutableMap()
        currentWorkflow[workflowId] = input
        updateWorkflow(currentWorkflow)
    }

    private fun updateRenameWorkflowState(
        isShow: Boolean? = null,
        workflowId: String? = null,
        name: String? = null,
        error: String? = null
    ) {
        _projectRenameWorkflowState.update {
            it.copy(
                isShow = isShow ?: it.isShow,
                workflowId = workflowId ?: it.workflowId,
                name = name ?: it.name,
                error = error ?: it.error
            )
        }
    }

    private fun resetRenameWorkflowState(isShow: Boolean? = null) {
        _projectRenameWorkflowState.update {
            it.copy(
                isShow = isShow ?: it.isShow,
                workflowId = null,
                name = "",
                error = "",
            )
        }
    }

    /* delete workflow */
    private fun deleteWorkflow() {
        val workflow = projectCreateOrEditRequest.workflow.toMutableMap()
        val workflowId: String = _projectDeleteWorkflowState.value.workflowId ?: ""
        workflow.remove(workflowId)
        updateWorkflow(workflow)
    }

    private fun updateDeleteWorkflowState(isShow: Boolean? = null, workflowId: String? = null) {
        _projectDeleteWorkflowState.update {
            it.copy(
                isShow = isShow ?: it.isShow,
                workflowId = workflowId ?: it.workflowId
            )
        }
    }

    private fun resetDeleteWorkflowState(isShow: Boolean? = null) {
        _projectDeleteWorkflowState.update {
            it.copy(
                isShow = isShow ?: it.isShow,
                workflowId = null
            )
        }
    }

    /* template workflow */
    private fun updateTemplateWorkflow(index: Int) {
        _projectTemplateWorkflowState.value.workflowList?.let {
            val workflowList = _projectTemplateWorkflowState.value.workflowList
            val selectedWorkflow = workflowList?.get(index)
            if (selectedWorkflow != null) {
                val workflowTemplate =
                    Json.decodeFromString<List<String>>(selectedWorkflow.workflow ?: "[]")
                val workflowMap =
                    workflowTemplate.mapIndexed { index: Int, s: String -> index.toString() to s }
                        .toMap()
                updateWorkflow(workflowMap)
            }
        }
    }

    private fun updateTemplateWorkflowState(
        isShow: Boolean? = null,
        workflowList: List<Workflow>? = null
    ) {
        _projectTemplateWorkflowState.update {
            it.copy(
                isShow = isShow ?: it.isShow,
                workflowList = workflowList ?: it.workflowList
            )
        }
    }

    private fun resetTemplateWorkflowState(isShow: Boolean? = null) {
        _projectTemplateWorkflowState.update {
            it.copy(
                isShow = isShow ?: it.isShow,
                workflowList = null
            )
        }
    }

}
