package com.github.coutinhonobre.ui.features.login.presentation.ui


import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CarRental
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.coutinhonobre.domain.feature.login.usecase.LoginUseCase
import com.github.coutinhonobre.ui.components.ui.appbar.GenericTopAppBar
import com.github.coutinhonobre.ui.components.ui.templates.GenericErrorScreen
import com.github.coutinhonobre.ui.components.ui.templates.GenericLoadingScreen
import com.github.coutinhonobre.ui.features.login.presentation.viewmodel.LoginViewAction
import com.github.coutinhonobre.ui.features.login.presentation.viewmodel.LoginViewModel
import com.github.coutinhonobre.ui.features.login.presentation.viewmodel.LoginViewState
import com.github.coutinhonobre.ui.features.success.GenericSuccessScreen
import com.github.coutinhonobre.ui.routers.Router

@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel) {
    val loginState by loginViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            GenericTopAppBar(
                titleText = "Faça login",
                onNavigationClick = {
                    navController.popBackStack()
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            when (loginState) {
                is LoginViewState.Idle -> {
                    LoginForm(
                        isLoading = false,
                        onLoginClick = { username, password ->
                            loginViewModel.onEvent(LoginViewAction.Login(username, password))
                        },
                        onCreateAccountClick = {
                            navController.navigate(Router.SIGNUP.name)
                        },
                        onForgetPasswordClick = {
                            navController.navigate(Router.FORGOT_PASSWORD.name)
                        }
                    )
                }
                is LoginViewState.Loading -> {
                    GenericLoadingScreen(
                        loadingMessage = "Realizando login..."
                    )
                }
                is LoginViewState.LoginSuccess -> {
                    GenericSuccessScreen {
                        navController.navigate(Router.HOME.name) {
                            popUpTo(Router.LOGIN.name) { inclusive = false }
                        }
                    }
                }
                is LoginViewState.Error -> {
                    GenericErrorScreen(
                        icon = Icons.Default.Error,
                        errorMessage = "Falha no login. Por favor, tente novamente.",
                        onRetry = {
                            loginViewModel.onEvent(LoginViewAction.LoginRetry)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    val loginViewModel = LoginViewModel(
        loginUseCase = object : LoginUseCase {
            override suspend fun login(email: String, password: String): Result<Boolean> {
                return Result.success(true)
            }

        }
    )

    LoginScreen(
        navController = navController,
        loginViewModel = loginViewModel
    )
}
