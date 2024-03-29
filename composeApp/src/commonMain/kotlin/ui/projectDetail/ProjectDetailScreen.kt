package ui.projectDetail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.model.Project
import core.presentation.Loading
import core.presentation.LoadingState
import core.presentation.TopAppBarWithBackButton
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import newkmmnoteapp.composeapp.generated.resources.Res
import newkmmnoteapp.composeapp.generated.resources.project
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.core.component.KoinComponent
import projectDetail.presentation.ProjectDetailIntent
import projectDetail.presentation.ProjectDetailScreenModel
import projectDetail.presentation.component.bottomSheet.ProjectDetailBottomSheet
import ui.projectCreateOrEdit.ProjectCreateOrEditScreen

class ProjectDetailScreen(val projectId: Int = -1) : Screen, KoinComponent {
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
                            Text(text = "Detail ${projectDetailState.projectError}")
                            Text(text = "Tasks ${projectDetailState.projectTasksError}")
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
                        var tabIndex by remember { mutableStateOf(0) }
                        val tasks = projectDetailState.tasks
                        project?.workflow.let {
                            val workflows = Json.decodeFromString<Map<String, String>>(it ?: "{}")
                            BoxWithConstraints(
                                Modifier.fillMaxSize(),
                                propagateMinConstraints = true
                            ) {
                                val maxWidth = this.maxWidth
                                val tabSize: Dp? =
                                    if (workflows.size < 5) (maxWidth / workflows.size) else null
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    ScrollableTabRow(
                                        selectedTabIndex = tabIndex,
                                        edgePadding = 0.dp,
                                    ) {
                                        workflows.onEachIndexed { index, workflow ->
                                            val tabModifier =
                                                if (tabSize != null) Modifier.width(tabSize) else Modifier
                                            Tab(
                                                modifier = tabModifier,
                                                text = { Text(text = workflow.value) },
                                                selected = tabIndex == index,
                                                onClick = {
                                                    scope.launch {
                                                        tabIndex = index
                                                        onEvent(
                                                            ProjectDetailIntent.ChangeWorkflow(
                                                                projectId = projectId,
                                                                workflowUuid = workflow.key
                                                            )
                                                        )
                                                    }
                                                }
                                            )
                                        }
                                    }
                                    LazyVerticalStaggeredGrid(
                                        columns = StaggeredGridCells.Fixed(2),
                                        verticalItemSpacing = 4.dp,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    ) {
                                        val taskList = tasks.toList()[0].second
                                        items(taskList.size) {
                                            Card(
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(text = taskList[it].name)
                                                if (it % 3 == 0) {
                                                    Text(text = taskList[it].description)
                                                }
                                            }
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