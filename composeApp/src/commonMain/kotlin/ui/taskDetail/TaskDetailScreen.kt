package ui.taskDetail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import core.presentation.Loading
import core.presentation.LoadingState
import core.presentation.TopAppBarWithBackButton
import newkmmnoteapp.composeapp.generated.resources.Res
import newkmmnoteapp.composeapp.generated.resources.task
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.core.component.KoinComponent
import taskDetail.presentation.TaskDetailIntent
import taskDetail.presentation.TaskDetailScreenModel

class TaskDetailScreen(val taskId: Int = -1) : Screen, KoinComponent {
    @OptIn(
        ExperimentalResourceApi::class, ExperimentalMaterial3Api::class,
        ExperimentalFoundationApi::class
    )
    @Composable
    override fun Content() {
        val taskDetailScreenModel = getScreenModel<TaskDetailScreenModel>()
        val taskDetailState by taskDetailScreenModel.taskDetailState.collectAsState()
        val task = taskDetailState.task
        val loadingState = taskDetailState.loadingState
        val onEvent = taskDetailScreenModel::onEvent

        LaunchedEffect(true) {
            onEvent(TaskDetailIntent.GetTaskDetail(taskId = taskId))
        }
        val skipPartiallyExpand by remember {
            mutableStateOf(false)
        }
        val bottomSheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = skipPartiallyExpand,
            confirmValueChange = { it != SheetValue.Hidden }
        )
        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = bottomSheetState,
        )

        BottomSheetScaffold(
            sheetContent = {
                when (loadingState) {
                    LoadingState.IsSuccess -> {
                        Column(
                            Modifier.fillMaxSize().padding(top = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth().weight(1f)
                            ) {
                                items(50) {
                                    ListItem(
                                        headlineContent = {
                                            Text(text = it.toString())
                                        }
                                    )
                                    HorizontalDivider(
                                        thickness = 1.dp,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .wrapContentWidth() // 可以del
                                    .offset {
                                        IntOffset(
                                            x = 0,
                                            y = if (-bottomSheetState.requireOffset()
                                                    .toInt() > -1930
                                            ) -bottomSheetState.requireOffset().toInt() else 0
                                        )
                                    }
                                    .fillMaxWidth()
                                    .background(color = Color.Red),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                HorizontalDivider(
                                    thickness = 1.dp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                ListItem(
                                    headlineContent = {
                                        Text(text = "test")
                                    }
                                )
                            }
                        }
                    }

                    else -> {}
                }
            },
            sheetPeekHeight = 75.dp,
            scaffoldState = bottomSheetScaffoldState,
            sheetDragHandle = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomSheetDefaults.DragHandle()
                    Text(text = "Comments")
                    HorizontalDivider(thickness = 1.dp, modifier = Modifier.fillMaxWidth())
                }
            },
            sheetShape = if (bottomSheetState.targetValue == SheetValue.PartiallyExpanded) BottomSheetDefaults.ExpandedShape else BottomSheetDefaults.HiddenShape,
            topBar = {
                TopAppBarWithBackButton(
                    title = task?.name ?: stringResource(resource = Res.string.task),
                )
            },
        ) {
            when (loadingState) {
                LoadingState.IsFail -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(it),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Fail")
                        Text(text = "Detail ${taskDetailState.taskDetailError}")
                    }
                }

                LoadingState.IsLoading -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(it),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Loading()
                    }
                }

                LoadingState.IsSuccess -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(it),
                    ) {
                        Text(text = "test")
                    }
                }

                else -> {}
            }
        }
    }
}