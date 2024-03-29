package projectDetail.presentation

import core.model.Project

interface ProjectDetailIntent {
    class GetProjectDetail(val projectId: Int) : ProjectDetailIntent
    class OpenProjectOption(val project: Project) : ProjectDetailIntent
    data object DismissProjectOption : ProjectDetailIntent
    class ChangeWorkflow(val projectId: Int, val workflowUuid: String) : ProjectDetailIntent
}