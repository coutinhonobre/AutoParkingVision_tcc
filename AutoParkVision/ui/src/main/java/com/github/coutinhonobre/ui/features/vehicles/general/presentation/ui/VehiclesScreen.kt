package com.github.coutinhonobre.ui.features.vehicles.general.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.coutinhonobre.domain.feature.vehicle.model.AuthorizedVehicle
import com.github.coutinhonobre.domain.feature.vehicle.model.Vehicle
import com.github.coutinhonobre.domain.feature.vehicle.repository.VehicleRepository
import com.github.coutinhonobre.domain.feature.vehicle.usecase.GetAllVehiclesUseCase
import com.github.coutinhonobre.ui.components.ui.templates.GenericEmptyScreen
import com.github.coutinhonobre.ui.components.ui.templates.GenericErrorScreen
import com.github.coutinhonobre.ui.components.ui.templates.GenericLoadingScreen
import com.github.coutinhonobre.ui.features.vehicles.general.presentation.viewmodel.VehiclesScreenViewAction
import com.github.coutinhonobre.ui.features.vehicles.general.presentation.viewmodel.VehiclesScreenViewModel
import com.github.coutinhonobre.ui.features.vehicles.general.presentation.viewmodel.VehiclesScreenViewState
import com.github.coutinhonobre.ui.features.vehicles.ui.components.VehicleCard
import com.github.coutinhonobre.ui.routers.Router

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiclesScreen(
    viewModel: VehiclesScreenViewModel,
    navController: NavHostController
) {

    val viewState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Veículos")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate("${Router.VEHICLE.name}/")
                },
                icon = { Icon(Icons.Filled.Add, "Extended floating action button.") },
                text = {
                    Text(text = "Adicionar veículo")
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (viewState) {
                is VehiclesScreenViewState.Loading -> {
                    GenericLoadingScreen()
                }
                is VehiclesScreenViewState.Empty -> {
                    GenericEmptyScreen(
                        emptyMessage = "Nenhum veículo encontrado",
                    ) {
                        viewModel.onEvent(VehiclesScreenViewAction.Retry)
                    }
                }
                is VehiclesScreenViewState.Error -> {
                    GenericErrorScreen(
                        errorMessage = "Erro ao buscar veículos",
                    ) {
                        viewModel.onEvent(VehiclesScreenViewAction.Retry)
                    }
                }
                is VehiclesScreenViewState.Idle -> {
                    LazyColumn {
                        val vehicles = (viewState as VehiclesScreenViewState.Idle).list
                        items(vehicles.size) { index ->
                            val vehicle = vehicles[index]

                            VehicleCard(
                                vehicle = vehicle
                            ) {
                                navController.navigate("${Router.VEHICLE.name}/${vehicle.id}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VehiclesScreenPreview() {
    val navController = rememberNavController()
    VehiclesScreen(
        viewModel = VehiclesScreenViewModel(
            getAllVehiclesUseCase = GetAllVehiclesUseCase(
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