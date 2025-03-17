package com.github.coutinhonobre.ui.features.vehicles.myvehicles.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.coutinhonobre.ui.components.ui.LoadingButton

@Composable
fun AddMyVehicles(
    onAddVehicleClick: (String) -> Unit
) {

    var placa by remember { mutableStateOf("") }
    var isEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    fun validateForm() {
        isEnabled = placa.isNotEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Adicione seus veículos aqui",
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Spacer(modifier = Modifier.padding(8.dp))

        TextField(
            value = placa,
            onValueChange = {
                placa = it
                validateForm()
            },
            label = { Text("Placa") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        LoadingButton(
            isLoading = isLoading,
            isEnabled = isEnabled,
            onClick = {
                isLoading = true
                onAddVehicleClick(placa)
            },
            buttonText = "Login"
        )
    }

}

@Preview(showBackground = true)
@Composable
fun AddMyVehiclesPreview() {
    AddMyVehicles {}
}