package home.presentation.home

import core.model.Project
import core.presentation.LoadingState
import core.response.fail.ResponseFailData

data class HomeTabState(
    val loadingState: LoadingState = LoadingState.Init,
    val projects: List<Project> = emptyList(),
    val errors: ResponseFailData? = null,

    val isUnauthorized: Boolean = false
)