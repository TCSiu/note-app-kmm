package core.di

import core.util.CommonHttpClient
import core.util.MultiplatformSetting
import home.data.remote.HomeRouter
import home.data.repository.HomeRepositoryImpl
import home.presentation.home.HomeTabScreenModel
import org.koin.dsl.module
import projectCreateOrEdit.data.remote.ProjectCreateOrEditRouter
import projectCreateOrEdit.data.repository.ProjectRepositoryImpl
import projectCreateOrEdit.domain.useCase.ProjectUseCase
import projectCreateOrEdit.presentation.ProjectCreateOrEditScreenModel
import projectDetail.data.remote.ProjectDetailRouter
import projectDetail.data.repository.ProjectDetailRepositoryImpl
import projectDetail.domain.useCase.ChangeWorkflowUseCase
import projectDetail.domain.useCase.GetProjectDetailUseCase
import projectDetail.presentation.ProjectDetailScreenModel
import signIn.data.remote.LoginRouter
import signIn.data.repository.LoginRepositoryImpl
import signIn.domain.useCase.LoginUseCase
import signIn.presentation.SignInScreenModel
import signUp.data.remote.RegisterRouter
import signUp.data.repository.RegisterRepositoryImpl
import signUp.domain.useCase.RegisterUseCase
import signUp.presentation.SignUpScreenModel
import taskDetail.data.remote.TaskDetailRouter
import taskDetail.data.repository.TaskDetailRepositoryImpl
import taskDetail.domain.useCase.TaskDetailUseCase
import taskDetail.presentation.TaskDetailScreenModel

val koinModule = module {
    single { CommonHttpClient().getHttpClient() }
    single { MultiplatformSetting().getSettings() }

    factory { SignInScreenModel() }
    factory { SignUpScreenModel() }
    factory { HomeTabScreenModel() }
    factory { ProjectCreateOrEditScreenModel() }
    factory { ProjectDetailScreenModel() }
    factory { TaskDetailScreenModel() }

    single { LoginRouter(httpClient = get()) }
    single { LoginRepositoryImpl(loginRouter = get()) }
    single { LoginUseCase(loginRepositoryImpl = get()) }

    single { RegisterRouter(httpClient = get()) }
    single { RegisterRepositoryImpl(registerRouter = get()) }
    single { RegisterUseCase(registerRepositoryImpl = get()) }

    single { HomeRouter(httpClient = get(), settings = get()) }
    single { HomeRepositoryImpl(homeRouter = get()) }

    single { ProjectCreateOrEditRouter(httpClient = get(), settings = get()) }
    single { ProjectRepositoryImpl(projectCreateOrEditRouter = get()) }
    single { ProjectUseCase(projectRepositoryImpl = get()) }

    single { ProjectDetailRouter(httpClient = get(), settings = get()) }
    single { ProjectDetailRepositoryImpl(projectDetailRouter = get()) }
    single { GetProjectDetailUseCase(projectDetailRepositoryImpl = get()) }
    single { ChangeWorkflowUseCase(projectDetailRepositoryImpl = get()) }

    single { TaskDetailRouter(httpClient = get(), settings = get()) }
    single { TaskDetailRepositoryImpl(taskDetailRouter = get()) }
    single { TaskDetailUseCase(taskDetailRepositoryImpl = get()) }
}
