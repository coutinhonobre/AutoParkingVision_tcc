package com.github.coutinhonobre.ui.features.collaborators.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.github.coutinhonobre.domain.feature.collaborators.repository.CollaboratorRepository
import com.github.coutinhonobre.domain.feature.collaborators.usecase.GetCollaboratorsUseCase
import com.github.coutinhonobre.ui.components.ui.templates.GenericEmptyScreen
import com.github.coutinhonobre.ui.components.ui.templates.GenericErrorScreen
import com.github.coutinhonobre.ui.components.ui.templates.GenericLoadingScreen
import com.github.coutinhonobre.ui.features.collaborators.presentation.viewmodel.CollaboratorsScreenViewAction
import com.github.coutinhonobre.ui.features.collaborators.presentation.viewmodel.CollaboratorsScreenViewModel
import com.github.coutinhonobre.ui.features.collaborators.presentation.viewmodel.CollaboratorsScreenViewState
import com.github.coutinhonobre.ui.routers.Router

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollaboratorsScreen(
    viewModel: CollaboratorsScreenViewModel,
    navController: NavHostController
) {

    val viewState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Colaboradores")
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
                    navController.navigate("${Router.COLLABORATOR.name}/")
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Adicionar"
                )
                Text(text = "Adicionar")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (viewState) {
                is CollaboratorsScreenViewState.Loading -> {
                    GenericLoadingScreen()
                }

                is CollaboratorsScreenViewState.Error -> {
                    GenericErrorScreen(
                        errorMessage = "Erro ao buscar colaboradores",
                    ) {
                        viewModel.onEvent(CollaboratorsScreenViewAction.Retry)
                    }
                }

                is CollaboratorsScreenViewState.Empty -> {
                    GenericEmptyScreen(
                        emptyMessage = "Nenhum colaborador encontrado",
                    ) {
                        viewModel.onEvent(CollaboratorsScreenViewAction.Retry)
                    }
                }

                is CollaboratorsScreenViewState.Idle -> {
                    val collaborators = (viewState as CollaboratorsScreenViewState.Idle).collaborators

                    collaborators.forEach { collaborator ->
                        CollaboratorCard(
                            collaborator = collaborator,
                            onClick = {
                                navController.navigate("${Router.COLLABORATOR.name}/${collaborator.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CollaboratorsScreenPreview() {
    val navController = rememberNavController()

    CollaboratorsScreen(
        viewModel = CollaboratorsScreenViewModel(
            getCollaboratorsUseCase = GetCollaboratorsUseCase(
                collaboratorRepository = object : CollaboratorRepository {}
            )
        ),
        navController = navController
    )
}