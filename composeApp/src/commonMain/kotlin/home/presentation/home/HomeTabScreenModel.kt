package home.presentation.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import core.domain.ProjectDetailError
import core.domain.Result
import core.model.Project
import core.presentation.LoadingState
import home.data.repository.HomeRepositoryImpl
import home.presentation.home.bottomSheet.ProjectOptionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeTabScreenModel : ScreenModel, KoinComponent {
    private val _homeTabState = MutableStateFlow(HomeTabState())
    private val _projectOptionState = MutableStateFlow(ProjectOptionState())
    val homeTabState = _homeTabState.asStateFlow()
    val projectOptionState = _projectOptionState.asStateFlow()

    private val homeRepositoryImpl by inject<HomeRepositoryImpl>()

    suspend fun onEvent(event: HomeTabIntent) {
        when (event) {
            HomeTabIntent.FetchData -> {
                fetchData()
            }

            is HomeTabIntent.OpenProjectOptionBottomSheet -> {
                openProjectOptionBottomSheet(event.project)
            }

            HomeTabIntent.DismissProjectOptionBottomSheet -> {
                dismissProjectOptionBottomSheet()
            }
        }
    }

    private suspend fun fetchData() {
        screenModelScope.launch {
            when (val response = homeRepositoryImpl.getProjects()) {
                is Result.Error -> {
                    println(response.error)
                    when (response.error) {
                        is ProjectDetailError.BadRequest -> {

                        }

                        ProjectDetailError.NoPermission -> {

                        }

                        ProjectDetailError.NotFound -> {

                        }

                        ProjectDetailError.Serialization -> {

                        }

                        ProjectDetailError.Unauthorized -> {
                            _homeTabState.update {
                                it.copy(
                                    isUnauthorized = true,
                                )
                            }
                        }

                        ProjectDetailError.Unknown -> {

                        }
                    }
                }

                is Result.Success -> {
                    _homeTabState.update { state ->
                        state.copy(
                            projects = response.data,
                            loadingState = LoadingState.IsSuccess
                        )
                    }
                }
            }
        }
    }

    private fun openProjectOptionBottomSheet(project: Project) {
        _projectOptionState.update {
            it.copy(
                isShow = true,
                project = project
            )
        }
    }

    private fun dismissProjectOptionBottomSheet() {
        _projectOptionState.update {
            it.copy(
                isShow = false,
                project = null
            )
        }
    }
}