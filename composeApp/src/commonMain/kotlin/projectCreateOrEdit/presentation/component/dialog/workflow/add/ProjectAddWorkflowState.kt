package projectCreateOrEdit.presentation.component.dialog.workflow.add

data class ProjectAddWorkflowState(
    val isShow: Boolean = false,
    val name: String = "",
    val error: String = "",
    val numberOfCustomWorkflow: Int = 0,
)
