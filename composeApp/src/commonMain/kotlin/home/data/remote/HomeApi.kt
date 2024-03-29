package home.data.remote

import io.ktor.client.statement.HttpResponse

interface HomeApi {
    suspend fun getProjects(): HttpResponse
}