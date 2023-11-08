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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ken.customerserviceagent.data.Routes
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

        }
        composable(Routes.LOGIN_SCREEN) {

        }
        composable(Routes.HOME_SCREEN) {

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