package com.github.coutinhonobre.licenseplate.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface ViewAction

interface ViewState

abstract class BaseViewModel<A : ViewAction, S : ViewState> : ViewModel() {
    private val _uiState = MutableStateFlow(initialState())
    val uiState: StateFlow<S> get() = _uiState

    protected abstract fun initialState(): S

    fun onEvent(action: A) {
        handleEvent(action)
    }

    fun postState(state: S) {
        _uiState.value = state
    }

    protected abstract fun handleEvent(action: A)
}