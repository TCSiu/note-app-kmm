package projectCreateOrEdit.presentation.component

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import newkmmnoteapp.composeapp.generated.resources.Res
import newkmmnoteapp.composeapp.generated.resources.workflow
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import projectCreateOrEdit.presentation.ProjectCreateOrEditIntent
import projectCreateOrEdit.presentation.ProjectCreateOrEditScreenModel
import projectCreateOrEdit.presentation.ProjectCreateOrEditState
import kotlin.math.sign

@Composable
fun ProjectWorkflow(
    projectId: Int? = null,
    projectCreateOrEditScreenModel: ProjectCreateOrEditScreenModel
) {
    val projectCreateOrEditState by projectCreateOrEditScreenModel.projectCreateOrEditState.collectAsState()

    ProjectWorkflowHeader(projectId, projectCreateOrEditScreenModel)
    ProjectWorkflowError(projectCreateOrEditState)
    ProjectWorkflowList(
        projectId = projectId,
        projectCreateOrEditScreenModel = projectCreateOrEditScreenModel
    )
}

@Composable
fun ProjectWorkflowList(
    projectId: Int? = null,
    projectCreateOrEditScreenModel: ProjectCreateOrEditScreenModel
) {
//    val workflow by projectCreateOrEditScreenModel.workflow.collectAsState()
    val projectCreateOrEditRequest = projectCreateOrEditScreenModel.projectCreateOrEditRequest
    val workflow = projectCreateOrEditRequest.workflow.toList().toMutableList()
    val scope = rememberCoroutineScope()

    val onEvent = projectCreateOrEditScreenModel::onEvent

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(workflow.size) { index ->
            var isDragged by remember { mutableStateOf(false) }
            var offsetY by remember { mutableFloatStateOf(0f) }
            var itemIndexChange by remember { mutableIntStateOf(0) }
            var newIndex by remember { mutableIntStateOf(0) }
            val zIndex = if (isDragged) 1.0f else 0.0f
            ListItem(
                headlineContent = {
                    Text(text = workflow[index].second)
                },
                leadingContent = {
                    Icon(imageVector = Icons.Outlined.Menu, contentDescription = "Drag to Reorder")
                },
                trailingContent = {
                    Row {
                        if (projectId == null) {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        onEvent(
                                            ProjectCreateOrEditIntent.OpenDeleteWorkflowDialog(
                                                workflow[index].first
                                            )
                                        )
                                    }
                                },
                                enabled = workflow.size > 1
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "Remove"
                                )
                            }
                        }
                        IconButton(
                            onClick = {
                                scope.launch {
                                    onEvent(
                                        ProjectCreateOrEditIntent.OpenRenameWorkflowDialog(
                                            workflowId = workflow[index].first,
                                            name = workflow[index].second
                                        )
                                    )
                                }
                            },
                        ) {
                            Icon(imageVector = Icons.Filled.Edit, contentDescription = "Rename")
                        }
                    }
                },
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(
                            onDrag = { _: PointerInputChange, dragAmount: Offset ->
                                offsetY += dragAmount.y
                            },
                            onDragStart = {
                                isDragged = true
                            },
                            onDragEnd = {
                                isDragged = false
                                itemIndexChange =
                                    offsetY.toInt() / this.size.height
                                newIndex = when {
                                    itemIndexChange.sign > 0 -> if ((index + itemIndexChange) > workflow.size - 1) workflow.size - 1 else (index + itemIndexChange)
                                    itemIndexChange.sign < 0 -> if ((index + itemIndexChange) < 0) 0 else (index + itemIndexChange)
                                    else -> index
                                }
                                offsetY = 0f
                                val item = workflow[index]
                                workflow.removeAt(index)
                                workflow.add(index = newIndex, element = item)
                                scope.launch {
                                    onEvent(ProjectCreateOrEditIntent.UpdateWorkflow(workflow.toMap()))
                                }
                            },
                            onDragCancel = {
                                isDragged = false
                                offsetY = 0f
                            },
                        )
                    }
                    .offset { IntOffset(0, offsetY.toInt()) }
                    .zIndex(zIndex = zIndex)
                    .alpha(alpha = if (isDragged) 0.5f else 1f),
            )
            HorizontalDivider(thickness = 2.dp)
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ProjectWorkflowHeader(
    projectId: Int? = null,
    projectCreateOrEditScreenModel: ProjectCreateOrEditScreenModel
) {
    val onEvent = projectCreateOrEditScreenModel::onEvent
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(resource = Res.string.workflow),
            modifier = Modifier,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Row {
            if (projectId == null) {
                IconButton(
                    onClick = {
                        scope.launch {
                            onEvent(ProjectCreateOrEditIntent.OpenTemplateWorkflowDialog)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Article,
                        contentDescription = "Select Workflow Template"
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(
                onClick = {
                    scope.launch {
                        onEvent(ProjectCreateOrEditIntent.OpenAddWorkflowDialog)
                    }
                }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add New State")
            }
        }
    }
}

@Composable
fun ProjectWorkflowError(projectCreateOrEditState: ProjectCreateOrEditState) {
    projectCreateOrEditState.projectWorkflowError?.let {
        Text(
            text = it,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 14.sp,
            ),
            color = Color.Red
        )
    }
}