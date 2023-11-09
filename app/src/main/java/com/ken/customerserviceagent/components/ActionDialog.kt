package com.ken.customerserviceagent.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun ActionDialog(
    text: String,
    dialogState: MutableState<Boolean>,
    action: () -> Unit
) {
    Dialog(onDismissRequest = { dialogState.value = false }) {
        ElevatedCard(
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = text, fontSize = 20.sp, textAlign = TextAlign.Center)
                Row(
                    modifier = Modifier.padding(10.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(onClick = {
                        action()
                        dialogState.value = false
                    }) {
                        Text(text = "Yes")
                    }
                    OutlinedButton(onClick = { dialogState.value = false }) {
                        Text(text = "No")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ActionDialogPreview() {
    ActionDialog(
        text = "Are you sure you want to dance",
        dialogState = remember {
            mutableStateOf(true)
        }
    ) {}
}