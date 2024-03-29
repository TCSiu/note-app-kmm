package core.util

import com.russhwolf.settings.Settings

class MultiplatformSetting {
    private var settings: Settings? = null

    init {
        settings = Settings()
    }

    fun getSettings(): Settings {
        return settings ?: Settings()
    }
}