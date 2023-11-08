package com.ken.customerserviceagent.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun NotificationDialog(
    dialogState: MutableState<Boolean>,
    icon: ImageVector,
    text: String,
    tint: Color? = null
) {
    Dialog(onDismissRequest = { dialogState.value = false }) {
        ElevatedCard(
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = tint ?: Color.Unspecified,
                    modifier = Modifier.size(80.dp)
                )
                Text(text = text, fontSize = 20.sp, textAlign = TextAlign.Center)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationDialogPreview() {
    NotificationDialog(
        remember {
            mutableStateOf(false)
        },
        Icons.Default.Error,
        "Login failed! Incorrect username"
    )
}