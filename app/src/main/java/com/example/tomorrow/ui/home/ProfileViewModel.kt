package com.example.tomorrow.ui.home

import android.util.Patterns
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class ProfileUiState(
    val isLoading: Boolean = false,
    val profileSuccess: Boolean = false,
    val errorMessage: String? = null
)

class ProfileViewModel() : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    val currentUser = auth.currentUser

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")

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

    val noErrosProfile by derivedStateOf {
        if(!nameHasErrors && !emailHasErrors && !passwordHasErrors) {
            true
        } else false
    }

    fun updateUser() {
        if (noErrosProfile) {
            _uiState.update { it.copy(errorMessage = null) }

            _uiState.update {
                it.copy(
                    isLoading = true,
                    profileSuccess = false,
                    errorMessage = null
                )
            }

            viewModelScope.launch {
                try {
                    currentUser?.let { user ->
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(name).build()
                        user.updateProfile(profileUpdates).await()

                        if (email != user.email) {
                            user.updateEmail(email).await()
                        }

                        if (password.isNotBlank()) {
                            user.updatePassword(password).await()
                        }

                        _uiState.update { it.copy(isLoading = false, profileSuccess = true) }
                    } ?: run {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "Nenhum usuário logado"
                            )
                        }
                    }
                } catch (e: Exception) {
                    val errorMessage: String = when (e) {
                        is FirebaseAuthInvalidUserException -> {
                            "Sua sessão expirou ou houve um problema com sua conta. Por favor, faça login novamente."
                        }

                        is FirebaseNetworkException -> {
                            "Problema de conexão. Verifique sua internet e tente novamente."
                        }

                        else -> {
                            e.message ?: "Erro ao atualizar perfil"
                        }
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            profileSuccess = false,
                            errorMessage = e.message ?: errorMessage
                        )
                    }
                }
            }
        }
    }

}