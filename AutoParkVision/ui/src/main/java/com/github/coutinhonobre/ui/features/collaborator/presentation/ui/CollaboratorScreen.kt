package com.github.coutinhonobre.ui.features.collaborator.presentation.ui

import androidx.compose.foundation.layout.Column
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
import androidx.navigation.compose.rememberNavController
import com.github.coutinhonobre.domain.feature.collaborators.repository.CollaboratorRepository
import com.github.coutinhonobre.domain.feature.collaborators.usecase.GetCollaboratorIdUseCase
import com.github.coutinhonobre.domain.feature.collaborators.usecase.SaveCollaboratorsUseCase
import com.github.coutinhonobre.ui.components.ui.appbar.GenericTopAppBar
import com.github.coutinhonobre.ui.components.ui.templates.GenericErrorScreen
import com.github.coutinhonobre.ui.components.ui.templates.GenericLoadingScreen
import com.github.coutinhonobre.ui.features.client.presentation.viewmodel.ClientViewAction
import com.github.coutinhonobre.ui.features.client.presentation.viewmodel.ClientViewState
import com.github.coutinhonobre.ui.features.collaborator.presentation.viewmodel.CollaboratorViewAction
import com.github.coutinhonobre.ui.features.collaborator.presentation.viewmodel.CollaboratorViewModel
import com.github.coutinhonobre.ui.features.collaborator.presentation.viewmodel.CollaboratorViewState
import com.github.coutinhonobre.ui.features.collaborators.model.CollaboratorModel
import com.github.coutinhonobre.ui.features.success.GenericSuccessScreen
import com.github.coutinhonobre.ui.routers.Router

@Composable
fun CollaboratorScreen(
    navController: NavController,
    collaboratorViewModel: CollaboratorViewModel
) {

    val viewState by collaboratorViewModel.uiState.collectAsState()
    var collaboratorModel by remember { mutableStateOf(CollaboratorModel()) }

    Scaffold(
        topBar = {
            GenericTopAppBar(
                titleText = "Colaborador",
            ) {
                navController.popBackStack()
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            when (viewState) {
                is CollaboratorViewState.Idle -> {
                    CollaboratorScreenForm(
                        collaboratorModel = (viewState as CollaboratorViewState.Idle).collaborator
                    ) {
                        collaboratorViewModel.onEvent(
                            CollaboratorViewAction.SaveCollaborator(
                                it
                            )
                        )
                    }
                }
                CollaboratorViewState.Loading -> {
                    GenericLoadingScreen()
                }
                is CollaboratorViewState.Success -> {
                    GenericSuccessScreen {
                        navController.navigate(Router.COLLABORATORS.name) {
                            popUpTo(Router.COLLABORATOR.name) { inclusive = false }
                        }
                    }
                }
                CollaboratorViewState.Error -> {
                    GenericErrorScreen(
                        errorMessage = "Erro ao salvar colaborador"
                    ) {
                        collaboratorViewModel.onEvent(CollaboratorViewAction.SaveCollaborator(collaboratorModel))
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun CollaboratorScreenPreview() {
    val navController = rememberNavController()

    val viewModel = CollaboratorViewModel(
        collaboratorId = "",
        saveCollaboratorUseCase = SaveCollaboratorsUseCase(
            collaboratorRepository = object : CollaboratorRepository {

            }
        ),
        getCollaboratorIdUseCase = GetCollaboratorIdUseCase(
            collaboratorRepository = object : CollaboratorRepository {

            }
        )
    )

    CollaboratorScreen(
        navController = navController,
        collaboratorViewModel = viewModel
    )
}