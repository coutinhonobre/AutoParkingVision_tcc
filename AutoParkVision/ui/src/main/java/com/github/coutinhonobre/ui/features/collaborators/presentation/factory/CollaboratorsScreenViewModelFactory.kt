package com.github.coutinhonobre.ui.features.collaborators.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.coutinhonobre.domain.feature.collaborators.usecase.GetCollaboratorsUseCase
import com.github.coutinhonobre.ui.features.collaborators.presentation.viewmodel.CollaboratorsScreenViewModel

class CollaboratorsScreenViewModelFactory(
    private val getCollaboratorsUseCase: GetCollaboratorsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CollaboratorsScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CollaboratorsScreenViewModel(
                getCollaboratorsUseCase = getCollaboratorsUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}