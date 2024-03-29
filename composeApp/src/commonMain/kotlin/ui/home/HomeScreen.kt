package ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import core.theme.SettingKeys
import home.presentation.component.TabItem
import home.presentation.home.HomeTab
import home.presentation.home.HomeTabScreenModel
import home.presentation.personal.PersonalTab
import home.presentation.project.ProjectTab
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.init.InitScreen
import ui.projectCreateOrEdit.ProjectCreateOrEditScreen

class HomeScreen : Screen, KoinComponent {
    @OptIn(
        ExperimentalSettingsApi::class, ExperimentalMaterial3Api::class,
        ExperimentalMaterialApi::class
    )
    @Composable
    override fun Content() {
        val settings by inject<Settings>()
        val navigator = LocalNavigator.currentOrThrow
        val observableSettings: ObservableSettings = settings as ObservableSettings
        val flowSettings: FlowSettings = observableSettings.toFlowSettings()
        val tokenFlow: Flow<String> = flowSettings.getStringFlow(SettingKeys.TOKEN, "")
        val token = tokenFlow.collectAsState("test")

        val homeTabScreenModel = getScreenModel<HomeTabScreenModel>()
        val projectOptionState = homeTabScreenModel.projectOptionState.collectAsState()

        LaunchedEffect(token.value) {
            if (token.value.isBlank()) {
                navigator.replace(InitScreen())
            }
        }

        TabNavigator(
            HomeTab
        ) {
            val tabNavigator = LocalTabNavigator.current
            Scaffold(
                bottomBar = {
                    NavigationBar(
                        modifier = Modifier
                            .height(75.dp),
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        TabItem(tab = HomeTab)
                        TabItem(tab = ProjectTab)
                        TabItem(tab = PersonalTab)
                    }
                },
                floatingActionButton = {
                    if (tabNavigator.current == ProjectTab) {
                        FloatingActionButton(
                            modifier = Modifier.zIndex(1f),
                            onClick = {
                                navigator.push(ProjectCreateOrEditScreen())
                            }) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "Create Project",
                            )
                        }
                    }
                }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(it)
                ) {
                    CurrentTab()
                }
            }
        }

    }
}