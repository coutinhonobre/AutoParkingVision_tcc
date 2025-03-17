package com.github.coutinhonobre.ui.features.login.presentation.viewmodel


import androidx.lifecycle.viewModelScope
import com.github.coutinhonobre.domain.feature.login.usecase.LoginUseCase
import com.github.coutinhonobre.ui.base.viewmodel.BaseViewModel
import com.github.coutinhonobre.ui.base.viewmodel.ViewAction
import com.github.coutinhonobre.ui.base.viewmodel.ViewState
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : BaseViewModel<LoginViewAction, LoginViewState>() {

    override fun initialState(): LoginViewState = LoginViewState.Idle

    override fun handleEvent(action: LoginViewAction) {
        when (action) {
            LoginViewAction.LoginRetry -> {
                postState(LoginViewState.Idle)
            }
            is LoginViewAction.Login -> {
                viewModelScope.launch {
                    postState(LoginViewState.Loading)
                    loginUseCase.login(action.username, action.password)
                        .onSuccess {
                            postState(LoginViewState.LoginSuccess)
                        }.onFailure {
                            postState(LoginViewState.Error("Invalid credentials"))
                        }
                }
            }
        }
    }
}

sealed class LoginViewAction: ViewAction {
    data class Login(val username: String, val password: String) : LoginViewAction()
    data object LoginRetry : LoginViewAction()
}

sealed class LoginViewState: ViewState {
    data object Idle : LoginViewState()
    data object Loading : LoginViewState()
    data object LoginSuccess : LoginViewState()
    data class Error(val message: String) : LoginViewState()
}
