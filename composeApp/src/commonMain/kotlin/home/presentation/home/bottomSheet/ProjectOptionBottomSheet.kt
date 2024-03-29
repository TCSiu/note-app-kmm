package home.presentation.home.bottomSheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import core.presentation.DataWithTitleHorizontal
import core.presentation.LongTextWithTitle
import core.presentation.isoDateTimeToString
import core.theme.SMALL_LAYOUT_PADDING
import core.theme.SPACER_HEIGHT
import core.theme.STANDARD_LAYOUT_PADDING
import home.presentation.home.HomeTabIntent
import home.presentation.home.HomeTabScreenModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import newkmmnoteapp.composeapp.generated.resources.Res
import newkmmnoteapp.composeapp.generated.resources.step
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ProjectOptionBottomSheet(
    homeTabScreenModel: HomeTabScreenModel,
) {
    val skipPartiallyExpand by remember {
        mutableStateOf(false)
    }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpand,
    )
    val projectOptionState = homeTabScreenModel.projectOptionState.collectAsState()
    val projectData = projectOptionState.value.project

    val onEvent = homeTabScreenModel::onEvent
    val scope = rememberCoroutineScope()

    if (projectOptionState.value.isShow) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    onEvent(HomeTabIntent.DismissProjectOptionBottomSheet)
                }
            },
            sheetState = bottomSheetState,
            windowInsets = BottomSheetDefaults.windowInsets,
            containerColor = Color.White,
            shape = if (bottomSheetState.targetValue == SheetValue.PartiallyExpanded) BottomSheetDefaults.ExpandedShape else BottomSheetDefaults.HiddenShape,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = STANDARD_LAYOUT_PADDING)
                    .padding(bottom = STANDARD_LAYOUT_PADDING),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProjectOptionBottomSheetLayout(projectId = projectData?.id ?: -1)
                ProjectOptionBottomSheetContent(homeTabScreenModel = homeTabScreenModel)
            }
        }
    }
}

@Composable
fun ProjectOptionBottomSheetLayout(projectId: Int) {
    val projectActionList = getProjectActionList(projectId = projectId)

    Column(
        modifier = Modifier,
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items(projectActionList.size) {
                ProjectActionButton(projectBottomSheetAction = projectActionList[it])
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = SMALL_LAYOUT_PADDING),
            thickness = 2.dp
        )
    }
}

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProjectOptionBottomSheetContent(
    homeTabScreenModel: HomeTabScreenModel,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        val projectOptionState = homeTabScreenModel.projectOptionState.collectAsState()
        val projectData = projectOptionState.value.project
        val workflow: List<Pair<String, String>> =
            Json.decodeFromString<Map<String, String>>(projectData?.workflow ?: "{}").toList()

        DataWithTitleHorizontal(title = "Project Name: ", data = projectData?.name ?: "")
        Spacer(modifier = Modifier.height(SPACER_HEIGHT))
        LongTextWithTitle(title = "Project Description: ", data = projectData?.description ?: "")
        Spacer(modifier = Modifier.height(SPACER_HEIGHT))
        DataWithTitleHorizontal(
            title = "Created By: ",
            data = projectData?.creator?.name ?: "Undefined"
        )
        Spacer(modifier = Modifier.height(SPACER_HEIGHT))
        DataWithTitleHorizontal(
            title = "Last Updated: ",
            data = isoDateTimeToString(projectData?.updatedAt ?: "")
        )
        Spacer(modifier = Modifier.height(SPACER_HEIGHT))
        LazyColumn {
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "#",
                        modifier = Modifier.weight(0.33f),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(Res.string.step),
                        modifier = Modifier.weight(0.66f),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            items(workflow.size) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(text = (it + 1).toString(), modifier = Modifier.weight(0.33f))
                    Text(text = workflow[it].second, modifier = Modifier.weight(0.66f))
                }
            }
        }
    }
}