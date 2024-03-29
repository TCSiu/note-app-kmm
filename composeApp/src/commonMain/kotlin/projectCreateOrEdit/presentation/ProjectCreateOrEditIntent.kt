package projectCreateOrEdit.presentation

sealed interface ProjectCreateOrEditIntent {
    class Init(val projectId: Int? = null) : ProjectCreateOrEditIntent
    class OnNameChange(val name: String) : ProjectCreateOrEditIntent
    class OnDescriptionChange(val description: String) : ProjectCreateOrEditIntent
    class UpdateWorkflow(val workflow: Map<String, String>) : ProjectCreateOrEditIntent
    class Submit(val projectId: Int? = null) : ProjectCreateOrEditIntent

    data object AddWorkflow : ProjectCreateOrEditIntent
    data object OpenAddWorkflowDialog : ProjectCreateOrEditIntent
    data object DismissAddWorkflowDialog : ProjectCreateOrEditIntent
    class UpdateAddWorkflowName(val name: String) : ProjectCreateOrEditIntent

    class OpenRenameWorkflowDialog(val workflowId: String, val name: String) :
        ProjectCreateOrEditIntent

    data object DismissRenameWorkflowDialog : ProjectCreateOrEditIntent
    data object RenameWorkflow : ProjectCreateOrEditIntent
    class UpdateRenameWorkflowName(val name: String) : ProjectCreateOrEditIntent

    class OpenDeleteWorkflowDialog(val workflowId: String) : ProjectCreateOrEditIntent
    data object DismissDeleteWorkflowDialog : ProjectCreateOrEditIntent
    data object DeleteWorkflow : ProjectCreateOrEditIntent

    data object FetchTemplateWorkflow : ProjectCreateOrEditIntent
    data object OpenTemplateWorkflowDialog : ProjectCreateOrEditIntent
    data object DismissTemplateWorkflowDialog : ProjectCreateOrEditIntent
    class UpdateTemplateWorkflow(val index: Int) : ProjectCreateOrEditIntent
}