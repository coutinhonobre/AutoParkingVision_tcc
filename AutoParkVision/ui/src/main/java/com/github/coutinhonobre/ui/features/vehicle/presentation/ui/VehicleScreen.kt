package com.github.coutinhonobre.ui.features.vehicle.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.coutinhonobre.domain.feature.vehicle.model.AuthorizedVehicle
import com.github.coutinhonobre.domain.feature.vehicle.model.Vehicle
import com.github.coutinhonobre.domain.feature.vehicle.repository.VehicleRepository
import com.github.coutinhonobre.domain.feature.vehicle.usecase.GetVehicleUseCase
import com.github.coutinhonobre.domain.feature.vehicle.usecase.SaveVehicleUseCase
import com.github.coutinhonobre.ui.components.ui.appbar.GenericTopAppBar
import com.github.coutinhonobre.ui.components.ui.templates.GenericErrorScreen
import com.github.coutinhonobre.ui.components.ui.templates.GenericLoadingScreen
import com.github.coutinhonobre.ui.features.success.GenericSuccessScreen
import com.github.coutinhonobre.ui.features.vehicle.model.VehicleScreeModel
import com.github.coutinhonobre.ui.features.vehicle.presentation.viewmodel.VehicleViewAction
import com.github.coutinhonobre.ui.features.vehicle.presentation.viewmodel.VehicleViewModel
import com.github.coutinhonobre.ui.features.vehicle.presentation.viewmodel.VehicleViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleScreen(
    viewModel: VehicleViewModel,
    navController: NavHostController
) {

    val viewState by viewModel.uiState.collectAsState()
    var vehicleScreeModel by remember { mutableStateOf(VehicleScreeModel()) }

    Scaffold(
        topBar = {
            GenericTopAppBar(
                titleText = "Veículo",
            ) {
                navController.popBackStack()
            }
        }
    ) { innerPadding ->

        Column(
            modifier = androidx.compose.ui.Modifier.padding(innerPadding)
        ) {
            when (viewState) {
                is VehicleViewState.Idle -> {
                    VehicleScreenForm(
                        vehicleScreeModel = (viewState as VehicleViewState.Idle).vehicle
                    ) {
                        viewModel.onEvent(
                            VehicleViewAction.SaveVehicle(
                                it
                            )
                        )
                    }
                }

                is VehicleViewState.Loading -> {
                    GenericLoadingScreen()
                }

                is VehicleViewState.Success -> {
                    GenericSuccessScreen {
                        navController.popBackStack()
                    }
                }

                is VehicleViewState.Error -> {
                    GenericErrorScreen(
                        errorMessage = (viewState as VehicleViewState.Error).message
                    ) {
                        viewModel.onEvent(VehicleViewAction.SaveVehicle(vehicleScreeModel))
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun VehicleScreenPreview() {
    val navController = rememberNavController()


    VehicleScreen(
        viewModel = VehicleViewModel(
            saveVehicleUseCase = SaveVehicleUseCase(
                vehicleRepository = object : VehicleRepository {
                    override suspend fun searchAuthorizedVehicles(plate: String): Result<List<Vehicle>> {
                        return Result.success(emptyList())
                    }

                    override suspend fun getAllVehicles(): Result<List<Vehicle>> {
                        return Result.success(
                            listOf(
                                Vehicle(
                                    id = "1",
                                    plate = "ABC-1234",
                                    model = "Fusca",
                                    color = "Azul",
                                    year = 1970,
                                    brand = "Volkswagen",
                                    authorized = AuthorizedVehicle.AUTHORIZED
                                )
                            )
                        )
                    }

                    override suspend fun saveVehicle(vehicle: Vehicle): Result<Unit> {
                        return Result.success(Unit)
                    }

                }
            ),
            getVehicleUseCase = GetVehicleUseCase(
                vehicleRepository = object : VehicleRepository {
                    override suspend fun searchAuthorizedVehicles(plate: String): Result<List<Vehicle>> {
                        return Result.success(emptyList())
                    }

                    override suspend fun getAllVehicles(): Result<List<Vehicle>> {
                        return Result.success(
                            listOf(
                                Vehicle(
                                    id = "1",
                                    plate = "ABC-1234",
                                    model = "Fusca",
                                    color = "Azul",
                                    year = 1970,
                                    brand = "Volkswagen",
                                    authorized = AuthorizedVehicle.AUTHORIZED
                                )
                            )
                        )
                    }

                    override suspend fun saveVehicle(vehicle: Vehicle): Result<Unit> {
                        return Result.success(Unit)
                    }

                }
            )
        ),
        navController = navController
    )
}