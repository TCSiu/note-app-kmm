package projectCreateOrEdit.data.validator

import newkmmnoteapp.composeapp.generated.resources.Res
import newkmmnoteapp.composeapp.generated.resources.workflow_key_exists
import newkmmnoteapp.composeapp.generated.resources.workflow_name_exists
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString

object RenameWorkflowValidator {

    @OptIn(ExperimentalResourceApi::class)
    suspend fun validateNewWorkflowName(
        workflow: Map<String, String>,
        workflowName: String,
        workflowId: String?
    ): ValidationResult {
        var result = ValidationResult()
        val checkValueExist = workflow.filter {
            it.value == workflowName
        }
        val checkKeyExist = workflow.filter {
            it.key == workflowId
        }
        if (checkValueExist.isNotEmpty()) {
            result = result.copy(
                nameError = getString(Res.string.workflow_name_exists)
            )
        }

        if (checkKeyExist.isEmpty()) {
            result = result.copy(
                nameError = getString(Res.string.workflow_key_exists)
            )
        }
        return result
    }

    data class ValidationResult(
        val nameError: String = "",
    )
}