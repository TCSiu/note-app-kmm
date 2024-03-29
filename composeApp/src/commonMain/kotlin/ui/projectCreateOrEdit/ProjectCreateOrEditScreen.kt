package ui.projectCreateOrEdit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.presentation.Loading
import core.presentation.LoadingBox
import core.presentation.LoadingState
import core.presentation.TopAppBarWithBackButton
import core.theme.SMALL_LAYOUT_PADDING
import core.theme.STANDARD_LAYOUT_PADDING
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import projectCreateOrEdit.presentation.ProjectCreateOrEditIntent
import projectCreateOrEdit.presentation.ProjectCreateOrEditScreenModel
import projectCreateOrEdit.presentation.component.ProjectGeneralField
import projectCreateOrEdit.presentation.component.ProjectWorkflow
import projectCreateOrEdit.presentation.component.dialog.workflow.add.ProjectAddWorkflowDialog
import projectCreateOrEdit.presentation.component.dialog.workflow.delete.ProjectDeleteWorkflowDialog
import projectCreateOrEdit.presentation.component.dialog.workflow.rename.ProjectRenameWorkflowDialog
import projectCreateOrEdit.presentation.component.dialog.workflow.template.ProjectWorkflowTemplateDialog
import ui.projectDetail.ProjectDetailScreen

class ProjectCreateOrEditScreen(
    val projectId: Int? = null
) : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val projectCreateOrEditScreenModel = getScreenModel<ProjectCreateOrEditScreenModel>()
        val projectCreateOrEditState by projectCreateOrEditScreenModel.projectCreateOrEditState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        val scope = rememberCoroutineScope()
        val onEvent = projectCreateOrEditScreenModel::onEvent

        LaunchedEffect(true) {
            onEvent(ProjectCreateOrEditIntent.Init(projectId))
        }

        Scaffold(
            topBar = {
                TopAppBarWithBackButton(
                    title = if (projectId != null) "Edit" else "Create",
                    actions = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    onEvent(ProjectCreateOrEditIntent.Submit(projectId = projectId))
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Save,
                                contentDescription = "Save Project"
                            )
                        }
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier.padding(it)
            ) {
                when (projectCreateOrEditState.loadingState) {
                    LoadingState.IsLoading -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Loading()
                        }
                    }

                    LoadingState.IsSuccess -> {
                        Column(
                            modifier = Modifier.fillMaxSize()
                                .padding(horizontal = STANDARD_LAYOUT_PADDING)
                                .padding(top = SMALL_LAYOUT_PADDING)
                        ) {
                            ProjectGeneralField(
                                columnScope = this,
                                projectId = projectId,
                                projectCreateOrEditScreenModel = projectCreateOrEditScreenModel
                            )
                            ProjectWorkflow(
                                projectId = projectId,
                                projectCreateOrEditScreenModel = projectCreateOrEditScreenModel
                            )
                        }
                        ProjectAddWorkflowDialog(projectCreateOrEditScreenModel = projectCreateOrEditScreenModel)
                        ProjectRenameWorkflowDialog(projectCreateOrEditScreenModel = projectCreateOrEditScreenModel)
                        ProjectDeleteWorkflowDialog(projectCreateOrEditScreenModel = projectCreateOrEditScreenModel)
                        ProjectWorkflowTemplateDialog(projectCreateOrEditScreenModel = projectCreateOrEditScreenModel)
                        LoadingBox(
                            isShow = projectCreateOrEditState.isSubmit,
                            loadingState = projectCreateOrEditState.submitLoadingState,
                        )
                        if (projectCreateOrEditState.isRedirect != null) {
                            navigator.replace(ProjectDetailScreen(projectId = projectCreateOrEditState.isRedirect!!))
                        }
                    }

                    LoadingState.IsFail -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Loading()
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}