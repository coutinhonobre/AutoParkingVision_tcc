package com.github.coutinhonobre.ui.features.splash.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.github.coutinhonobre.ui.base.viewmodel.BaseViewModel
import com.github.coutinhonobre.ui.base.viewmodel.ViewAction
import com.github.coutinhonobre.ui.base.viewmodel.ViewState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : BaseViewModel<SplashViewAction, SplashViewState>() {
    override fun initialState(): SplashViewState = SplashViewState.Idle
    override fun handleEvent(action: SplashViewAction) {
        when (action) {
            SplashViewAction.NavigateToLogin -> {
                viewModelScope.launch {
                    delay(3000)
                    postState(SplashViewState.NavigateToLogin)
                }
            }
        }
    }
}

sealed class SplashViewAction: ViewAction {
    data object NavigateToLogin : SplashViewAction()
}

sealed class SplashViewState: ViewState {
    data object Idle : SplashViewState()
    data object NavigateToLogin : SplashViewState()
}
