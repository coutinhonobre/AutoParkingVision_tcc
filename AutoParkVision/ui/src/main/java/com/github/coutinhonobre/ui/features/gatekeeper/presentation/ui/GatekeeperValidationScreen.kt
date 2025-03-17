package com.github.coutinhonobre.ui.features.gatekeeper.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.coutinhonobre.domain.feature.collaborators.repository.CollaboratorRepository
import com.github.coutinhonobre.domain.feature.collaborators.usecase.GetGatekeeperUseCase
import com.github.coutinhonobre.ui.common.AppPreferences
import com.github.coutinhonobre.ui.components.ui.appbar.GenericTopAppBar
import com.github.coutinhonobre.ui.components.ui.templates.GenericErrorScreen
import com.github.coutinhonobre.ui.components.ui.templates.GenericLoadingScreen
import com.github.coutinhonobre.ui.features.gatekeeper.presentation.viewmodel.GatekeeperViewAction
import com.github.coutinhonobre.ui.features.gatekeeper.presentation.viewmodel.GatekeeperViewModel
import com.github.coutinhonobre.ui.features.gatekeeper.presentation.viewmodel.GatekeeperViewState
import com.github.coutinhonobre.ui.features.success.GenericSuccessScreen
import com.github.coutinhonobre.ui.routers.Router

@Composable
fun GatekeeperValidationScreen(
    navController: NavController,
    gatekeeperViewModel: GatekeeperViewModel
) {
    val gatekeeperState by gatekeeperViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            GenericTopAppBar(
                titleText = "Validação do Porteiro",
                onNavigationClick = {
                    navController.popBackStack()
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (gatekeeperState) {
                is GatekeeperViewState.Idle -> {
                    val code = (gatekeeperState as GatekeeperViewState.Idle).code
                    val error = (gatekeeperState as GatekeeperViewState.Idle).error
                    if (code.isNotEmpty() && !error) {
                        gatekeeperViewModel.onEvent(GatekeeperViewAction.Validate((gatekeeperState as GatekeeperViewState.Idle).code))
                    } else {
                        GatekeeperValidationForm(
                            code = code,
                            isLoading = false,
                            onValidateClick = { code ->
                                gatekeeperViewModel.onEvent(GatekeeperViewAction.Validate(code))
                            }
                        )
                    }
                }
                is GatekeeperViewState.Loading -> {
                    GenericLoadingScreen(
                        loadingMessage = "Validando código..."
                    )
                }
                is GatekeeperViewState.Success -> {
                    GenericSuccessScreen {
                        navController.navigate(Router.CAMERA.name) {
                            popUpTo(Router.GATEKEEPER.name) {
                                inclusive = true
                            }
                        }
                    }
                }
                is GatekeeperViewState.Error -> {
                    GenericErrorScreen(
                        icon = Icons.Default.Error,
                        errorMessage = "Código inválido. Tente novamente.",
                        onRetry = {
                            gatekeeperViewModel.onEvent(GatekeeperViewAction.ValidateRetry)
                        }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GatekeeperValidationScreenPreview() {
    val navController = rememberNavController()
    val gatekeeperViewModel = GatekeeperViewModel(
        getGatekeeperUseCase = GetGatekeeperUseCase(
            collaboratorRepository = object : CollaboratorRepository {}
        ),
        appPreferences = AppPreferences(
            context = LocalContext.current
        )
    )

    GatekeeperValidationScreen(
        navController = navController,
        gatekeeperViewModel = gatekeeperViewModel
    )
}

