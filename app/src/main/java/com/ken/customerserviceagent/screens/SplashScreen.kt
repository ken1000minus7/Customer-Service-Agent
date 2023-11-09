package com.ken.customerserviceagent.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ken.customerserviceagent.R
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

    val composition = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.agent)
    )
    val progress = animateLottieCompositionAsState(
        composition = composition.value,
        iterations = 1
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition.value,
            progress = progress.value,
            modifier = Modifier
                .size(250.dp)
                .padding(10.dp),
            contentScale = ContentScale.Crop
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Customer Service Agent",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(10.dp)
            )
            Text(
                text = "Developed by Manjot Singh Oberoi",
                fontSize = 12.sp,
                modifier = Modifier.padding(10.dp)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(rememberNavController())
}