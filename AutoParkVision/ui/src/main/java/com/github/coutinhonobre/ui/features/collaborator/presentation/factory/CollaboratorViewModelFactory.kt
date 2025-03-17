package com.github.coutinhonobre.ui.features.collaborator.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.coutinhonobre.domain.feature.collaborators.usecase.GetCollaboratorIdUseCase
import com.github.coutinhonobre.domain.feature.collaborators.usecase.SaveCollaboratorsUseCase
import com.github.coutinhonobre.ui.features.collaborator.presentation.viewmodel.CollaboratorViewModel

class CollaboratorViewModelFactory(
    private val collaboratorId: String,
    private val saveCollaboratorsUseCase: SaveCollaboratorsUseCase,
    private val getIdCollaboratorUseCase: GetCollaboratorIdUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CollaboratorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CollaboratorViewModel(
                collaboratorId = collaboratorId,
                saveCollaboratorUseCase = saveCollaboratorsUseCase,
                getCollaboratorIdUseCase = getIdCollaboratorUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}