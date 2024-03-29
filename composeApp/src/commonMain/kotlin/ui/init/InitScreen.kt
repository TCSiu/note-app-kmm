package ui.init

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import core.theme.SettingKeys
import kotlinx.coroutines.flow.Flow
import newkmmnoteapp.composeapp.generated.resources.Res
import newkmmnoteapp.composeapp.generated.resources.login
import newkmmnoteapp.composeapp.generated.resources.register
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.home.HomeScreen
import ui.signIn.SignInScreen
import ui.signUp.SignUpScreen

class InitScreen : Screen, KoinComponent {

    @OptIn(ExperimentalResourceApi::class, ExperimentalSettingsApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val settings by inject<Settings>()
        val observableSettings: ObservableSettings = settings as ObservableSettings
        val flowSettings: FlowSettings = observableSettings.toFlowSettings()
        val tokenFlow: Flow<String> = flowSettings.getStringFlow(SettingKeys.TOKEN, "")
        val token = tokenFlow.collectAsState("")

        if (token.value.isNotBlank()) {
            navigator.replace(HomeScreen())
        }

        Surface(
            modifier = Modifier,
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    border = BorderStroke(width = 1.dp, color = Color.Black),
                    onClick = {
                        navigator.push(SignInScreen())
                    }
                ) {
                    Text(text = stringResource(resource = Res.string.login))
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    border = BorderStroke(width = 1.dp, color = Color.Black),
                    onClick = {
                        navigator.push(SignUpScreen())
                    }
                ) {
                    Text(text = stringResource(resource = Res.string.register))
                }
            }
        }
    }
}