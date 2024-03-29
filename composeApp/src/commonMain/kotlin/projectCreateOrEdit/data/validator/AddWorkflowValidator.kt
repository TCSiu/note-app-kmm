package projectCreateOrEdit.data.validator

import newkmmnoteapp.composeapp.generated.resources.Res
import newkmmnoteapp.composeapp.generated.resources.workflow_name_exists
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString

object AddWorkflowValidator {

    @OptIn(ExperimentalResourceApi::class)
    suspend fun validateNewWorkflowName(
        workflow: Map<String, String>,
        workflowName: String
    ): ValidationResult {
        var result = ValidationResult()
        val checkExist = workflow.filter {
            it.value == workflowName
        }
        if (checkExist.isNotEmpty()) {
            result = result.copy(
                nameError = getString(Res.string.workflow_name_exists)
            )
        }

        return result
    }

    data class ValidationResult(
        val nameError: String = "",
    )
}