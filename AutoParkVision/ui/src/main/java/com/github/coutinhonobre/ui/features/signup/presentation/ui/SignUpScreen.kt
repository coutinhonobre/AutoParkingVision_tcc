package com.github.coutinhonobre.ui.features.signup.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Scaffold
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
import com.github.coutinhonobre.ui.components.ui.appbar.GenericTopAppBar
import com.github.coutinhonobre.ui.components.ui.templates.GenericErrorScreen
import com.github.coutinhonobre.ui.features.signup.presentation.model.SignUpErrorType
import com.github.coutinhonobre.ui.features.signup.presentation.viewmodel.SignUpScreenViewAction
import com.github.coutinhonobre.ui.features.signup.presentation.viewmodel.SignUpScreenViewModel
import com.github.coutinhonobre.ui.features.signup.presentation.viewmodel.SignUpScreenViewState
import com.github.coutinhonobre.ui.features.success.GenericSuccessScreen
import com.github.coutinhonobre.ui.routers.Router

@Composable
fun SignUpScreen(navController: NavController, signUpScreenViewModel: SignUpScreenViewModel) {
    val signUpState by signUpScreenViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            GenericTopAppBar(
                titleText = "Crie sua conta",
                onNavigationClick = {
                    navController.popBackStack()
                }
            )
        }
    ) { innerPadding ->
       Column(
              modifier = Modifier.padding(innerPadding)
       ) {
           when (signUpState) {
               is SignUpScreenViewState.Idle -> {
                   isLoading = false
                   SignUpForm(
                       isLoading = isLoading,
                       onSignUpClick = { email, password, confirmPassword ->
                           signUpScreenViewModel.onEvent(SignUpScreenViewAction.SignUp(email, password, confirmPassword))
                       }
                   )
               }
               is SignUpScreenViewState.Loading -> {
                   isLoading = true
               }
               is SignUpScreenViewState.Success -> {
                   isLoading = false
                   GenericSuccessScreen {
                       navController.navigate(Router.LOGIN.name) {
                           popUpTo(Router.SIGNUP.name) { inclusive = false }
                       }
                   }
               }
               is SignUpScreenViewState.Failure -> {
                   val errorMessage = when((signUpState as SignUpScreenViewState.Failure).typeError) {
                       SignUpErrorType.GENERIC -> "Falha no login. Por favor, tente novamente."
                       SignUpErrorType.EMAIL_ALREADY_REGISTERED -> "Email já cadastrado."
                       SignUpErrorType.PASSWORDS_DO_NOT_MATCH -> "As senhas não coincidem."
                   }
                   isLoading = false
                   GenericErrorScreen(
                       icon = Icons.Default.Error,
                       errorMessage = errorMessage,
                       onRetry = {
                           signUpScreenViewModel.onEvent(SignUpScreenViewAction.SignUpRetry)
                       }
                   )
               }
           }
       }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {

}