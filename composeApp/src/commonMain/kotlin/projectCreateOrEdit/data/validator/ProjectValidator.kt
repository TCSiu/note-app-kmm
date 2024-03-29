package projectCreateOrEdit.data.validator

import newkmmnoteapp.composeapp.generated.resources.Res
import newkmmnoteapp.composeapp.generated.resources.missing_project_description
import newkmmnoteapp.composeapp.generated.resources.missing_project_name
import newkmmnoteapp.composeapp.generated.resources.missing_project_workflow
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString
import projectCreateOrEdit.domain.model.ProjectCreateOrEditRequest

object ProjectValidator {

    @OptIn(ExperimentalResourceApi::class)
    suspend fun validateProject(projectCreateOrEditRequest: ProjectCreateOrEditRequest): ValidationResult {
        var result = ValidationResult()
        if (projectCreateOrEditRequest.name.isNullOrBlank()) {
            result = result.copy(
                nameError = getString(resource = Res.string.missing_project_name)
            )
        }
        if (projectCreateOrEditRequest.description.isNullOrBlank()) {
            result = result.copy(
                descriptionError = getString(resource = Res.string.missing_project_description)
            )
        }
        if (projectCreateOrEditRequest.workflow.isEmpty()) {
            result = result.copy(
                workflowError = getString(resource = Res.string.missing_project_workflow)
            )
        }
        return result
    }

    data class ValidationResult(
        val nameError: String? = null,
        val descriptionError: String? = null,
        val workflowError: String? = null,
    )
}