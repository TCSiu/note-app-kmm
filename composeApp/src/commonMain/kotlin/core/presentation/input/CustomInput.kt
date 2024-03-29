package core.presentation.input

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import core.presentation.capitalString
import core.theme.SPACER_HEIGHT
import core.theme.Shapes

@Composable
fun ColumnScope.CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChanged: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPasswordTextField: Boolean = false,
    isSingleLine: Boolean = true,
    error: String?,
    placeholder: String = "",
    minLines: Int = 1,
    maxLines: Int = 1,
    title: String? = null,
    isEnable: Boolean = true,
) {
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        if (!title.isNullOrEmpty()) {
            Text(
                text = capitalString(text = title),
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    fontSize = 18.sp
                ),
            )
            Spacer(modifier = Modifier.height(SPACER_HEIGHT))
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChanged,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = placeholder)
            },
            placeholder = {
                Text(text = placeholder)
            },
            singleLine = isSingleLine,
            trailingIcon = {
                if (isPasswordTextField) {
                    PasswordEyeIcon(isPasswordVisible = isPasswordVisible) {
                        isPasswordVisible = !isPasswordVisible
                    }
                }
            },
            visualTransformation = if (isPasswordTextField) {
                if (isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                }
            } else {
                VisualTransformation.None
            },
            minLines = minLines,
            maxLines = maxLines,
            shape = Shapes.small,
            enabled = isEnable,
        )
        if (error != null) {
            Text(text = error, color = MaterialTheme.colorScheme.error)
        }
    }
}