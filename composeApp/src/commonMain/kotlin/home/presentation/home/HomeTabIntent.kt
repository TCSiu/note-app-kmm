package home.presentation.home

import core.model.Project

sealed class HomeTabIntent {
    data object FetchData : HomeTabIntent()
    data class OpenProjectOptionBottomSheet(val project: Project) : HomeTabIntent()
    data object DismissProjectOptionBottomSheet : HomeTabIntent()
}