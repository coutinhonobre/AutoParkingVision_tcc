package com.github.coutinhonobre.ui.features.parking.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.github.coutinhonobre.ui.components.ui.templates.GenericLoadingScreen
import com.github.coutinhonobre.ui.features.parking.presentation.model.ParkingScreenModel
import com.github.coutinhonobre.ui.features.parking.presentation.viewmodel.ParkingScreenViewAction
import com.github.coutinhonobre.ui.features.parking.presentation.viewmodel.ParkingScreenViewModel
import com.github.coutinhonobre.ui.features.parking.presentation.viewmodel.ParkingScreenViewState
import com.github.coutinhonobre.ui.features.success.GenericSuccessScreen
import com.github.coutinhonobre.ui.routers.Router

@Composable
fun ParkingScreen(
    navController: NavController,
    viewModel: ParkingScreenViewModel
) {

    val viewState by viewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var parkingScreenModel by remember { mutableStateOf(ParkingScreenModel()) }

    Scaffold(
        topBar = {
            GenericTopAppBar(
                titleText = "Estacionamento",
                onNavigationClick = {
                    navController.popBackStack()
                }
            )
        }
    ) {innerPadding ->

        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {

            when (viewState) {
                is ParkingScreenViewState.Idle -> {
                    isLoading = false
                    parkingScreenModel = (viewState as ParkingScreenViewState.Idle).parking
                    ParkingScreenForm(
                        parkingScreenModel = parkingScreenModel,
                        onSaveParkingClick = {
                            parkingScreenModel.name = it.name
                            parkingScreenModel.capacity = it.capacity
                            parkingScreenModel.id = it.id
                            viewModel.onEvent(ParkingScreenViewAction.SaveParking(it))
                        }
                    )
                }
                is ParkingScreenViewState.Loading -> {
                    isLoading = true
                    GenericLoadingScreen()
                }
                is ParkingScreenViewState.Error -> {
                    isLoading = false
                    GenericErrorScreen(
                        errorMessage = "Erro ao salvar estacionamento",
                    ) {
                        viewModel.onEvent(ParkingScreenViewAction.Retry)
                    }
                }
                is ParkingScreenViewState.Success -> {
                    isLoading = false
                    GenericSuccessScreen {
                        navController.navigate(Router.HOME.name) {
                            popUpTo(Router.PARKING.name) { inclusive = false }
                        }
                    }
                }
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun ParkingScreenPreview() {

}