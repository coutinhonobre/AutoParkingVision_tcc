package com.github.coutinhonobre.ui.features.client.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCarFilled
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.coutinhonobre.domain.feature.client.model.Client
import com.github.coutinhonobre.domain.feature.client.repository.ClientRepository
import com.github.coutinhonobre.domain.feature.client.usecase.GetClientUseCase
import com.github.coutinhonobre.domain.feature.client.usecase.SaveClientUseCase
import com.github.coutinhonobre.ui.components.ui.appbar.GenericTopAppBar
import com.github.coutinhonobre.ui.components.ui.templates.GenericErrorScreen
import com.github.coutinhonobre.ui.components.ui.templates.GenericLoadingScreen
import com.github.coutinhonobre.ui.features.client.presentation.model.ClientScreenModel
import com.github.coutinhonobre.ui.features.client.presentation.viewmodel.ClientViewAction
import com.github.coutinhonobre.ui.features.client.presentation.viewmodel.ClientViewModel
import com.github.coutinhonobre.ui.features.client.presentation.viewmodel.ClientViewState
import com.github.coutinhonobre.ui.features.success.GenericSuccessScreen
import com.github.coutinhonobre.ui.routers.Router

@Composable
fun ClientScreen(
    navController: NavController,
    clientViewModel: ClientViewModel
) {
    val viewState by clientViewModel.uiState.collectAsState()
    var clientScreenModel by remember { mutableStateOf(ClientScreenModel()) }
    var isShowMyVehicles by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            GenericTopAppBar(
                titleText = "Cliente",
            ) {
                navController.popBackStack()
            }
        },
        floatingActionButton = {
            if (isShowMyVehicles) {
                ExtendedFloatingActionButton(
                    onClick = {
                        navController.navigate("${Router.MY_VEHICLES.name}/${clientScreenModel.id}/${clientScreenModel.name}")
                    },
                    icon = {
                        Icon(
                            Icons.Filled.DirectionsCarFilled,
                            "Extended floating action button."
                        )
                    },
                    text = {
                        Text(text = "Meus Veículos")
                    }
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            when (viewState) {
                is ClientViewState.Idle -> {
                    isShowMyVehicles = (viewState as ClientViewState.Idle).client.id?.isNotEmpty() ?: false
                    clientScreenModel = (viewState as ClientViewState.Idle).client
                    ClientScreenForm(
                        clientScreenModel = (viewState as ClientViewState.Idle).client
                    ) {
                        clientViewModel.onEvent(
                            ClientViewAction.SaveClient(
                                it
                            )
                        )
                    }
                }
                is ClientViewState.Loading -> {
                    GenericLoadingScreen()
                }
                is ClientViewState.Success -> {
                    GenericSuccessScreen {
                        navController.popBackStack()
                    }
                }
                is ClientViewState.Error -> {
                    GenericErrorScreen(
                        errorMessage = "Erro ao salvar cliente",
                    ) {
                        clientViewModel.onEvent(ClientViewAction.SaveClient(clientScreenModel))
                    }
                }
            }


        }

    }

}

@Preview(showBackground = true)
@Composable
fun ClientScreenPreview() {
    val clientRepository = object : ClientRepository {
        override suspend fun saveClient(
            client: Client
        ): Result<Boolean> {
            return Result.success(true)
        }

        override suspend fun getClient(clientId: String): Result<Client> {
            return Result.success(
                Client(
                    id = "as12",
                    name = "Teste",
                    phone = "123456",
                    email = "email@email.com"
                )
            )
        }
    }
    val clientViewModel = ClientViewModel(
        clientId = "",
        saveClientUseCase = SaveClientUseCase(
            clientRepository = clientRepository
        ),
        getClientUseCase = GetClientUseCase(
            clientRepository = clientRepository
        )
    )
    val navController = rememberNavController()
    ClientScreen(
        navController, clientViewModel = clientViewModel)
}