package com.github.coutinhonobre.ui.features.collaborator.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.github.coutinhonobre.domain.feature.collaborators.model.Collaborator
import com.github.coutinhonobre.domain.feature.collaborators.model.CollaboratorStatus
import com.github.coutinhonobre.domain.feature.collaborators.usecase.GetCollaboratorIdUseCase
import com.github.coutinhonobre.domain.feature.collaborators.usecase.SaveCollaboratorsUseCase
import com.github.coutinhonobre.ui.base.viewmodel.BaseViewModel
import com.github.coutinhonobre.ui.base.viewmodel.ViewAction
import com.github.coutinhonobre.ui.base.viewmodel.ViewState
import com.github.coutinhonobre.ui.features.collaborators.model.CollaboratorModel
import com.github.coutinhonobre.ui.features.collaborators.model.CollaboratorStatusModel
import kotlinx.coroutines.launch

class CollaboratorViewModel(
    private val collaboratorId: String,
    private val saveCollaboratorUseCase: SaveCollaboratorsUseCase,
    private val getCollaboratorIdUseCase: GetCollaboratorIdUseCase
) : BaseViewModel<CollaboratorViewAction, CollaboratorViewState>() {
    override fun initialState(): CollaboratorViewState = CollaboratorViewState.Loading

    init {
        collaboratorId.isNotEmpty().takeIf { it }?.let {
            viewModelScope.launch {
                postState(CollaboratorViewState.Loading)
                getCollaboratorIdUseCase(collaboratorId)
                    .onSuccess { collaborator ->
                        postState(
                            CollaboratorViewState.Idle(
                                CollaboratorModel(
                                    id = collaborator.id,
                                    name = collaborator.name,
                                    accessCode = collaborator.accessCode,
                                    status = when (collaborator.status) {
                                        CollaboratorStatus.ACTIVE -> CollaboratorStatusModel.ACTIVE
                                        CollaboratorStatus.INACTIVE -> CollaboratorStatusModel.INACTIVE
                                    }
                                )
                            )
                        )
                    }
                    .onFailure { postState(CollaboratorViewState.Error) }
            }
        } ?: run {
            postState(CollaboratorViewState.Idle(CollaboratorModel()))
        }
    }

    override fun handleEvent(action: CollaboratorViewAction) {
        when (action) {
            is CollaboratorViewAction.SaveCollaborator -> {
                viewModelScope.launch {
                    postState(CollaboratorViewState.Loading)
                    val collaborator = Collaborator(
                        id = action.collaborator.id,
                        name = action.collaborator.name,
                        accessCode = action.collaborator.accessCode,
                        status = when (action.collaborator.status) {
                            CollaboratorStatusModel.ACTIVE -> CollaboratorStatus.ACTIVE
                            CollaboratorStatusModel.INACTIVE -> CollaboratorStatus.INACTIVE
                        }
                    )
                    saveCollaboratorUseCase(
                        collaborator = collaborator
                    ).onSuccess {
                        postState(CollaboratorViewState.Success)
                    }.onFailure {
                        postState(CollaboratorViewState.Error)
                    }
                }
            }
        }
    }
}

sealed class CollaboratorViewState : ViewState {
    data object Loading : CollaboratorViewState()
    data object Success : CollaboratorViewState()
    data object Error : CollaboratorViewState()
    data class Idle(val collaborator: CollaboratorModel) : CollaboratorViewState()
}

sealed class CollaboratorViewAction : ViewAction {
    data class SaveCollaborator(
        val collaborator: CollaboratorModel
    ) : CollaboratorViewAction()
}

