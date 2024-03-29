package projectDetail.data.result

import core.model.Project
import core.model.Task

data class ProjectDetailResult(
    var projectDetail: Project? = null,
    var projectTasks: Map<String, List<Task>>? = null,

    var projectDetailError: String? = null,
    var projectTasksError: String? = null,
)