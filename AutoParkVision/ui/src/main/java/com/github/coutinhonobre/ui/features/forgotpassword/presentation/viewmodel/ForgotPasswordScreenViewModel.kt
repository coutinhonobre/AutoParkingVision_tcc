package com.github.coutinhonobre.ui.features.forgotpassword.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.github.coutinhonobre.domain.feature.forgotpassword.usecase.ForgotPasswordUseCase
import com.github.coutinhonobre.ui.base.viewmodel.BaseViewModel
import com.github.coutinhonobre.ui.base.viewmodel.ViewAction
import com.github.coutinhonobre.ui.base.viewmodel.ViewState
import kotlinx.coroutines.launch

class ForgotPasswordScreenViewModel(
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : BaseViewModel<ForgotPasswordScreenViewAction, ForgotPasswordScreenViewState>() {
    override fun initialState(): ForgotPasswordScreenViewState = ForgotPasswordScreenViewState.Idle

    override fun handleEvent(action: ForgotPasswordScreenViewAction) {
        when (action) {
            is ForgotPasswordScreenViewAction.ForgotPassword -> {
                viewModelScope.launch {
                    postState(ForgotPasswordScreenViewState.Loading)

                    forgotPasswordUseCase.forgotPassword(action.email).onSuccess {
                        postState(ForgotPasswordScreenViewState.Success)
                    }.onFailure {
                        postState(ForgotPasswordScreenViewState.Failure)
                    }
                }
            }
            is ForgotPasswordScreenViewAction.ForgotPasswordRetry -> {
                postState(ForgotPasswordScreenViewState.Idle)
            }
        }
    }
}

sealed class ForgotPasswordScreenViewAction : ViewAction {
    data class ForgotPassword(
        val email: String
    ) : ForgotPasswordScreenViewAction()
    data object ForgotPasswordRetry : ForgotPasswordScreenViewAction()
}

sealed class ForgotPasswordScreenViewState : ViewState {
    data object Idle : ForgotPasswordScreenViewState()
    data object Loading : ForgotPasswordScreenViewState()
    data object Success : ForgotPasswordScreenViewState()
    data object Failure : ForgotPasswordScreenViewState()
}
