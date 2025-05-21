package com.example.tomorrow.ui.states

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val onEmailChange: (String) -> Unit = {},
    val onPasswordChange: (String) -> Unit = {},
    val isAuthenticated: Boolean = false,
    val error: String? = null
)