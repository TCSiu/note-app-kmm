package taskDetail.data.result

import core.model.Task

data class TaskDetailResult(
    var task: Task? = null,
    var taskDetailError: String? = null,
)
