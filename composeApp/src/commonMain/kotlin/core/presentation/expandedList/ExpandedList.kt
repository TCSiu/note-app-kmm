package core.presentation.expandedList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.model.Task
import core.model.Workflow
import kotlinx.serialization.json.Json

@Composable
fun ExpandedListItem(text: String, onclick: () -> Unit = {}) {
    ListItem(
        headlineContent = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onclick() }
    )
    HorizontalDivider(thickness = 1.dp)
}

@Composable
fun ExpandedListHeader(text: String, isExpand: Boolean, onHeaderClicked: () -> Unit) {
    ListItem(
        headlineContent = {
            Text(
                text = text,
                fontSize = 20.sp,
                style = MaterialTheme.typography.headlineMedium,
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = if (isExpand) MaterialTheme.colorScheme.secondaryContainer else Color.White
        ),
        modifier = Modifier
            .clickable { onHeaderClicked() },
        trailingContent = {
            if (isExpand) {
                Icon(imageVector = Icons.Filled.ExpandLess, contentDescription = "Collapse")
            } else {
                Icon(imageVector = Icons.Filled.ExpandMore, contentDescription = "Expand")
            }
        }
    )
}

@Composable
fun ExpandedList(dataList: List<Any>, modifier: Modifier, selectedIndex: MutableState<Int?>) {
    val isExpandedMap = remember {
        List(dataList.size) { index -> index to false }.toMutableStateMap()
    }

    LazyColumn(
        modifier = modifier
    ) {
        dataList.onEachIndexed { index, data ->
            section(
                data = data,
                isExpand = isExpandedMap[index] ?: false,
                onHeaderClicked = {
                    isExpandedMap[index] = !(isExpandedMap[index] ?: false)
                    isExpandedMap.map {
                        isExpandedMap[it.key] = if (it.key != index) false else it.value
                    }
                    selectedIndex.value = index
                }
            )
        }
    }
}

fun <T> LazyListScope.section(
    data: T,
    isExpand: Boolean,
    onHeaderClicked: () -> Unit,
    onItemClicked: (id: Int) -> Unit = {},
) {
    when (data) {
        is Workflow -> {
            val items = Json.decodeFromString<List<String>>(data.workflow ?: "[]")

            item {
                ExpandedListHeader(
                    text = "Template " + data.id.toString(),
                    isExpand = isExpand,
                    onHeaderClicked = onHeaderClicked
                )
            }

            items(items) {
                AnimatedVisibility(
                    visible = isExpand,
                    enter = expandVertically(),
                    exit = shrinkVertically(),
                ) {
                    ExpandedListItem(text = it)
                }
            }

            item {
                HorizontalDivider(
                    thickness = 2.5.dp,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(1.dp))
            }
        }

        is Map.Entry<*, *> -> {
            when (data.value) {
                is List<*> -> {
                    item {
                        ExpandedListHeader(
                            text = data.key.toString(),
                            isExpand = isExpand,
                            onHeaderClicked = onHeaderClicked
                        )
                    }
                    if ((data.value as List<*>).isNotEmpty()) {
                        items(data.value as List<*>) {
                            when (it) {
                                is Task -> {
                                    AnimatedVisibility(
                                        visible = isExpand,
                                        enter = expandVertically(),
                                        exit = shrinkVertically(),
                                    ) {
                                        ExpandedListItem(text = it.name, onclick = {
                                            onItemClicked(it.id)
                                        })
                                    }
                                }
                            }
                        }
                    } else {
                        item {
                            AnimatedVisibility(
                                visible = isExpand,
                                enter = expandVertically(),
                                exit = shrinkVertically(),
                            ) {
                                ExpandedListItem(text = "Empty")
                            }
                        }
                    }
                    item {
                        HorizontalDivider(
                            thickness = 2.5.dp,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(1.dp))
                    }
                }
            }
        }
    }
}