package com.github.coutinhonobre.ui.features.collaborators.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.github.coutinhonobre.domain.feature.collaborators.usecase.GetCollaboratorsUseCase
import com.github.coutinhonobre.ui.base.viewmodel.BaseViewModel
import com.github.coutinhonobre.ui.base.viewmodel.ViewAction
import com.github.coutinhonobre.ui.base.viewmodel.ViewState
import com.github.coutinhonobre.ui.features.collaborators.model.CollaboratorModel
import kotlinx.coroutines.launch

class CollaboratorsScreenViewModel(
    private val getCollaboratorsUseCase: GetCollaboratorsUseCase
) : BaseViewModel<CollaboratorsScreenViewAction, CollaboratorsScreenViewState>() {

    override fun initialState(): CollaboratorsScreenViewState = CollaboratorsScreenViewState.Loading
    override fun handleEvent(action: CollaboratorsScreenViewAction) {
        when (action) {
            is CollaboratorsScreenViewAction.Retry -> {
                getCollaborators()
            }
        }
    }

    init {
        getCollaborators()
    }

    private fun getCollaborators() {
        viewModelScope.launch {
            postState(CollaboratorsScreenViewState.Loading)
            getCollaboratorsUseCase().onSuccess { collaborators ->
                if (collaborators.isEmpty()) {
                    postState(CollaboratorsScreenViewState.Empty)
                } else {
                    val collaborators = collaborators.map {
                        CollaboratorModel(
                            id = it.id,
                            name = it.name,
                            accessCode = it.accessCode,
                            status = when(it.status) {
                                com.github.coutinhonobre.domain.feature.collaborators.model.CollaboratorStatus.ACTIVE -> com.github.coutinhonobre.ui.features.collaborators.model.CollaboratorStatusModel.ACTIVE
                                com.github.coutinhonobre.domain.feature.collaborators.model.CollaboratorStatus.INACTIVE -> com.github.coutinhonobre.ui.features.collaborators.model.CollaboratorStatusModel.INACTIVE
                            }
                        )
                    }
                    postState(CollaboratorsScreenViewState.Idle(collaborators))
                }
            }.onFailure {
                postState(CollaboratorsScreenViewState.Error)
            }
        }
    }
}

sealed class CollaboratorsScreenViewAction: ViewAction {
    data object Retry: CollaboratorsScreenViewAction()
}

sealed class CollaboratorsScreenViewState: ViewState {
    data object Loading: CollaboratorsScreenViewState()
    data class Idle(
        val collaborators: List<CollaboratorModel>
    ): CollaboratorsScreenViewState()
    data object Empty: CollaboratorsScreenViewState()
    data object Error: CollaboratorsScreenViewState()
}