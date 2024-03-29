package core.presentation

import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Loading(
    size: Int = 32,
    modifier: Modifier? = null,
    color: Color = MaterialTheme.colorScheme.secondary
) {
    CircularProgressIndicator(
        modifier = modifier?.width(size.dp) ?: Modifier.width(size.dp),
        color = color,
    )
}

//sealed interface LoadingState {
//    data object IsLoading: LoadingState
//    data object IsSuccess: LoadingState
//    data object IsFail: LoadingState
//    data object Init: LoadingState
//}

sealed class LoadingState {
    data object IsLoading : LoadingState()
    data object IsSuccess : LoadingState()
    data object IsFail : LoadingState()
    data object Init : LoadingState()
}