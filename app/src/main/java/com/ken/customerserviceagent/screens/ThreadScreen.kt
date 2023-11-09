package com.ken.customerserviceagent.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ken.customerserviceagent.model.AgentApiResult
import com.ken.customerserviceagent.model.Message
import com.ken.customerserviceagent.util.AgentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreadScreen(navController: NavController, threadId: Int) {
    val context = LocalContext.current
    val messages = remember {
        mutableStateListOf<Message>()
    }
    var loading by remember {
        mutableStateOf(false)
    }
    val agentViewModel = hiltViewModel<AgentViewModel>()
    val messagesResult by agentViewModel.messagesResult.observeAsState()
    LaunchedEffect(true) {
        agentViewModel.getThreadMessages(threadId)
    }

    LaunchedEffect(messagesResult) {
        if(messagesResult !is AgentApiResult.Loading) {
            loading = false
        }
        when(messagesResult) {
            is AgentApiResult.Success -> {
                val messageList = (messagesResult as AgentApiResult.Success<*>).data!! as List<Message>
                messages.clear()
                messages.addAll(messageList)
            }
            is AgentApiResult.Failure -> {
                Toast.makeText(context, "Failed to get messages", Toast.LENGTH_SHORT).show()
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
            TopAppBar(
                title = { /*TODO*/ },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            
        }
    }
}