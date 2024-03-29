package ui.projectDetail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.model.Project
import core.presentation.Loading
import core.presentation.LoadingState
import core.presentation.TopAppBarWithBackButton
import core.theme.SMALL_LAYOUT_PADDING
import kotlinx.coroutines.launch
import newkmmnoteapp.composeapp.generated.resources.Res
import newkmmnoteapp.composeapp.generated.resources.project
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.core.component.KoinComponent
import projectDetail.presentation.ProjectDetailIntent
import projectDetail.presentation.ProjectDetailScreenModel
import projectDetail.presentation.component.bottomSheet.ProjectDetailBottomSheet
import ui.projectCreateOrEdit.ProjectCreateOrEditScreen
import ui.taskDetail.TaskDetailScreen

class OldProjectDetailScreen(val projectId: Int = -1) : Screen, KoinComponent {
    @OptIn(ExperimentalResourceApi::class, ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val projectDetailScreenModel = getScreenModel<ProjectDetailScreenModel>()
        val projectDetailState by projectDetailScreenModel.projectDetailState.collectAsState()
        val loadingState = projectDetailState.loadingState
        val project: Project? = projectDetailState.project
        val navigator = LocalNavigator.currentOrThrow

        val onEvent = projectDetailScreenModel::onEvent
        val scope = rememberCoroutineScope()

        LaunchedEffect(true) {
            onEvent(ProjectDetailIntent.GetProjectDetail(projectId))
        }

        Scaffold(
            topBar = {
                TopAppBarWithBackButton(
                    title = project?.name ?: stringResource(resource = Res.string.project),
                    actions = {
                        when (loadingState) {
                            LoadingState.IsSuccess -> {
                                if (project?.canEdit == true) {
                                    IconButton(
                                        modifier = Modifier,
                                        onClick = {
                                            navigator.push(ProjectCreateOrEditScreen(projectId = projectId))
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Edit,
                                            contentDescription = "Edit"
                                        )
                                    }
                                }
                                IconButton(
                                    modifier = Modifier,
                                    onClick = {
                                        projectDetailState.project?.let {
                                            scope.launch {
                                                onEvent(ProjectDetailIntent.OpenProjectOption(it))
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Outlined.Article,
                                        contentDescription = "View"
                                    )
                                }
                            }

                            else -> {}
                        }
                    }
                )
            }
        ) { it ->
            Column(
                modifier = Modifier.padding(it).fillMaxSize()
            ) {
                when (loadingState) {
                    LoadingState.IsFail -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "Fail")
                            Text(text = "Detail ${projectDetailState.projectError}" ?: "")
                            Text(text = "Tasks ${projectDetailState.projectTasksError}" ?: "")
                        }
                    }

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
                        val projectTasks = projectDetailState.tasks
                        projectTasks?.let {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                LazyColumn {
                                    it.onEachIndexed { _, (header, tasks) ->
                                        stickyHeader {
                                            Row(
                                                modifier = Modifier.fillMaxWidth()
                                                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
                                                    .padding(horizontal = SMALL_LAYOUT_PADDING),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                            ) {
                                                Text(
                                                    text = header,
                                                    fontSize = 14.sp,
                                                )
                                                Text(
                                                    text = tasks.size.toString(),
                                                    fontSize = 14.sp,
                                                )
                                            }
                                            HorizontalDivider(
                                                thickness = 2.5.dp,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                        items(tasks) { task ->
                                            ListItem(
                                                headlineContent = {
                                                    Text(
                                                        text = task.name,
                                                        fontSize = 16.sp,
                                                        style = MaterialTheme.typography.headlineMedium,
                                                    )
                                                },
                                                modifier = Modifier.clickable {
                                                    navigator.push(TaskDetailScreen(taskId = task.id))
                                                }
                                            )
                                            HorizontalDivider(
                                                thickness = 1.dp,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            Spacer(modifier = Modifier.height(1.dp))
                                        }
                                    }
                                }
                            }
                        }
                        ProjectDetailBottomSheet(projectDetailScreenModel = projectDetailScreenModel)
                    }

                    else -> {}
                }
            }
        }
    }
}