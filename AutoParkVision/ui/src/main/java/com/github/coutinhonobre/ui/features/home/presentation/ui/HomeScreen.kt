import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.coutinhonobre.domain.feature.parking.model.Parking
import com.github.coutinhonobre.domain.feature.parking.repository.ParkingRepository
import com.github.coutinhonobre.domain.feature.parking.usecase.GetParkingUseCase
import com.github.coutinhonobre.domain.feature.parkingactivitylog.repository.ParkingActivityLogRepository
import com.github.coutinhonobre.domain.feature.parkingactivitylog.usecase.GetTodayEntryExitCountUseCase
import com.github.coutinhonobre.domain.feature.parkingactivitylog.usecase.ListParkingActivityLogUseCase
import com.github.coutinhonobre.ui.components.ui.templates.GenericErrorScreen
import com.github.coutinhonobre.ui.components.ui.templates.GenericLoadingScreen
import com.github.coutinhonobre.ui.features.home.presentation.model.ParkingLogModel
import com.github.coutinhonobre.ui.features.home.presentation.ui.componets.DailySummaryCard
import com.github.coutinhonobre.ui.features.home.presentation.ui.componets.ParkingLogCard
import com.github.coutinhonobre.ui.features.home.presentation.viewmodel.HomeScreenViewModel
import com.github.coutinhonobre.ui.features.home.presentation.viewmodel.HomeScreenViewState
import com.github.coutinhonobre.ui.routers.Router

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel
) {
    val homeScreenState by homeScreenViewModel.uiState.collectAsState()
    var email by remember { mutableStateOf("") }
    var parkingLot by remember { mutableStateOf(listOf<ParkingLogModel>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "AutoParkVision", style = MaterialTheme.typography.titleLarge)
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Router.PARKING.name) {
                                popUpTo(Router.HOME.name) { inclusive = false }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocalParking,
                            contentDescription = "Estacionamentos"
                        )
                    }
                    IconButton(
                        onClick = {
                            navController.navigate(Router.SPLASH.name) {
                                popUpTo(Router.HOME.name) { inclusive = true }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Sair"
                        )
                    }
                }
            )

        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate(Router.CLIENTS.name)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Clientes"
                        )
                    },
                    label = { Text("Clientes") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate(Router.VEHICLES.name) {
                            popUpTo(Router.HOME.name) { inclusive = false }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = "Veículos"
                        )
                    },
                    label = { Text("Veículos") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate(Router.COLLABORATORS.name) {
                            popUpTo(Router.HOME.name) { inclusive = false }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = "Colaboradores"
                        )
                    },
                    label = { Text("Colaboradores") }
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            when (homeScreenState) {
                is HomeScreenViewState.Idle -> {
                    email = (homeScreenState as HomeScreenViewState.Idle).userEmail
                    parkingLot = (homeScreenState as HomeScreenViewState.Idle).parkingLogs
                    val capacity = (homeScreenState as HomeScreenViewState.Idle).capacity
                    val entryCount = (homeScreenState as HomeScreenViewState.Idle).parkingLogs.filter { it.eventType == "Entrada" }.size
                    val exitCount = (homeScreenState as HomeScreenViewState.Idle).parkingLogs.filter { it.eventType == "Saída" }.size

                    item {
                        DailySummaryCard(
                            totalEntries = entryCount,
                            totalExits = exitCount,
                            totalCapacity = capacity,
                        )
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Últimas atividades",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            TextButton(
                                onClick = {
                                    navController.navigate(Router.PARKING_LOG.name)
                                },
                                modifier = Modifier.padding(0.dp),
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = MaterialTheme.colorScheme.primary,
                                    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = 0.5f
                                    )
                                )
                            ) {
                                Text(
                                    text = "Ver todas",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    items(parkingLot) { parkingLog ->
                        ParkingLogCard(
                            parkingLogModel = parkingLog
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                    }

                }

                is HomeScreenViewState.Loading -> {
                    item {
                        GenericLoadingScreen()
                    }
                }

                is HomeScreenViewState.Failure -> {
                    item {
                        GenericErrorScreen(
                            errorMessage = "Erro ao carregar dados"
                        ) { }
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        navController = rememberNavController(),
        homeScreenViewModel = HomeScreenViewModel(
            listParkingActivityLogUseCase = ListParkingActivityLogUseCase(
                object : ParkingActivityLogRepository {

                }
            ),
            getParkingUseCase = GetParkingUseCase(
                parkingRepository = object : ParkingRepository {
                    override suspend fun saveParking(parking: Parking): Result<Boolean> {
                        return Result.success(true)
                    }

                    override suspend fun getParkingForId(): Result<Parking> {
                        return Result.success(
                            Parking(
                                id = "1",
                                capacity = 100,
                                name = "Estacionamento 1"
                            )
                        )
                    }

                    override suspend fun updateParking(parking: Parking): Result<Boolean> {
                        return Result.success(true)
                    }

                }
            ),
            getTodayEntryExitCountUseCase = GetTodayEntryExitCountUseCase(
                object : ParkingActivityLogRepository {

                }
            )
        )
    )
}
