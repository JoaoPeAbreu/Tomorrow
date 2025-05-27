package com.example.tomorrow.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.tomorrow.data.UserEntity


@Composable
fun RegisterScreen(viewModel: RegisterViewModel = viewModel()) {

    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmpassword by remember { mutableStateOf("") }

    Column (modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = name,
            label = { Text("Nome") },
            onValueChange = { input -> viewModel.updateName(input) },
            isError = viewModel.nameHasErrors,
            supportingText = {
                if (viewModel.nameHasErrors) {
                    Text("O nome deve ter pelo menos 3 dígitos.")
                }
            }
        )

        OutlinedTextField(
            value = email,
            label = { Text("Email") },
            onValueChange = { input -> viewModel.updateEmail(input) },
            isError = viewModel.emailHasErrors,
            supportingText = {
                if (viewModel.emailHasErrors) {
                    Text("Formato de email incorreto.")
                }
            }
        )

        OutlinedTextField(
            value = password,
            label = { Text("Senha") },
            onValueChange = { input -> viewModel.updatePassword(input) },
            isError = viewModel.passwordHasErrors,
            supportingText = {
                if (viewModel.passwordHasErrors) {
                    Text("A senha deve ter pelo menos 6 dígitos.")
                }
            }
        )

        OutlinedTextField(
            value = confirmpassword,
            label = { Text("Confirme a senha") },
            onValueChange = { input -> viewModel.updateConfirmPassword(input) },
            isError = viewModel.confirmPasswordHasErrors,
            supportingText = {
                if (viewModel.confirmPasswordHasErrors) {
                    Text("As duas senhas devem ser iguais.")
                }
            }
        )
        Button(onClick = {
            scope.launch { viewModel.insert(UserEntity(
                name = name, email = email, password = password,
                id = TODO()
            )) }
        }) {
            Text("Cadastrar")
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}