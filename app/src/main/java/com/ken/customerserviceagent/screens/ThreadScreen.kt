package com.ken.customerserviceagent.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ken.customerserviceagent.components.LoadingAnimation
import com.ken.customerserviceagent.model.AgentApiResult
import com.ken.customerserviceagent.model.Message
import com.ken.customerserviceagent.util.AgentViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ThreadScreen(navController: NavController, threadId: Int) {
    val context = LocalContext.current
    val messages = remember {
        mutableStateListOf<Message>()
    }
    var loading by remember {
        mutableStateOf(false)
    }
    var text by remember {
        mutableStateOf("")
    }
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val lazyListState = rememberLazyListState()
    val agentViewModel = hiltViewModel<AgentViewModel>()
    val messagesResult by agentViewModel.messagesResult.observeAsState()
    val sendMessageResult by agentViewModel.sendMessageResult.observeAsState()

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
            is AgentApiResult.Loading -> {
                loading = true
            }
            is AgentApiResult.Failure -> {
                Toast.makeText(context, "Failed to get messages", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    LaunchedEffect(sendMessageResult) {
        when(sendMessageResult) {
            is AgentApiResult.Success -> {
                val message = (sendMessageResult as AgentApiResult.Success<*>).data as Message
                messages.add(message)
            }
            is AgentApiResult.Failure -> {
                Toast.makeText(context, "Failed to send message", Toast.LENGTH_SHORT).show()
                agentViewModel.sendMessageResult.postValue(null)
            }
            else -> {}
        }
    }

    val sendMessage = {
        if(text.isNotBlank()) {
            agentViewModel.sendMessage(threadId, text)
            focusManager.clearFocus()
            softwareKeyboardController?.hide()
            text = ""
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if(messages.isEmpty()) "Thread ID $threadId" else "User ${messages[0].userId}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if(loading) {
                LoadingAnimation()
            }
            else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    state = lazyListState,
                    reverseLayout = true
                ) {
                    items(messages.reversed()) {
                        MessageListItem(message = it)
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = { Text("Type a message...") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Send
                        ),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                sendMessage()
                            }
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )

                    IconButton(
                        onClick = sendMessage,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "Send", modifier = Modifier.size(30.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun MessageListItem(message: Message) {
    val sender = if (message.agentId == null) "User" else "Agent"
    val senderId = message.agentId ?: message.userId
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clickable {}
            .padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Icon(
            imageVector = if (message.agentId == null) Icons.Default.Person else Icons.Default.SupportAgent,
            contentDescription = sender,
            modifier = Modifier.size(28.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 5.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$sender $senderId",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = message.timestamp.format(DateTimeFormatter.ofPattern("d MMM uuuu, h:mm a")),
                    fontWeight = FontWeight.Light,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
            }

            Text(
                text = message.body,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessageListItemPreview() {
    MessageListItem(
        message = Message(
            0, 1, "2", 3,
            "I want to talk with you please come and talk to me I miss you all the time ",
            ZonedDateTime.now()
        )
    )
}