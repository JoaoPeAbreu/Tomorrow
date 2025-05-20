package com.example.tomorrow.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tomorrow.ui.states.LoginUiState
import com.example.tomorrow.ui.theme.TomorrowTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    uiState: LoginUiState,
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
) {
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val isError = uiState.error != null
        AnimatedVisibility(visible = isError) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.error)
            ) {
                val error = uiState.error ?: ""
                Text(
                    text = error,
                    Modifier
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.onError
                )
            }
        }
        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = "O que devo fazer amanhã?")
            Spacer(modifier = Modifier.size(16.dp))
            val textFieldModifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(8.dp)
            OutlinedTextField(
                value = uiState.email,
                onValueChange = uiState.onEmailChange,
                textFieldModifier,
                shape = RoundedCornerShape(25),
                label = {
                    Text(text = "Email")
                }
            )
            OutlinedTextField(
                value = uiState.password,
                onValueChange = uiState.onPasswordChange,
                textFieldModifier,
                shape = RoundedCornerShape(25),
                label = {
                    Text("Senha")
                },
            )
            Button(
                onClick = onLoginClick,
                Modifier
                    .fillMaxWidth(0.8f)
                    .padding(8.dp)
            ) {
                Text(text = "Entrar")
            }
            TextButton(
                onClick = onRegisterClick,
                Modifier
                    .fillMaxWidth(0.8f)
                    .padding(8.dp)
            ) {
                Text(text = "Cadastrar")
            }
        }
    }
}

@Preview(showBackground = true, name = "Default")
@Composable
fun LoginScreenPreview() {
    TomorrowTheme {
        LoginScreen(
            uiState = LoginUiState(),
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}

@Preview(showBackground = true, name = "with error")
@Composable
fun LoginScreen1Preview() {
    TomorrowTheme {
        LoginScreen(
            uiState = LoginUiState(
                error = "Erro ao fazer login"
            ),
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}