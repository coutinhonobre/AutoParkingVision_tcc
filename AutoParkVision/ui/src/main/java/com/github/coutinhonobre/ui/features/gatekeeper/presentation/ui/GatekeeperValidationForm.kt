package com.github.coutinhonobre.ui.features.gatekeeper.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.coutinhonobre.ui.components.ui.LoadingButton

@Composable
fun GatekeeperValidationForm(
    isLoading: Boolean,
    onValidateClick: (String) -> Unit,
    code: String
) {
    var code by remember { mutableStateOf(code) }
    var isEnabled by remember { mutableStateOf(code.isNotEmpty()) }

    fun validateForm() {
        isEnabled = code.isNotEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = code,
            onValueChange = {
                code = it
                validateForm()
            },
            label = { Text("Código do Porteiro") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )

        LoadingButton(
            isLoading = isLoading,
            isEnabled = isEnabled,
            onClick = {
                onValidateClick(code)
            },
            buttonText = "Validar"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GatekeeperValidationFormPreview() {
    GatekeeperValidationForm(
        isLoading = false,
        onValidateClick = {},
        code = "1111"
    )
}