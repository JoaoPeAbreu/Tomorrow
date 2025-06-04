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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {}
) {

    val uiState by viewModel.uiState.collectAsState()
    var showPassword by remember { mutableStateOf(value = false) }
    var showConfirmPassword by remember { mutableStateOf(value = false) }

    LaunchedEffect(uiState.registrationSuccess) {
        if (uiState.registrationSuccess) {
            onRegisterSuccess()
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar"
                )
            }
        }

        Spacer(Modifier.height(8.dp))
        Text("Crie uma nova conta", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = viewModel.name,
            label = { Text("Nome") },
            onValueChange = { input -> viewModel.name = input },
            isError = viewModel.nameHasErrors,
            supportingText = {
                if (viewModel.nameHasErrors) {
                    Text("O nome deve ter pelo menos 3 dígitos.")
                }
            }
        )
        Spacer(Modifier.height(16.dp))


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
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.confirmpassword,
            label = { Text("Confirme a senha") },
            onValueChange = { input -> viewModel.confirmpassword = input },
            isError = viewModel.confirmPasswordHasErrors,
            supportingText = {
                if (viewModel.confirmPasswordHasErrors) {
                    Text("As duas senhas devem ser iguais.")
                }

            },
            visualTransformation = if (showConfirmPassword) {

                VisualTransformation.None

            } else {

                PasswordVisualTransformation()

            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                if (showConfirmPassword) {
                    IconButton(onClick = { showConfirmPassword = false }) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = "hide_password"
                        )
                    }
                } else {
                    IconButton(
                        onClick = { showConfirmPassword = true }) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = "hide_password"
                        )
                    }
                }
            }
        )
        Spacer(Modifier.height(24.dp))

        Button(onClick = { viewModel.registerUser(onRegisterSuccess) },
            enabled = !uiState.isLoading && viewModel.noErrosRegister,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (uiState.isLoading) "Carregando..." else "Cadastre-se")
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
