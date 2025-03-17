package com.github.coutinhonobre.ui.features.splash.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CarRental
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.github.coutinhonobre.ui.features.splash.presentation.viewmodel.SplashViewAction
import com.github.coutinhonobre.ui.features.splash.presentation.viewmodel.SplashViewModel
import com.github.coutinhonobre.ui.features.splash.presentation.viewmodel.SplashViewState
import com.github.coutinhonobre.ui.routers.Router

@Composable
fun SplashScreen(navController: NavController, splashViewModel: SplashViewModel) {
    val splashState by splashViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        splashViewModel.onEvent(SplashViewAction.NavigateToLogin)
    }

    when (splashState) {
        is SplashViewState.Idle -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.CarRental,
                        contentDescription = "App Icon",
                        modifier = Modifier.fillMaxWidth().height(200.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "AutoParking", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        is SplashViewState.NavigateToLogin -> {
            LaunchedEffect(Unit) {
                navController.navigate(Router.WELCOME.name) {
                    popUpTo(Router.SPLASH.name) { inclusive = true }
                }
            }
        }
    }
}
