package com.github.coutinhonobre.ui.features.forgotpassword.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedButton
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
fun ForgotPassword(
    isLoading: Boolean,
    onSendEmailClick: (String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var isEnabled by remember { mutableStateOf(true) }

    fun validateForm() {
        isEnabled = username.isNotEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Digite seu email para recuperar sua senha",
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
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            LoadingButton(
                isLoading = isLoading,
                isEnabled = isEnabled,
                onClick = {
                    onSendEmailClick(username)
                },
                buttonText = "Login"
            )

            Spacer(modifier = Modifier.height(16.dp))


        }

    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordPreview() {
    ForgotPassword(
        isLoading = false,
        onSendEmailClick = {}
    )
}