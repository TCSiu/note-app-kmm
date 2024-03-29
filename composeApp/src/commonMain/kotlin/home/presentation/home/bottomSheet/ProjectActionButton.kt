package home.presentation.home.bottomSheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DisabledByDefault
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.presentation.capitalString
import core.theme.SPACER_HEIGHT
import ui.projectCreateOrEdit.ProjectCreateOrEditScreen
import ui.projectDetail.ProjectDetailScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectActionButton(projectBottomSheetAction: ProjectBottomSheetAction) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Card(
            modifier = Modifier
                .size(size = 50.dp),
            colors = CardDefaults.cardColors(
                containerColor = projectBottomSheetAction.actionContainerColor,
                contentColor = projectBottomSheetAction.actionContentColor,
            ),
            onClick = projectBottomSheetAction.onClick,
            shape = MaterialTheme.shapes.large,
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = projectBottomSheetAction.icon,
                    contentDescription = projectBottomSheetAction.iconDescription,
                )
            }
        }
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            modifier = Modifier,
            style = TextStyle(
                fontSize = 10.sp,
            ),
            color = Color.Black,
            text = capitalString(projectBottomSheetAction.actionName),
        )
    }
    Spacer(Modifier.width(SPACER_HEIGHT))
}

@Composable
fun getProjectActionList(projectId: Int): List<ProjectBottomSheetAction> {
    val navigator = LocalNavigator.currentOrThrow
    return listOf(
        ProjectBottomSheetAction(
            actionContainerColor = Color.White,
            actionContentColor = Color.Black,
            icon = Icons.Outlined.Visibility,
            iconDescription = "View Project",
            actionName = "View",
            onClick = {
                navigator.parent?.push(ProjectDetailScreen(projectId = projectId))
            }
        ),
        ProjectBottomSheetAction(
            actionContainerColor = Color.White,
            actionContentColor = Color.Black,
            icon = Icons.Outlined.Edit,
            iconDescription = "Edit Project",
            actionName = "Edit",
            onClick = {
                navigator.parent?.push(ProjectCreateOrEditScreen(projectId = projectId))
            }
        ),
        ProjectBottomSheetAction(
            actionContainerColor = Color.White,
            actionContentColor = Color.Black,
            icon = Icons.Outlined.PersonAdd,
            iconDescription = "Assign Project",
            actionName = "Assign",
            onClick = {
//                navigator.parent?.push(ProjectAssignScreen(projectId = projectData?.id ?: -1))
            }
        ),
        ProjectBottomSheetAction(
            actionContainerColor = Color.White,
            actionContentColor = Color.Black,
            icon = Icons.Outlined.Delete,
            iconDescription = "Delete Project",
            actionName = "Delete",
        ),
    )
}


data class ProjectBottomSheetAction(
    val actionContainerColor: Color = Color.White,
    val actionContentColor: Color = Color.Black,
    val icon: ImageVector = Icons.Filled.DisabledByDefault,
    val iconDescription: String? = null,
    val actionName: String = "Default",
    val onClick: () -> Unit = {}
)