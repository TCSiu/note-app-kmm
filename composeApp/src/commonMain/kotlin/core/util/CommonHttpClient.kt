package core.util

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import core.response.success.RefreshTokenSuccess
import core.theme.Constants
import core.theme.SettingKeys
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CommonHttpClient : KoinComponent {
    private val settings by inject<Settings>()
    private var httpClient: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
//        install(HttpRequestRetry) {
//            maxRetries = 5
//            retryIf { _, response ->
//                !response.status.isSuccess()
//            }
//            delayMillis { retry ->
//                retry * 3000L
//            }
//        }
        expectSuccess = true
    }

    fun getHttpClient(): HttpClient {
        httpClient.plugin(HttpSend).intercept { request ->
            runBlocking {
                val token = settings[SettingKeys.TOKEN, ""]
                if (token.isNotBlank()) {
                    val tokenCheckRequest = HttpRequestBuilder()
                    tokenCheckRequest.url("${Constants.BASE_URL}/token-check")
                    tokenCheckRequest.bearerAuth(token)
                    val tokenCheck = execute(tokenCheckRequest)
                    if (tokenCheck.response.status.value !in 200..299) {
                        val tokenRefreshRequest = HttpRequestBuilder()
                        tokenRefreshRequest.url("${Constants.BASE_URL}/token-refresh")
                        tokenRefreshRequest.bearerAuth(token)
                        val tokenRefresh = execute(tokenRefreshRequest)
                        if (tokenRefresh.response.status.value in 200..299) {
                            val tokenRefreshBody = tokenRefresh.response.body<RefreshTokenSuccess>()
                            settings[SettingKeys.TOKEN] = tokenRefreshBody.data
                            request.bearerAuth(tokenRefreshBody.data)
                        }
//                        else {
//                            settings[SettingKeys.TOKEN] = ""
//                        }
                    }
                }
            }
            execute(request)
        }
        return httpClient
    }
}