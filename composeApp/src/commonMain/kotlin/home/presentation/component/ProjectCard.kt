package home.presentation.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.model.Project
import core.presentation.capitalString
import core.presentation.isoDateTimeToString
import home.presentation.home.HomeTabIntent
import home.presentation.home.HomeTabScreenModel
import kotlinx.coroutines.runBlocking
import ui.projectDetail.ProjectDetailScreen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProjectCard(
    project: Project,
    homeTabScreenModel: HomeTabScreenModel,
) {
    val navigator = LocalNavigator.currentOrThrow
    val onEvent = homeTabScreenModel::onEvent

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
            .size(width = 260.dp, height = 180.dp)
            .combinedClickable(
                onClick = {
                    navigator.parent?.push(ProjectDetailScreen(projectId = project.id))
                },
                onLongClick = {
                    runBlocking {
                        onEvent(HomeTabIntent.OpenProjectOptionBottomSheet(project))
                    }
                }
            ),
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = capitalString(project.name),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
            )
            Text(
                text = project.description,
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 12.sp
                ),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = isoDateTimeToString(project.updatedAt),
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 10.sp,
                    )
                )
                Text(
                    text = project.creator?.name ?: "Undefined",
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 10.sp,
                    )
                )
            }
        }

    }
}