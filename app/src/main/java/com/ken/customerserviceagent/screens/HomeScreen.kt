package com.ken.customerserviceagent.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ken.customerserviceagent.components.ActionDialog
import com.ken.customerserviceagent.components.HomeAppBar
import com.ken.customerserviceagent.data.Constants
import com.ken.customerserviceagent.data.Routes
import com.ken.customerserviceagent.util.AgentViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.ken.customerserviceagent.components.LoadingDialog
import com.ken.customerserviceagent.components.NotificationDialog
import com.ken.customerserviceagent.model.AgentApiResult
import com.ken.customerserviceagent.model.Message
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current

    val threadList = remember {
        mutableStateListOf<Message>()
    }
    val logoutState = remember {
        mutableStateOf(false)
    }
    val resetState = remember {
        mutableStateOf(false)
    }
    var loading by remember {
        mutableStateOf(false)
    }
    val notificationState = remember {
        mutableStateOf(false)
    }
    val agentViewModel = hiltViewModel<AgentViewModel>()
    val resetResult by agentViewModel.resetResult.observeAsState()
    val messagesResult by agentViewModel.messagesResult.observeAsState()

    if(logoutState.value) {
        ActionDialog(text = "Are you sure you want to logout?", dialogState = logoutState) {
            val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)
            sharedPreferences.edit()
                .remove(Constants.AUTH_TOKEN)
                .apply()
            Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
            navController.navigate(Routes.LOGIN_SCREEN) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
        }
    }

    if(resetState.value) {
        ActionDialog(text = "Are you sure you want to reset messages?", dialogState = resetState) {
            agentViewModel.resetMessages()
        }
    }

    if(loading) {
        LoadingDialog()
    }

    if(notificationState.value) {
        NotificationDialog(
            dialogState = notificationState,
            icon = Icons.Default.CheckCircle,
            text = "All messages have been reset",
            tint = Color.Green
        )
    }

    LaunchedEffect(true) {
        agentViewModel.getLatestMessages()
    }

    LaunchedEffect(resetResult) {
        if(resetResult !is AgentApiResult.Loading) {
            loading = false
        }
        when(resetResult) {
            is AgentApiResult.Success -> {
                notificationState.value = true
            }
            is AgentApiResult.Failure -> {
                Toast.makeText(context, "Reset failed", Toast.LENGTH_SHORT).show()
            }
            is AgentApiResult.Loading -> {
                loading = true
            }
            else -> {}
        }
    }

    LaunchedEffect(messagesResult) {
        if(messagesResult !is AgentApiResult.Loading) {
            loading = false
        }
        when(messagesResult) {
            is AgentApiResult.Success -> {
                val threads = (messagesResult as AgentApiResult<*>).data!! as List<Message>
                threadList.clear()
                threadList.addAll(threads)
            }
            is AgentApiResult.Loading -> {
                loading = true
            }
            else -> {}
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HomeAppBar(logoutState = logoutState, resetState = resetState)
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            items(threadList) {
                ThreadListItem(message = it, navController = navController)
            }
        }
    }
}

@Composable
fun ThreadListItem(message: Message, navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable {
                navController.navigate(Routes.THREAD_SCREEN + "/${message.threadId}")
            }
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun ThreadListItemPreview() {
    ThreadListItem(
        message = Message(0, 1, 2, 3, "I want to talk with you", ZonedDateTime.now()),
        navController = rememberNavController()
    )
}