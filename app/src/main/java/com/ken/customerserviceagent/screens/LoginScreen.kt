package com.ken.customerserviceagent.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ken.customerserviceagent.components.LoadingDialog
import com.ken.customerserviceagent.components.NotificationDialog
import com.ken.customerserviceagent.data.Routes
import com.ken.customerserviceagent.model.AgentApiResult
import com.ken.customerserviceagent.util.AgentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    var username by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")

    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var loading by remember {
        mutableStateOf(false)
    }

    var notificationIcon by remember {
        mutableStateOf(Icons.Default.Error)
    }

    var notificationText by remember {
        mutableStateOf("Error")
    }

    var notificationTint by remember {
        mutableStateOf<Color?>(null)
    }


    val notificationState = remember {
        mutableStateOf(false)
    }

    val agentViewModel = hiltViewModel<AgentViewModel>()
    val apiResult by agentViewModel.loginResult.observeAsState()


    LaunchedEffect(apiResult) {
        if(apiResult !is AgentApiResult.Loading) {
            loading = false
        }
        when(apiResult) {
            is AgentApiResult.Success -> {
                Toast.makeText(context, "Logged in!", Toast.LENGTH_SHORT).show()
                navController.navigate(Routes.HOME_SCREEN) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
            is AgentApiResult.Failure -> {
                notificationIcon = Icons.Default.Error
                notificationText = "Incorrect username or password!"
                notificationTint = Color.Red
                notificationState.value = true
            }
            is AgentApiResult.Loading -> {
                loading = true
            }
            else -> {}
        }

    }

    if(loading) {
        LoadingDialog()
    }

    if(notificationState.value) {
        NotificationDialog(
            dialogState = notificationState,
            icon = notificationIcon,
            text = notificationText,
            tint = notificationTint
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login", fontSize = 40.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "Welcome back!",
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 50.dp)
        )
        OutlinedTextField(
            modifier = Modifier.padding(vertical = 10.dp),
            value = username,
            onValueChange = { username = it },
            placeholder = {
                Text(text = "Username")
            }
        )
        OutlinedTextField(
            modifier = Modifier.padding(vertical = 10.dp),
            value = password,
            onValueChange = { password = it },
            placeholder = {
                Text(text = "Password")
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = {passwordVisible = !passwordVisible}) {
                    Icon(imageVector  = image, description)
                }
            }
        )
        OutlinedButton(
            modifier = Modifier.padding(vertical = 20.dp),
            onClick = {
                if (username.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "One or more fields are empty", Toast.LENGTH_SHORT)
                        .show()
                    return@OutlinedButton
                }
                agentViewModel.loginUser(username, password)
            }
        ) {
            Text(
                text = "Submit",
                modifier = Modifier.width(200.dp),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}