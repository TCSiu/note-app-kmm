package projectCreateOrEdit.presentation.component.dialog.workflow.template

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import core.model.Workflow
import core.presentation.Loading
import core.presentation.LoadingState
import core.presentation.expandedList.ExpandedList
import kotlinx.coroutines.launch
import projectCreateOrEdit.presentation.ProjectCreateOrEditIntent
import projectCreateOrEdit.presentation.ProjectCreateOrEditScreenModel

@Composable
fun ProjectWorkflowTemplateDialog(
    projectCreateOrEditScreenModel: ProjectCreateOrEditScreenModel
) {
    val projectTemplateWorkflowState by projectCreateOrEditScreenModel.projectTemplateWorkflowState.collectAsState()
    val loadingState = projectTemplateWorkflowState.loadingState
    var workflowTemplate: List<Workflow> = emptyList()

    val onEvent = projectCreateOrEditScreenModel::onEvent
    val scope = rememberCoroutineScope()

    val selectedWorkflowTemplateIndex = remember {
        mutableStateOf<Int?>(null)
    }

    LaunchedEffect(projectTemplateWorkflowState) {
        if (projectTemplateWorkflowState.isShow) {
            when (loadingState) {
                is LoadingState.Init -> {
                    onEvent(ProjectCreateOrEditIntent.FetchTemplateWorkflow)
                }

                else -> {}
            }
        }
    }

    if (projectTemplateWorkflowState.isShow) {
        Dialog(
            onDismissRequest = {
                scope.launch {
                    onEvent(ProjectCreateOrEditIntent.DismissTemplateWorkflowDialog)
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            ),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Text(
                            text = "Select a workflow template",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    when (loadingState) {
                        is LoadingState.IsLoading -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Loading()
                            }
                        }

                        is LoadingState.IsSuccess -> {
                            workflowTemplate =
                                projectTemplateWorkflowState.workflowList ?: emptyList()

                            ExpandedList(
                                dataList = workflowTemplate,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                selectedIndex = selectedWorkflowTemplateIndex
                            )
                        }

                        else -> {}
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                    ) {
                        TextButton(
                            onClick = {
                                scope.launch {
                                    onEvent(ProjectCreateOrEditIntent.DismissTemplateWorkflowDialog)
                                }
                            }
                        ) {
                            Text(text = "Cancel", color = Color.Red)
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        TextButton(
                            onClick = {
                                scope.launch {
                                    onEvent(
                                        ProjectCreateOrEditIntent.UpdateTemplateWorkflow(
                                            selectedWorkflowTemplateIndex.value!!
                                        )
                                    )
                                }
                            }
                        ) {
                            Text(text = "Confirm")
                        }
                    }
                }
            }
        }
    }
}