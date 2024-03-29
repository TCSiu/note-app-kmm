package core.theme

import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import home.presentation.project.ProjectTab

val STANDARD_LAYOUT_PADDING = 20.dp
val SPACER_HEIGHT = 10.dp
val LARGE_LAYOUT_PADDING = 30.dp
val SMALL_LAYOUT_PADDING = 10.dp

object Constants {
    const val BASE_URL = "http://note-app.localhost/api" // for android use
//    const val BASE_URL = "http://::1.[1]:80/api" // for android use
//    const val BASE_URL = "http://localhost/api" // for android use
//    const val BASE_URL = "http://10.0.2.2/api" // for android use
//    const val BASE_URL = "http://127.0.0.1/api" // for ios use
}

object SettingKeys {
    const val TOKEN = "token"
    const val USER_ID = "userId"
    const val USER_NAME = "userName"
    const val USER_EMAIL = "userEmail"
}

enum class ItemType {
    PROJECT {
        override fun getScreen(): Tab {
            return ProjectTab
        }

        override fun toString(): String {
            return this.name.lowercase()
        }
    },
    TASK {
        override fun getScreen(): Tab {
            return ProjectTab
        }

        override fun toString(): String {
            return this.name.lowercase()
        }
    };

    abstract fun getScreen(): Tab
}