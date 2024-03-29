package ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import core.theme.AppTheme
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ui.init.InitScreen


@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    AppTheme {
        Navigator(screen = InitScreen())
    }
}