package projectCreateOrEdit.presentation.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import core.presentation.input.CustomTextField
import core.theme.SPACER_HEIGHT
import kotlinx.coroutines.launch
import projectCreateOrEdit.presentation.ProjectCreateOrEditIntent
import projectCreateOrEdit.presentation.ProjectCreateOrEditScreenModel

@Composable
fun ProjectGeneralField(
    columnScope: ColumnScope,
    projectId: Int? = null,
    projectCreateOrEditScreenModel: ProjectCreateOrEditScreenModel
) {
    val projectCreateOrEditState by projectCreateOrEditScreenModel.projectCreateOrEditState.collectAsState()
    val projectCreateOrEditRequest = projectCreateOrEditScreenModel.projectCreateOrEditRequest
    val onEvent = projectCreateOrEditScreenModel::onEvent
    val scope = rememberCoroutineScope()

    columnScope.CustomTextField(
        title = "Project Name",
        value = projectCreateOrEditRequest.name ?: "",
        onValueChanged = { event ->
            scope.launch {
                onEvent(ProjectCreateOrEditIntent.OnNameChange(event))
            }
        },
        error = projectCreateOrEditState.projectNameError,
    )
    Spacer(modifier = Modifier.height(SPACER_HEIGHT))
    columnScope.CustomTextField(
        title = "Project Description",
        value = projectCreateOrEditRequest.description ?: "",
        onValueChanged = { event ->
            scope.launch {
                onEvent(ProjectCreateOrEditIntent.OnDescriptionChange(event))
            }
        },
        error = projectCreateOrEditState.projectDescriptionError,
        isSingleLine = false,
        minLines = 10,
        maxLines = 10,
    )
}