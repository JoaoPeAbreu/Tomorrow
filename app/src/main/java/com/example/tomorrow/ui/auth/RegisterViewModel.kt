package com.example.tomorrow.ui.auth

import android.util.Patterns
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class RegistrationUiState(
    val isLoading: Boolean = false,
    val registrationSuccess: Boolean = false,
    val errorMessage: String? = null
)


class RegisterViewModel() : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmpassword by mutableStateOf("")

    val nameHasErrors by derivedStateOf {
        if(name.isNotBlank()) !(name.length >= 3) else false
    }

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

    val confirmPasswordHasErrors by derivedStateOf {
        if(confirmpassword.isNotBlank()) !confirmpassword.equals(password) else false
    }

    val noErrosRegister by derivedStateOf {
        if(!nameHasErrors && !emailHasErrors && !passwordHasErrors && !confirmPasswordHasErrors) {
            true
        } else false
    }

    fun registerUser() {
        if (noErrosRegister) {
            _uiState.update { it.copy(errorMessage = null) }
        } else {
            _uiState.update { it.copy(errorMessage = "Corrija os erros indicados.") }
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                registrationSuccess = false,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()

                val user = authResult.user

                if (user != null) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name).build()

                    user.updateProfile(profileUpdates).await()

                    _uiState.update { it.copy(isLoading = false, registrationSuccess = true) }

                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            registrationSuccess = false,
                            errorMessage = "Erro ao criar usuário."
                        )
                    }
                }


            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        registrationSuccess = false,
                        errorMessage = e.message ?: "Um erro desconhecido ocorreu durante o cadastro"
                    )
                }
                e.printStackTrace()
            }
        }
    }

    /*fun resetState() {
        _uiState.update { RegistrationUiState() } // Reset to initial state
    }*/
}

