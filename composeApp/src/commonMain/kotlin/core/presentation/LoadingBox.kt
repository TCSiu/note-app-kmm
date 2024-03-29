package core.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DoNotDisturbOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import core.theme.SPACER_HEIGHT
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LoadingBox(
    isShow: Boolean,
    loadingState: LoadingState,
) {
    if (isShow) {
        Dialog(
            onDismissRequest = {

            },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .zIndex(1f)
                        .fillMaxWidth(0.5f)
                        .fillMaxHeight(0.2f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (loadingState) {
                            LoadingState.Init -> {}
                            LoadingState.IsFail -> {
                                Icon(
                                    imageVector = Icons.Outlined.DoNotDisturbOn,
                                    contentDescription = "Success",
                                    tint = Color.Red,
                                    modifier = Modifier.size(64.dp)
                                )
                                Text(text = "Fail")
                            }

                            LoadingState.IsLoading -> {
                                Loading(64)
                                Spacer(modifier = Modifier.height(SPACER_HEIGHT))
                                Text(text = "Loading")
                            }

                            LoadingState.IsSuccess -> {
                                Icon(
                                    imageVector = Icons.Outlined.CheckCircle,
                                    contentDescription = "Success",
                                    tint = Color.Green,
                                    modifier = Modifier.size(64.dp)
                                )
                                Text(text = "Success")
                            }
                        }

                    }
                }
            }
        }
    }
}

