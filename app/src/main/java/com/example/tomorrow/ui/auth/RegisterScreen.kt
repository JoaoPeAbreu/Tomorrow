package com.example.tomorrow.ui.auth

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
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
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.tomorrow.data.UserEntity
import com.example.tomorrow.ui.theme.TomorrowTheme


@Composable
fun RegisterScreen(viewModel: RegisterViewModel = viewModel()) {

    val scope = rememberCoroutineScope()
    var showPassword by remember { mutableStateOf(value = false) }
    var showConfirmPassword by remember { mutableStateOf(value = false) }


    Column (modifier = Modifier.padding(16.dp)) {
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

        Button(onClick = {
            scope.launch { viewModel.insert(UserEntity(
                name = viewModel.name, email = viewModel.email, password = viewModel.password,
                id = TODO()
            )) }

            print(viewModel.showUser(viewModel.name))
        }) {
            Text("Cadastrar")
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    TomorrowTheme {
        RegisterScreen()
    }

}