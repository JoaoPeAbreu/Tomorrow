package com.example.tomorrow.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tomorrow.authentication.FirebaseAuthRepository
import com.example.tomorrow.ui.states.LoginUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class LoginViewModel(
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()
    val isAuthenticated = firebaseAuthRepository.currentUser
        .map {
            it != null
        }

    init {
        _uiState.update { currentState ->
            currentState.copy(
                onEmailChange = { user ->
                    _uiState.update {
                        it.copy(email = user)
                    }
                },
                onPasswordChange = { password ->
                    _uiState.update {
                        it.copy(password = password)
                    }
                }
            )
        }
    }

    suspend fun login() {
        try {
            firebaseAuthRepository
                .login(
                    email = _uiState.value.email,
                    password = _uiState.value.password
                )
        } catch (e: Exception) {
            Log.e("LoginViewModel", "login: ", e)
            _uiState.update {
                it.copy(error = "Erro ao fazer login")
            }
            delay(3000)
            _uiState.update {
                it.copy(error = null)
            }
        }
    }

}