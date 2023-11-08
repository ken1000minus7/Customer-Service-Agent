package com.ken.customerserviceagent.screens

import android.content.Context
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ken.customerserviceagent.data.Constants
import com.ken.customerserviceagent.data.Routes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val navControllerBackStackEntry = navController.currentBackStackEntryAsState()
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)

    LaunchedEffect(key1 = true) {
        delay(1200)
        val route = if(sharedPreferences.contains(Constants.AUTH_TOKEN)) Routes.HOME_SCREEN else Routes.LOGIN_SCREEN
        navController.navigate(route) {
            popUpTo(navControllerBackStackEntry.value!!.destination.route!!) {
                inclusive = true
            }
        }
    }
    Text(text = Routes.SPLASH_SCREEN)
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen(rememberNavController())
}