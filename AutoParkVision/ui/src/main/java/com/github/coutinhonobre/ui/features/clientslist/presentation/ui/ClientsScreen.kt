package com.github.coutinhonobre.ui.features.clientslist.presentation.ui

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
import com.github.coutinhonobre.domain.feature.clientslist.model.ClientsGeneric
import com.github.coutinhonobre.domain.feature.clientslist.repository.ClientListRepository
import com.github.coutinhonobre.domain.feature.clientslist.usecase.ClientListUseCase
import com.github.coutinhonobre.ui.components.ui.templates.GenericEmptyScreen
import com.github.coutinhonobre.ui.components.ui.templates.GenericLoadingScreen
import com.github.coutinhonobre.ui.features.clientslist.presentation.viewmodel.ClientsScreenViewAction
import com.github.coutinhonobre.ui.features.clientslist.presentation.viewmodel.ClientsScreenViewModel
import com.github.coutinhonobre.ui.features.clientslist.presentation.viewmodel.ClientsScreenViewState
import com.github.coutinhonobre.ui.routers.Router

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientsScreen(
    viewModel: ClientsScreenViewModel,
    navController: NavHostController
) {

    val viewState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Clientes") },
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
                    navController.navigate("${Router.SAVE_CLIENT.name}/")
                },
                icon = { Icon(Icons.Filled.Add, "Extended floating action button.") },
                text = { Text(text = "Adicionar cliente") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            when (viewState) {
                is ClientsScreenViewState.Loading -> {
                    GenericLoadingScreen()
                }

                is ClientsScreenViewState.Empty -> {
                    GenericEmptyScreen(
                        emptyMessage = "Nenhum cliente encontrado",
                    ) {
                        viewModel.onEvent(ClientsScreenViewAction.Retry)
                    }
                }

                is ClientsScreenViewState.Idle -> {
                    LazyColumn {
                        val clients = (viewState as ClientsScreenViewState.Idle).clients
                        items(clients.size) { index ->
                            val client = clients[index]

                            ClientCard(
                                client = client,
                            ) {
                                navController.navigate("${Router.SAVE_CLIENT.name}/${client.id}")
                            }
                        }
                    }
                }

                is ClientsScreenViewState.Error -> {
                    GenericEmptyScreen(
                        emptyMessage = "Erro ao carregar os clientes",
                    ) {
                        viewModel.onEvent(ClientsScreenViewAction.Retry)
                    }
                }
            }

        }
    }


}

@Preview(showBackground = true)
@Composable
fun ClientsScreenPreview() {
    val navController = rememberNavController()
    val clientListUseCase = ClientListUseCase(
        repository = object : ClientListRepository {
            override suspend fun getClientsList(): Result<List<ClientsGeneric>> {
                return Result.success(
                    listOf(
                        ClientsGeneric(
                            id = "1",
                            name = "João",
                            email = "email@teste.com",
                            cellphone = "999999999"
                        ),
                        ClientsGeneric(
                            id = "1",
                            name = "João",
                            email = "email@teste.com",
                            cellphone = "999999999"
                        ),
                    )
                )
            }
        }
    )
    val viewModel = ClientsScreenViewModel(clientListUseCase)
    ClientsScreen(viewModel, navController)
}