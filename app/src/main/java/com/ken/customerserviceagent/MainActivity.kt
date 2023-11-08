package com.ken.customerserviceagent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ken.customerserviceagent.data.Routes
import com.ken.customerserviceagent.screens.HomeScreen
import com.ken.customerserviceagent.screens.LoginScreen
import com.ken.customerserviceagent.screens.SplashScreen
import com.ken.customerserviceagent.screens.ThreadScreen
import com.ken.customerserviceagent.ui.theme.CustomerServiceAgentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomerServiceAgentTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CustomerServiceAgentApp()
                }
            }
        }
    }
}

@Composable
fun CustomerServiceAgentApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH_SCREEN
    ) {
        composable(Routes.SPLASH_SCREEN) {
            SplashScreen(navController = navController)
        }
        composable(Routes.LOGIN_SCREEN) {
            LoginScreen(navController = navController)
        }
        composable(Routes.HOME_SCREEN) {
            HomeScreen(navController = navController)
        }
        composable(
            route = Routes.THREAD_SCREEN + "/{threadId}",
            arguments = listOf(navArgument("threadId") { type = NavType.IntType })
        ) {
            val threadId = it.arguments!!.getInt("threadId")
            ThreadScreen(navController = navController, threadId = threadId)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CustomerServiceAgentTheme {
        CustomerServiceAgentApp()
    }
}