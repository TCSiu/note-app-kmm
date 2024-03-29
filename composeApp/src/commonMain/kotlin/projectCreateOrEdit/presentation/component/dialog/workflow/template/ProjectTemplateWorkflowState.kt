package projectCreateOrEdit.presentation.component.dialog.workflow.template

import core.model.Workflow
import core.presentation.LoadingState

data class ProjectTemplateWorkflowState(
    val isShow: Boolean = false,
    val workflowList: List<Workflow>? = null,

    val loadingState: LoadingState = LoadingState.Init,
)
