package pt.isel.pdm.gomokuroyale.ui.components


//TODO: REMOVE ALL HARDCODED VALUES

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.pdm.gomokuroyale.R

const val ButtonTestTag = "ButtonTestTag"
const val VerificationTestTag = "VerificationTestTag"
const val BUTTON_COLOR = 0xFF7E91DB
const val TEXT_BOX = 0xFFBDBDBD

@Composable
fun TextComponent(value: Int, fontSize: TextUnit = 35.sp, height: Dp = 40.dp) {
    Text(
        text = stringResource(id = value),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = height),
        style = TextStyle(
            fontSize = fontSize,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.Monospace,
            fontStyle = FontStyle.Italic,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    )
}

@Composable
fun InformationBox(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    resourceId: Int,
    fieldType: FieldType,
    validateField: Boolean = true,
    isError: Boolean,
    supportText: String? = "",
    minCharacters: Int = 0,
    maxCharacters: Int = 24
) {
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp)),
        value = value,
        onValueChange = {
            val newValue = if (it.length > maxCharacters) it.substring(0, maxCharacters) else it
            onValueChange(newValue)
        },
        label = { Text(text = label, fontStyle = FontStyle.Italic, color = Color(TEXT_BOX)) },
        leadingIcon = {
            IconSpecial(painterId = resourceId)
        },
        visualTransformation = when (fieldType) {
            FieldType.PASSWORD -> if (!passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None
            FieldType.EMAIL_OR_USER  -> VisualTransformation.None
        },
        isError = isError,
        supportingText = {
            if (supportText != null) {
                Text(
                    text = supportText,
                    color = if(isError) MaterialTheme.colorScheme.error else Color.DarkGray,
                )
            }
        },
        trailingIcon = {
            when (fieldType) {
                FieldType.PASSWORD -> {
                    IconButton(
                        onClick = { passwordVisibility = !passwordVisibility },
                    ) {
                        IconSpecial(
                            painterId = if (passwordVisibility) R.drawable.icon_eye_open else R.drawable.icon_eye_close,
                            color = if (validateField) Color.Green else Color.Red
                        )
                    }
                }

                FieldType.EMAIL_OR_USER -> {
                    if (validateField) {
                        IconSpecial(painterId = R.drawable.icon_correct)
                    } else {
                        IconSpecial(painterId = R.drawable.icon_incorrect)
                    }
                }
            }
        }

    )
}

@Preview(showBackground = true)
@Composable
fun InformationBoxPreview() {
    InformationBox(
        value = "test",
        label = "Username",
        onValueChange = {},
        resourceId = R.drawable.ic_user,
        fieldType = FieldType.EMAIL_OR_USER,
        validateField = false,
        isError = true,
        supportText = "Help"
    )
}


@Composable
fun IconSpecial(painterId: Int, color: Color = Color.Unspecified) {
    Icon(
        modifier = Modifier
            .height(24.dp)
            .padding(4.dp),
        painter = painterResource(id = painterId),
        contentDescription = "",
        tint = color
    )
}


enum class FieldType {
    EMAIL_OR_USER,
    PASSWORD
}


@Composable
fun TextComponent1(
    text: String,
    underline: Boolean = false,
    enableClick: Boolean = false,
    fontSize: TextUnit = 15.sp,
    onClick: () -> Unit = {}
) {
    if (underline && enableClick) {
        Text(
            text,
            modifier = Modifier
                .clickable { onClick() },
            color = Color.Gray,
            fontStyle = FontStyle.Italic,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline
        )
    } else {
        Text(
            text, color = Color.Gray,
            fontStyle = FontStyle.Italic,
            fontSize = fontSize,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun VerificationComponent(text: String? = null, textUnderline: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().testTag(VerificationTestTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (text != null) {
            TextComponent1(text = text, underline = false, enableClick = false, onClick = {})
            Spacer(modifier = Modifier.width(5.dp))
            TextComponent1(
                text = textUnderline,
                underline = true,
                enableClick = true,
                onClick = onClick
            )
        } else {
            TextComponent1(
                text = textUnderline,
                underline = true,
                enableClick = true,
                onClick = onClick
            )
        }
    }


}

@Composable
fun ButtonComponent(iconResourceId: Int, text: String, onClick: () -> Unit, enabled: Boolean = true) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(1f).testTag(ButtonTestTag),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(Color(BUTTON_COLOR))
    ) {
        Icon(
            painter = painterResource(id = iconResourceId),
            contentDescription = "",
            modifier = Modifier
                .height(30.dp)
                .padding(4.dp),
            tint = Color.Black
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = text,
            textAlign = TextAlign.Start,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
        )
    }
}

//val a = LocalConfiguration.current.screenHeightDp

@Composable
fun DivideComponent() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), color = Color.Gray, thickness = 1.dp
        )
        Text(
            text = "or",
            modifier = Modifier.padding(horizontal = 10.dp),
            color = Color.Gray,
            fontStyle = FontStyle.Italic,
            fontSize = 20.sp
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), color = Color.Gray, thickness = 1.dp
        )
    }
}

@Composable
fun IconButtonWithBorder(
    iconResourceId: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .padding(4.dp)
            .clickable(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(id = iconResourceId),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}


