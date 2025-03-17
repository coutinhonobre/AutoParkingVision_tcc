package com.github.coutinhonobre.ui.features.signup.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.github.coutinhonobre.domain.feature.signup.exception.SignUpAuthUserCollisionException
import com.github.coutinhonobre.domain.feature.signup.exception.SignUpGenericException
import com.github.coutinhonobre.domain.feature.signup.exception.SignUpPasswordsDoNotMatchException
import com.github.coutinhonobre.domain.feature.signup.usecase.SignUpUseCase
import com.github.coutinhonobre.ui.base.viewmodel.BaseViewModel
import com.github.coutinhonobre.ui.base.viewmodel.ViewAction
import com.github.coutinhonobre.ui.base.viewmodel.ViewState
import com.github.coutinhonobre.ui.features.signup.presentation.model.SignUpErrorType
import kotlinx.coroutines.launch

class SignUpScreenViewModel(
    private val signUpUseCase: SignUpUseCase
) : BaseViewModel<SignUpScreenViewAction, SignUpScreenViewState>() {

    override fun initialState(): SignUpScreenViewState = SignUpScreenViewState.Idle

    override fun handleEvent(action: SignUpScreenViewAction) {
        when (action) {
            is SignUpScreenViewAction.SignUp -> {
                viewModelScope.launch {
                    postState(SignUpScreenViewState.Loading)

                    signUpUseCase.execute(
                        action.email,
                        action.password,
                        action.confirmPassword
                    ).onSuccess {
                        postState(SignUpScreenViewState.Success)
                    }.onFailure { error ->
                        when (error) {
                            is SignUpPasswordsDoNotMatchException -> postState(
                                SignUpScreenViewState.Failure(
                                    SignUpErrorType.PASSWORDS_DO_NOT_MATCH
                                )
                            )
                            is SignUpAuthUserCollisionException -> postState(
                                SignUpScreenViewState.Failure(
                                    SignUpErrorType.EMAIL_ALREADY_REGISTERED
                                )
                            )
                            is SignUpGenericException -> postState(
                                SignUpScreenViewState.Failure(
                                    SignUpErrorType.GENERIC
                                )
                            )
                            else -> postState(SignUpScreenViewState.Failure(SignUpErrorType.GENERIC))
                        }

                    }
                }
            }
            is SignUpScreenViewAction.SignUpRetry -> {
                postState(SignUpScreenViewState.Idle)
            }
        }
    }

}

sealed class SignUpScreenViewAction : ViewAction {
    data class SignUp(
        val email: String,
        val password: String,
        val confirmPassword: String
    ) : SignUpScreenViewAction()
    data object SignUpRetry : SignUpScreenViewAction()
}

sealed class SignUpScreenViewState : ViewState {
    data object Idle : SignUpScreenViewState()
    data object Loading : SignUpScreenViewState()
    data object Success : SignUpScreenViewState()
    data class Failure(val typeError: SignUpErrorType) : SignUpScreenViewState()
}