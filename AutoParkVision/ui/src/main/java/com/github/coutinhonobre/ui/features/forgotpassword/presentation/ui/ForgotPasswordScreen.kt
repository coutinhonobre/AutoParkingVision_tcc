package com.github.coutinhonobre.ui.features.forgotpassword.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.github.coutinhonobre.ui.components.ui.appbar.GenericTopAppBar
import com.github.coutinhonobre.ui.components.ui.templates.GenericErrorScreen
import com.github.coutinhonobre.ui.features.forgotpassword.presentation.viewmodel.ForgotPasswordScreenViewAction
import com.github.coutinhonobre.ui.features.forgotpassword.presentation.viewmodel.ForgotPasswordScreenViewModel
import com.github.coutinhonobre.ui.features.forgotpassword.presentation.viewmodel.ForgotPasswordScreenViewState
import com.github.coutinhonobre.ui.features.success.GenericSuccessScreen
import com.github.coutinhonobre.ui.routers.Router

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    forgotPasswordScreenViewModel: ForgotPasswordScreenViewModel
) {

    val forgotPasswordState by forgotPasswordScreenViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            GenericTopAppBar(
                titleText = "Esqueci minha senha",
                onNavigationClick = {
                    navController.popBackStack()
                }
            )

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            when (forgotPasswordState) {
                is ForgotPasswordScreenViewState.Idle -> {
                    isLoading = false
                    ForgotPassword(
                        isLoading = isLoading,
                    ) {
                        forgotPasswordScreenViewModel.onEvent(ForgotPasswordScreenViewAction.ForgotPassword(it))
                    }
                }
                is ForgotPasswordScreenViewState.Loading -> {
                    isLoading = true
                }
                is ForgotPasswordScreenViewState.Success -> {
                    isLoading = false
                    GenericSuccessScreen {
                        navController.navigate(Router.LOGIN.name) {
                            popUpTo(Router.FORGOT_PASSWORD.name) { inclusive = false }
                        }
                    }
                }
                is ForgotPasswordScreenViewState.Failure -> {
                    isLoading = false
                    GenericErrorScreen(
                        icon = Icons.Default.Error,
                        errorMessage = "Falha ao enviar email de recuperação de senha.",
                    ) {
                        forgotPasswordScreenViewModel.onEvent(ForgotPasswordScreenViewAction.ForgotPasswordRetry)
                    }
                }
            }
        }
    }
}
