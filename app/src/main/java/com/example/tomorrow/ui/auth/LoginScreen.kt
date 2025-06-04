package com.example.tomorrow.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onNavigateToRegister: () -> Unit = {},
    onLoginSuccess: () -> Unit = {}
) {

    val uiState by viewModel.uiState.collectAsState()
    var showPassword by remember { mutableStateOf(value = false) }

    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            onLoginSuccess()
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = viewModel.email,
            label = { Text("Email") },
            onValueChange = { input -> viewModel.email = input },
            isError = viewModel.emailHasErrors,
            supportingText = {
                if (viewModel.emailHasErrors) {
                    Text("Formato de email incorreto.")
                }
            }
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.password,
            label = { Text("Senha") },
            onValueChange = { input -> viewModel.password = input },
            isError = viewModel.passwordHasErrors,
            supportingText = {
                if (viewModel.passwordHasErrors) {
                    Text("A senha deve ter pelo menos 6 dígitos.")
                }
            },
            visualTransformation = if (showPassword) {

                VisualTransformation.None

            } else {

                PasswordVisualTransformation()

            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                if (showPassword) {
                    IconButton(onClick = { showPassword = false }) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = "hide_password"
                        )
                    }
                } else {
                    IconButton (
                        onClick = { showPassword = true }) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = "hide_password"
                        )
                    }
                }
            }
        )
        Spacer(Modifier.height(24.dp))

        Button(onClick = { viewModel.loginUser(onLoginSuccess) },
            enabled = !uiState.isLoading && viewModel.noErrosRegister && !uiState.alreadyLogged,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (uiState.isLoading) "Carregando..." else "Login")
        }

        Spacer(Modifier.height(8.dp))

        TextButton(
            onClick = onNavigateToRegister,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Criar uma nova conta")
        }

        if (uiState.isLoading) {
            Spacer(Modifier.height(16.dp))
            CircularProgressIndicator()
        }

        uiState.errorMessage?.let { message ->
            Spacer(Modifier.height(16.dp))
            Text(message, color = MaterialTheme.colorScheme.error)
        }
    }
}
