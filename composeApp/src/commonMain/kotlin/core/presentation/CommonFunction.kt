package core.presentation

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun capitalString(text: String): String {
    return text.replaceFirstChar {
        if (it.isLowerCase())
            it.uppercase()
        else
            it.toString()
    }
}

fun isoDateTimeToString(dateTime: String): String {
    if (dateTime.isNotBlank()) {
        val time = Instant.parse(dateTime).toLocalDateTime(TimeZone.currentSystemDefault())
        return "${time.date} ${time.time}"
    }
    return ""
}
