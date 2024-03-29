package projectCreateOrEdit.presentation.component.dialog.workflow.rename

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import core.presentation.input.CustomTextField
import kotlinx.coroutines.launch
import newkmmnoteapp.composeapp.generated.resources.Res
import newkmmnoteapp.composeapp.generated.resources.cancel
import newkmmnoteapp.composeapp.generated.resources.confirm
import newkmmnoteapp.composeapp.generated.resources.rename_workflow
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import projectCreateOrEdit.presentation.ProjectCreateOrEditIntent
import projectCreateOrEdit.presentation.ProjectCreateOrEditScreenModel

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ProjectRenameWorkflowDialog(
    projectCreateOrEditScreenModel: ProjectCreateOrEditScreenModel
) {
    val projectRenameWorkflowState by projectCreateOrEditScreenModel.projectRenameWorkflowState.collectAsState()
    val onEvent = projectCreateOrEditScreenModel::onEvent
    val scope = rememberCoroutineScope()

    if (projectRenameWorkflowState.isShow) {
        AlertDialog(
            onDismissRequest = {
                scope.launch {
                    onEvent(ProjectCreateOrEditIntent.DismissRenameWorkflowDialog)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            onEvent(ProjectCreateOrEditIntent.RenameWorkflow)
                        }
                    }
                ) {
                    Text(text = stringResource(resource = Res.string.confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            onEvent(ProjectCreateOrEditIntent.DismissRenameWorkflowDialog)
                        }
                    }
                ) {
                    Text(text = stringResource(resource = Res.string.cancel), color = Color.Red)
                }
            },
            title = {
                Text(
                    text = stringResource(
                        resource = Res.string.rename_workflow,
                        projectRenameWorkflowState.name
                    )
                )
            },
            text = {
                Column {
                    CustomTextField(
                        value = projectRenameWorkflowState.name,
                        onValueChanged = {
                            scope.launch {
                                onEvent(ProjectCreateOrEditIntent.UpdateRenameWorkflowName(it))
                            }
                        },
                        error = projectRenameWorkflowState.error,
                        placeholder = "Rename Workflow"
                    )
                }
            }
        )
    }
}