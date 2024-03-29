package home.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import core.presentation.LoadingState
import core.theme.ItemType
import home.presentation.component.ItemsRowHeader
import home.presentation.component.ProjectCard
import home.presentation.home.bottomSheet.ProjectOptionBottomSheet
import ui.init.InitScreen

object HomeTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Home"
            val icon = rememberVectorPainter(Icons.Outlined.Home)
            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon,
                )
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val tabNavigator = LocalTabNavigator.current
        val homeTabScreenModel = getScreenModel<HomeTabScreenModel>()
        val homeTabState by homeTabScreenModel.homeTabState.collectAsState()
        val projects = homeTabState.projects
        val onEvent = homeTabScreenModel::onEvent

        LaunchedEffect(true, homeTabState) {
            onEvent(HomeTabIntent.FetchData)
            if (homeTabState.isUnauthorized) {
                navigator.replace(InitScreen())
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (homeTabState.loadingState) {
                is LoadingState.IsSuccess -> {
                    ItemsRowHeader(
                        itemType = ItemType.PROJECT,
                        items = projects,
                        tabNavigator = tabNavigator,
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp)
                    ) {
                        items(projects) { project ->
                            ProjectCard(
                                project = project,
                                homeTabScreenModel = homeTabScreenModel,
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                        }
                    }
                    ProjectOptionBottomSheet(homeTabScreenModel = homeTabScreenModel)
                }

                else -> {}
            }
        }
    }
}