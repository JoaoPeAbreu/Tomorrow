package com.example.tomorrow.ui.auth

import android.util.Patterns
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class LoginUiState(
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val errorMessage: String? = null,
    var alreadyLogged: Boolean = false
)


class LoginViewModel() : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    val currentUser = auth.currentUser


    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    var email by mutableStateOf("")
    var password by mutableStateOf("")

    val emailHasErrors by derivedStateOf {
        if (email.isNotBlank()) {
            !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            false
        }
    }

    val passwordHasErrors by derivedStateOf {
        if(password.isNotBlank()) !(password.length >= 6) else false
    }

    val noErrosRegister by derivedStateOf {
        if(!emailHasErrors && !passwordHasErrors) {
            true
        } else false
    }
    fun loginUser(onSuccess: () -> Unit) {

        if (currentUser != null) {
            _uiState.update { it.copy(alreadyLogged = true) }
            onSuccess()
        } else {
            _uiState.update { it.copy(alreadyLogged = false) }

            if (noErrosRegister) {
                _uiState.update { it.copy(errorMessage = null) }
            } else {
                _uiState.update { it.copy(errorMessage = "Corrija os erros indicados.") }
            }

            _uiState.update {
                it.copy(
                    isLoading = true,
                    loginSuccess = false,
                    errorMessage = null
                )
            }

            viewModelScope.launch {
                try {
                    val authResult = auth.signInWithEmailAndPassword(email, password).await()

                    val user = authResult.user

                    if (user != null) {
                        _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
                        onSuccess()

                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                loginSuccess = false,
                                errorMessage = "User object was null after creation."
                            )
                        }
                    }


                } catch (e: Exception) {
                    val errorMessage: String = when (e) {

                        is FirebaseAuthInvalidUserException -> {
                            "Não encontramos um usuário com este e-mail ou a conta foi desativada."
                        }

                        is FirebaseAuthInvalidCredentialsException -> {
                            "O e-mail ou a senha está incorreta. Por favor, tente novamente."
                        }

                        is FirebaseTooManyRequestsException -> {
                            "Muitas tentativas de login falharam. Tente novamente mais tarde."
                        }

                        else -> {
                            e.message ?: "Um erro desconhecido ocorreu durante o login."
                        }
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loginSuccess = false,
                            errorMessage = e.message ?: errorMessage
                        )
                    }
                }
            }
        }
    }

    /*fun resetState() {
        _uiState.update { RegistrationUiState() } // Reset to initial state
    }*/
}

