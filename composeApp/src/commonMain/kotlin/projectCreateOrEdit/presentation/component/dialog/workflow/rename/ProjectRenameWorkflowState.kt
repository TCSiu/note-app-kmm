package projectCreateOrEdit.presentation.component.dialog.workflow.rename

data class ProjectRenameWorkflowState(
    val isShow: Boolean = false,
    val workflowId: String? = null,
    val name: String = "",
    val error: String = "",
    val numberOfCustomWorkflow: Int = 0,
)
