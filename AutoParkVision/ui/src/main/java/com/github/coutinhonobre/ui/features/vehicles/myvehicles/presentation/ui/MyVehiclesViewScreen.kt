package com.github.coutinhonobre.ui.features.vehicles.myvehicles.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.coutinhonobre.domain.feature.client.repository.ClientRepository
import com.github.coutinhonobre.domain.feature.client.usecase.AddVehicleToClientUseCase
import com.github.coutinhonobre.domain.feature.client.usecase.GetClientUseCase
import com.github.coutinhonobre.domain.feature.vehicle.model.AuthorizedVehicle
import com.github.coutinhonobre.domain.feature.vehicle.model.Vehicle
import com.github.coutinhonobre.domain.feature.vehicle.repository.VehicleRepository
import com.github.coutinhonobre.domain.feature.vehicle.usecase.GetAllVehiclesUseCase
import com.github.coutinhonobre.domain.feature.vehicle.usecase.GetFilterVehiclesUseCase
import com.github.coutinhonobre.ui.components.ui.templates.GenericEmptyScreen
import com.github.coutinhonobre.ui.components.ui.templates.GenericErrorScreen
import com.github.coutinhonobre.ui.components.ui.templates.GenericLoadingScreen
import com.github.coutinhonobre.ui.features.vehicles.myvehicles.presentation.viewmodel.MyVehiclesViewAction
import com.github.coutinhonobre.ui.features.vehicles.myvehicles.presentation.viewmodel.MyVehiclesViewModel
import com.github.coutinhonobre.ui.features.vehicles.myvehicles.presentation.viewmodel.MyVehiclesViewState
import com.github.coutinhonobre.ui.features.vehicles.ui.components.VehicleCard
import com.github.coutinhonobre.ui.routers.Router
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyVehiclesViewScreen(
    viewModel: MyVehiclesViewModel,
    navController: NavHostController
) {

    val viewState by viewModel.uiState.collectAsState()
    var nameClient by remember { mutableStateOf("") }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Veículos de $nameClient")
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
                    showBottomSheet = true
                },
                icon = { Icon(Icons.Filled.Add, "Extended floating action button.") },
                text = {
                    Text(text = "Víncular veículo")
                }
            )
        }
    ) { innerPadding ->

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                AddMyVehicles(
                    onAddVehicleClick = { placa ->
                        scope.launch {
                            viewModel.onEvent(MyVehiclesViewAction.AddVehicle(placa))
                            showBottomSheet = false
                        }
                    }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (viewState) {
                is MyVehiclesViewState.Loading -> {
                    GenericLoadingScreen()
                }
                is MyVehiclesViewState.Empty -> {
                    GenericEmptyScreen(
                        emptyMessage = "Nenhum veículo encontrado",
                    ) {
                        viewModel.onEvent(MyVehiclesViewAction.Retry)
                    }
                }
                is MyVehiclesViewState.Error -> {
                    GenericErrorScreen(
                        errorMessage = "Erro ao buscar veículos",
                    ) {
                        viewModel.onEvent(MyVehiclesViewAction.Retry)
                    }
                }
                is MyVehiclesViewState.Idle -> {
                    LazyColumn {
                        val vehicles = (viewState as MyVehiclesViewState.Idle).list
                        nameClient = (viewState as MyVehiclesViewState.Idle).nameClient
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
                is MyVehiclesViewState.ErrorAddVehicle -> {
                    GenericErrorScreen(
                        errorMessage = "Erro ao adicionar veículo",
                    ) {
                        viewModel.onEvent(MyVehiclesViewAction.Retry)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyVehiclesViewScreenPreview() {
    val navController = rememberNavController()
    MyVehiclesViewScreen(
        viewModel = MyVehiclesViewModel(
            getFilterVehiclesUseCase = GetFilterVehiclesUseCase(
                vehicleRepository = object : VehicleRepository {}
            ),
            getClientUseCase = GetClientUseCase(
                clientRepository = object : ClientRepository {}
            ),
            addVehicleToClientUseCase = AddVehicleToClientUseCase(
                clientRepository = object : ClientRepository {}
            ),
        ),
        navController = navController
    )
}