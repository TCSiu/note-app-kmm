package projectCreateOrEdit.presentation.component.dialog.workflow.delete

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties
import core.theme.STANDARD_LAYOUT_PADDING
import kotlinx.coroutines.launch
import newkmmnoteapp.composeapp.generated.resources.Res
import newkmmnoteapp.composeapp.generated.resources.cancel
import newkmmnoteapp.composeapp.generated.resources.delete
import newkmmnoteapp.composeapp.generated.resources.workflow_delete_confirm
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import projectCreateOrEdit.presentation.ProjectCreateOrEditIntent
import projectCreateOrEdit.presentation.ProjectCreateOrEditScreenModel

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ProjectDeleteWorkflowDialog(
    projectCreateOrEditScreenModel: ProjectCreateOrEditScreenModel
) {
    val projectDeleteWorkflowState by projectCreateOrEditScreenModel.projectDeleteWorkflowState.collectAsState()
    val projectCreateOrEditRequest = projectCreateOrEditScreenModel.projectCreateOrEditRequest
    val workflow = projectCreateOrEditRequest.workflow.toMutableMap()
    val workflowId = projectDeleteWorkflowState.workflowId
    val onEvent = projectCreateOrEditScreenModel::onEvent

    val scope = rememberCoroutineScope()

    if (projectDeleteWorkflowState.isShow) {
        AlertDialog(
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = STANDARD_LAYOUT_PADDING),
            onDismissRequest = {
                scope.launch {
                    onEvent(ProjectCreateOrEditIntent.DismissDeleteWorkflowDialog)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            onEvent(ProjectCreateOrEditIntent.DeleteWorkflow)
                        }
                    }
                ) {
                    Text(text = stringResource(resource = Res.string.delete))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            onEvent(ProjectCreateOrEditIntent.DismissDeleteWorkflowDialog)
                        }
                    }
                ) {
                    Text(text = stringResource(resource = Res.string.cancel), color = Color.Red)
                }
            },
            title = {
                Text(
                    text = stringResource(
                        resource = Res.string.workflow_delete_confirm,
                        workflow[workflowId] ?: ""
                    )
                )
            },
        )
    }
}