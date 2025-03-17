package com.github.coutinhonobre.autoparkvision

import HomeScreen
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.coutinhonobre.autoparkvision.ui.theme.AutoParkVisionTheme
import com.github.coutinhonobre.data.features.client.repository.FireStoreClientRepositoryImpl
import com.github.coutinhonobre.data.features.clientslist.repository.FireStoreClientListRepositoryImpl
import com.github.coutinhonobre.data.features.collaborators.collaborators.FireStoreCollaboratorRepositoryImpl
import com.github.coutinhonobre.data.features.forgotpassword.repository.FirebaseForgotPasswordRepositoryImpl
import com.github.coutinhonobre.data.features.login.repository.FirebaseLoginRepositoryImpl
import com.github.coutinhonobre.data.features.parking.repository.FireStoreParkingRepositoryImpl
import com.github.coutinhonobre.data.features.parkingactivitylog.FireStoreParkingActivityLogRepositoryImpl
import com.github.coutinhonobre.data.features.signup.repository.FirebaseSignUpRepositoryImpl
import com.github.coutinhonobre.data.features.vehicle.repository.FireStoreVehicleRepositoryImpl
import com.github.coutinhonobre.domain.feature.client.usecase.AddVehicleToClientUseCase
import com.github.coutinhonobre.domain.feature.client.usecase.GetClientUseCase
import com.github.coutinhonobre.domain.feature.client.usecase.SaveClientUseCase
import com.github.coutinhonobre.domain.feature.clientslist.usecase.ClientListUseCase
import com.github.coutinhonobre.domain.feature.collaborators.usecase.GetCollaboratorIdUseCase
import com.github.coutinhonobre.domain.feature.collaborators.usecase.GetCollaboratorsUseCase
import com.github.coutinhonobre.domain.feature.collaborators.usecase.GetGatekeeperUseCase
import com.github.coutinhonobre.domain.feature.collaborators.usecase.SaveCollaboratorsUseCase
import com.github.coutinhonobre.domain.feature.forgotpassword.usecase.ForgotPasswordUseCaseImpl
import com.github.coutinhonobre.domain.feature.login.usecase.LoginUseCaseImpl
import com.github.coutinhonobre.domain.feature.parking.usecase.GetParkingUseCase
import com.github.coutinhonobre.domain.feature.parking.usecase.SaveParkingUseCase
import com.github.coutinhonobre.domain.feature.parking.usecase.UpdateParkingUseCase
import com.github.coutinhonobre.domain.feature.parkingactivitylog.usecase.FilterParkingLogUseCase
import com.github.coutinhonobre.domain.feature.parkingactivitylog.usecase.GetTodayEntryExitCountUseCase
import com.github.coutinhonobre.domain.feature.parkingactivitylog.usecase.ListParkingActivityLogUseCase
import com.github.coutinhonobre.domain.feature.signup.usecase.SignUpUseCaseImpl
import com.github.coutinhonobre.domain.feature.vehicle.usecase.GetAllVehiclesUseCase
import com.github.coutinhonobre.domain.feature.vehicle.usecase.GetFilterVehiclesUseCase
import com.github.coutinhonobre.domain.feature.vehicle.usecase.GetVehicleUseCase
import com.github.coutinhonobre.domain.feature.vehicle.usecase.SaveVehicleUseCase
import com.github.coutinhonobre.ui.common.AppPreferences
import com.github.coutinhonobre.ui.features.client.presentation.factoty.ClientViewModelFactory
import com.github.coutinhonobre.ui.features.client.presentation.ui.ClientScreen
import com.github.coutinhonobre.ui.features.clientslist.presentation.factory.ClientsScreenViewModelFactory
import com.github.coutinhonobre.ui.features.clientslist.presentation.ui.ClientsScreen
import com.github.coutinhonobre.ui.features.clientslist.presentation.viewmodel.ClientsScreenViewModel
import com.github.coutinhonobre.ui.features.collaborator.presentation.factory.CollaboratorViewModelFactory
import com.github.coutinhonobre.ui.features.collaborator.presentation.ui.CollaboratorScreen
import com.github.coutinhonobre.ui.features.collaborator.presentation.viewmodel.CollaboratorViewModel
import com.github.coutinhonobre.ui.features.collaborators.presentation.factory.CollaboratorsScreenViewModelFactory
import com.github.coutinhonobre.ui.features.collaborators.presentation.ui.CollaboratorsScreen
import com.github.coutinhonobre.ui.features.collaborators.presentation.viewmodel.CollaboratorsScreenViewModel
import com.github.coutinhonobre.ui.features.forgotpassword.presentation.factory.ForgotPasswordScreenViewModelFactory
import com.github.coutinhonobre.ui.features.forgotpassword.presentation.ui.ForgotPasswordScreen
import com.github.coutinhonobre.ui.features.forgotpassword.presentation.viewmodel.ForgotPasswordScreenViewModel
import com.github.coutinhonobre.ui.features.gatekeeper.presentation.factory.GatekeeperViewModelFactory
import com.github.coutinhonobre.ui.features.gatekeeper.presentation.ui.GatekeeperValidationScreen
import com.github.coutinhonobre.ui.features.gatekeeper.presentation.viewmodel.GatekeeperViewModel
import com.github.coutinhonobre.ui.features.home.presentation.factory.HomeScreenViewModelFactory
import com.github.coutinhonobre.ui.features.home.presentation.viewmodel.HomeScreenViewModel
import com.github.coutinhonobre.ui.features.login.presentation.factory.SplashViewModelFactory
import com.github.coutinhonobre.ui.features.login.presentation.ui.LoginScreen
import com.github.coutinhonobre.ui.features.login.presentation.viewmodel.LoginViewModel
import com.github.coutinhonobre.ui.features.login.presentation.viewmodel.LoginViewModelFactory
import com.github.coutinhonobre.ui.features.parking.presentation.factory.ParkingScreenViewModelFactory
import com.github.coutinhonobre.ui.features.parking.presentation.model.ParkingScreenModel
import com.github.coutinhonobre.ui.features.parking.presentation.ui.ParkingScreen
import com.github.coutinhonobre.ui.features.parking.presentation.viewmodel.ParkingScreenViewModel
import com.github.coutinhonobre.ui.features.parkinglog.presentation.factory.ParkingLogViewModelFactory
import com.github.coutinhonobre.ui.features.parkinglog.presentation.ui.ParkingLogScreen
import com.github.coutinhonobre.ui.features.parkinglog.presentation.viewmodel.ParkingLogViewModel
import com.github.coutinhonobre.ui.features.signup.presentation.factory.SignUpScreenViewModelFactory
import com.github.coutinhonobre.ui.features.signup.presentation.ui.SignUpScreen
import com.github.coutinhonobre.ui.features.signup.presentation.viewmodel.SignUpScreenViewModel
import com.github.coutinhonobre.ui.features.splash.presentation.ui.SplashScreen
import com.github.coutinhonobre.ui.features.splash.presentation.viewmodel.SplashViewModel
import com.github.coutinhonobre.ui.features.vehicle.presentation.factory.VehicleViewModelFactory
import com.github.coutinhonobre.ui.features.vehicle.presentation.ui.VehicleScreen
import com.github.coutinhonobre.ui.features.vehicle.presentation.viewmodel.VehicleViewModel
import com.github.coutinhonobre.ui.features.vehicles.general.presentation.factory.VehiclesScreenViewModelFactory
import com.github.coutinhonobre.ui.features.vehicles.general.presentation.ui.VehiclesScreen
import com.github.coutinhonobre.ui.features.vehicles.general.presentation.viewmodel.VehiclesScreenViewModel
import com.github.coutinhonobre.ui.features.vehicles.myvehicles.presentation.factory.MyVehiclesViewModelFactory
import com.github.coutinhonobre.ui.features.vehicles.myvehicles.presentation.ui.MyVehiclesViewScreen
import com.github.coutinhonobre.ui.features.vehicles.myvehicles.presentation.viewmodel.MyVehiclesViewModel
import com.github.coutinhonobre.ui.features.welcome.presentation.ui.WelcomeScreen
import com.github.coutinhonobre.ui.routers.Router

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            AutoParkVisionTheme {
                val navController = rememberNavController()

                val activityLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    if (result.resultCode == 1001) {
                        navController.navigate(Router.SPLASH.name)
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = Router.SPLASH.name,
                ) {
                    composable(Router.SPLASH.name) {
                        val splashViewModel: SplashViewModel = viewModel(
                            factory = SplashViewModelFactory()
                        )
                        SplashScreen(
                            navController = navController,
                            splashViewModel = splashViewModel
                        )
                    }
                    composable(Router.WELCOME.name) {
                        WelcomeScreen(
                            navController = navController
                        )
                    }
                    composable(Router.GATEKEEPER.name) {

                        val viewModel: GatekeeperViewModel = viewModel(
                            factory = GatekeeperViewModelFactory(
                                appPreferences = AppPreferences(
                                    context = LocalContext.current
                                ),
                                getGatekeeperUseCase = GetGatekeeperUseCase(
                                    collaboratorRepository = FireStoreCollaboratorRepositoryImpl(),
                                )
                            )
                        )

                        GatekeeperValidationScreen(
                            navController = navController,
                            gatekeeperViewModel = viewModel
                        )
                    }
                    composable(Router.CAMERA.name) {
                        LaunchedEffect(Unit) {
                            navController.popBackStack()
                            val deeplinkUri = "autoparkvisionapp://placadetection/detectar"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(deeplinkUri))
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            activityLauncher.launch(intent)
                        }
                    }
                    composable(Router.LOGIN.name) {
                        val loginViewModel: LoginViewModel = viewModel(
                            factory = LoginViewModelFactory(
                                loginUseCase = LoginUseCaseImpl(
                                    repository = FirebaseLoginRepositoryImpl()
                                )
                            )
                        )
                        LoginScreen(
                            navController = navController,
                            loginViewModel = loginViewModel
                        )
                    }
                    composable(Router.FORGOT_PASSWORD.name) {
                        val forgotPasswordScreenViewModel: ForgotPasswordScreenViewModel =
                            viewModel(
                                factory = ForgotPasswordScreenViewModelFactory(
                                    forgotPasswordUseCase = ForgotPasswordUseCaseImpl(
                                        repository = FirebaseForgotPasswordRepositoryImpl()
                                    )
                                )
                            )
                        ForgotPasswordScreen(
                            navController = navController,
                            forgotPasswordScreenViewModel = forgotPasswordScreenViewModel
                        )
                    }
                    composable(Router.SIGNUP.name) {
                        val signUpScreenViewModel: SignUpScreenViewModel = viewModel(
                            factory = SignUpScreenViewModelFactory(
                                signUpUseCase = SignUpUseCaseImpl(
                                    repository = FirebaseSignUpRepositoryImpl()
                                )
                            )
                        )
                        SignUpScreen(
                            navController = navController,
                            signUpScreenViewModel = signUpScreenViewModel
                        )
                    }
                    composable(Router.HOME.name) {
                        val parkingRepository = FireStoreParkingRepositoryImpl()
                        val homeScreenViewModel: HomeScreenViewModel = viewModel(
                            factory = HomeScreenViewModelFactory(
                                listParkingActivityLogUseCase = ListParkingActivityLogUseCase(
                                    parkingActivityLogRepository = FireStoreParkingActivityLogRepositoryImpl()
                                ),
                                parkingRepository = parkingRepository,
                                getTodayEntryExitCountUseCase = GetTodayEntryExitCountUseCase(
                                    parkingActivityLogRepository = FireStoreParkingActivityLogRepositoryImpl()
                                )
                            )
                        )
                        HomeScreen(
                            navController = navController,
                            homeScreenViewModel = homeScreenViewModel
                        )
                    }
                    composable(Router.PARKING.name) { backStackEntry ->
                        val parkingRepository = FireStoreParkingRepositoryImpl()
                        val parkingScreenViewModel: ParkingScreenViewModel = viewModel(
                            factory = ParkingScreenViewModelFactory(
                                saveParkingUseCase = SaveParkingUseCase(
                                    parkingRepository = parkingRepository
                                ),
                                getParkingUseCase = GetParkingUseCase(
                                    parkingRepository = parkingRepository
                                ),
                                updateParkingUseCase = UpdateParkingUseCase(
                                    parkingRepository = parkingRepository
                                ),
                                parking = ParkingScreenModel()
                            )
                        )
                        ParkingScreen(
                            navController = navController,
                            viewModel = parkingScreenViewModel
                        )
                    }
                    composable(Router.CLIENTS.name) {
                        val clientsScreenViewModel: ClientsScreenViewModel = viewModel(
                            factory = ClientsScreenViewModelFactory(
                                getClientsUseCase = ClientListUseCase(
                                    repository = FireStoreClientListRepositoryImpl()
                                )
                            )
                        )
                        ClientsScreen(
                            viewModel = clientsScreenViewModel,
                            navController = navController
                        )
                    }
                    composable("${Router.SAVE_CLIENT.name}/{clientId}") { backStackEntry ->
                        val clientId = backStackEntry.arguments?.getString("clientId") ?: ""
                        ClientScreen(
                            navController = navController,
                            clientViewModel = viewModel(
                                factory = ClientViewModelFactory(
                                    clientId = clientId,
                                    saveClientUseCase = SaveClientUseCase(
                                        clientRepository = FireStoreClientRepositoryImpl()
                                    ),
                                    getClientUseCase = GetClientUseCase(
                                        clientRepository = FireStoreClientRepositoryImpl()
                                    )
                                )

                            )
                        )
                    }
                    composable(Router.VEHICLES.name) {
                        val viewModel: VehiclesScreenViewModel = viewModel(
                            factory = VehiclesScreenViewModelFactory(
                                getAllVehiclesUseCase = GetAllVehiclesUseCase(
                                    vehicleRepository = FireStoreVehicleRepositoryImpl()
                                )
                            )
                        )
                        VehiclesScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                    composable("${Router.VEHICLE.name}/{vehicleId}") { backStackEntry ->
                        val vehicleId = backStackEntry.arguments?.getString("vehicleId") ?: ""

                        val viewModel: VehicleViewModel = viewModel(
                            factory = VehicleViewModelFactory(
                                vehicleId = vehicleId,
                                saveVehicleUseCase = SaveVehicleUseCase(
                                    vehicleRepository = FireStoreVehicleRepositoryImpl()
                                ),
                                getVehicleUseCase = GetVehicleUseCase(
                                    vehicleRepository = FireStoreVehicleRepositoryImpl()
                                )
                            )
                        )

                        VehicleScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                    composable(Router.COLLABORATORS.name) {
                        val viewModel: CollaboratorsScreenViewModel = viewModel(
                            factory = CollaboratorsScreenViewModelFactory(
                                getCollaboratorsUseCase = GetCollaboratorsUseCase(
                                    collaboratorRepository = FireStoreCollaboratorRepositoryImpl()
                                )
                            )
                        )

                        CollaboratorsScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                    composable("${Router.COLLABORATOR.name}/{collaboratorId}") { backStackEntry ->
                        val collaboratorId = backStackEntry.arguments?.getString("collaboratorId") ?: ""

                        val viewModel: CollaboratorViewModel = viewModel(
                            factory = CollaboratorViewModelFactory(
                                collaboratorId = collaboratorId,
                                saveCollaboratorsUseCase = SaveCollaboratorsUseCase(
                                    collaboratorRepository = FireStoreCollaboratorRepositoryImpl()
                                ),
                                getIdCollaboratorUseCase = GetCollaboratorIdUseCase(
                                    collaboratorRepository = FireStoreCollaboratorRepositoryImpl()
                                )
                            )
                        )

                        CollaboratorScreen(
                            collaboratorViewModel = viewModel,
                            navController = navController
                        )
                    }
                    composable(Router.PARKING_LOG.name) {
                        val viewModel: ParkingLogViewModel = viewModel(
                            factory = ParkingLogViewModelFactory(
                                listParkingActivityLogUseCase = ListParkingActivityLogUseCase(
                                    parkingActivityLogRepository = FireStoreParkingActivityLogRepositoryImpl()
                                ),
                                filterParkingLogUseCase = FilterParkingLogUseCase(
                                    parkingActivityLogRepository = FireStoreParkingActivityLogRepositoryImpl()
                                )
                            )
                        )

                        ParkingLogScreen(
                            navController = navController,
                            parkingLogViewModel = viewModel
                        )
                    }
                    composable("${Router.MY_VEHICLES.name}/{clientId}/{nameClientId}") { backStackEntry ->
                        val clientId = backStackEntry.arguments?.getString("clientId") ?: ""
                        val nameClientId = backStackEntry.arguments?.getString("nameClientId") ?: ""

                        val viewModel: MyVehiclesViewModel = viewModel(
                            factory = MyVehiclesViewModelFactory(
                                getFilterVehiclesUseCase = GetFilterVehiclesUseCase(
                                    vehicleRepository = FireStoreVehicleRepositoryImpl()
                                ),
                                getClientUseCase = GetClientUseCase(
                                    clientRepository = FireStoreClientRepositoryImpl()
                                ),
                                addVehicleToClientUseCase = AddVehicleToClientUseCase(
                                    clientRepository = FireStoreClientRepositoryImpl()
                                ),
                                clientId = clientId,
                                nameClient = nameClientId
                            )
                        )
                        MyVehiclesViewScreen(
                            viewModel = viewModel,
                            navController = navController
                        )

                    }
                }
            }
        }
    }

}

