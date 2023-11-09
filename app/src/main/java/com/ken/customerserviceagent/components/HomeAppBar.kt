package com.ken.customerserviceagent.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(
    logoutState: MutableState<Boolean>,
    resetState: MutableState<Boolean>
) {
    TopAppBar(
        title = {
            Text(text = "Home", fontWeight = FontWeight.Bold, fontSize = 25.sp)
        },
        actions = {
            IconButton(onClick = { resetState.value = true }) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "")
            }
            IconButton(onClick = { logoutState.value = true }) {
                Icon(imageVector = Icons.Default.Logout, contentDescription = "")
            }
        }
    )
}

@Preview
@Composable
fun HomeAppBarPreview() {
    val state = remember {
        mutableStateOf(false)
    }
    HomeAppBar(state, state)
}