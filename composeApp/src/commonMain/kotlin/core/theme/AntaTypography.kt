package core.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import newkmmnoteapp.composeapp.generated.resources.Anta_Regular
import newkmmnoteapp.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AntaTypography(): Typography {
    val anta = FontFamily(
        Font(
            resource = Res.font.Anta_Regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        )
    )
    return Typography(
        displayMedium = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            fontFamily = anta,
        ),
        bodyMedium = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            fontFamily = anta,
        ),
        titleMedium = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp,
            fontFamily = anta,
        ),
        labelMedium = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            fontFamily = anta,
        )
    )
}