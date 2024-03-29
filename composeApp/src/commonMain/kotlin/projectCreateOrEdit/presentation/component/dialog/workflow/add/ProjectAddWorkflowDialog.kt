package projectCreateOrEdit.presentation.component.dialog.workflow.add

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
import newkmmnoteapp.composeapp.generated.resources.add_workflow
import newkmmnoteapp.composeapp.generated.resources.cancel
import newkmmnoteapp.composeapp.generated.resources.confirm
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import projectCreateOrEdit.presentation.ProjectCreateOrEditIntent
import projectCreateOrEdit.presentation.ProjectCreateOrEditScreenModel

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ProjectAddWorkflowDialog(
    projectCreateOrEditScreenModel: ProjectCreateOrEditScreenModel
) {
    val projectAddWorkflowState by projectCreateOrEditScreenModel.projectAddWorkflowState.collectAsState()
    val onEvent = projectCreateOrEditScreenModel::onEvent
    val scope = rememberCoroutineScope()

    if (projectAddWorkflowState.isShow) {
        AlertDialog(
            onDismissRequest = {
                scope.launch {
                    onEvent(ProjectCreateOrEditIntent.DismissAddWorkflowDialog)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            onEvent(ProjectCreateOrEditIntent.AddWorkflow)
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
                            onEvent(ProjectCreateOrEditIntent.DismissAddWorkflowDialog)
                        }
                    }
                ) {
                    Text(text = stringResource(resource = Res.string.cancel), color = Color.Red)
                }
            },
            title = {
                Text(text = stringResource(resource = Res.string.add_workflow))
            },
            text = {
                Column {
                    CustomTextField(
                        value = projectAddWorkflowState.name,
                        onValueChanged = {
                            scope.launch {
                                onEvent(ProjectCreateOrEditIntent.UpdateAddWorkflowName(it))
                            }
                        },
                        error = projectAddWorkflowState.error,
                        placeholder = "Add Workflow"
                    )
                }
            }
        )
    }
}