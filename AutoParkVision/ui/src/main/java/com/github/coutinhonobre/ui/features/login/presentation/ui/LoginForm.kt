package com.github.coutinhonobre.ui.features.login.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.coutinhonobre.ui.components.ui.LoadingButton

@Composable
fun LoginForm(
    isLoading: Boolean,
    onLoginClick: (String, String) -> Unit,
    onCreateAccountClick: () -> Unit,
    onForgetPasswordClick: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isEnabled by remember { mutableStateOf(true) }

    fun validateForm() {
        isEnabled = username.isNotEmpty() && password.isNotEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Gerencie seu estacionamento de forma eficiente e sem complicações.",
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        TextField(
            value = username,
            onValueChange = {
                username = it
                validateForm()
            },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        TextField(
            value = password,
            onValueChange = {
                password = it
                validateForm()
            },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        TextButton(
            onClick = { onForgetPasswordClick() },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = "Não lembro minha senha",
            )
        }

        LoadingButton(
            isLoading = isLoading,
            isEnabled = isEnabled,
            onClick = {
                onLoginClick(username, password)
            },
            buttonText = "Login"
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(top = 32.dp)
        ) {
            Text(
                text = "Não tem uma conta?",
                fontStyle = FontStyle.Italic
            )

            TextButton(
                onClick = {
                    onCreateAccountClick()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cadastrar-se")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun LoginFormPreview() {
    LoginForm(onLoginClick = { _, _ -> }, onCreateAccountClick = {}, onForgetPasswordClick = {}, isLoading = false)
}
