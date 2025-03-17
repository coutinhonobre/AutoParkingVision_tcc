package com.github.coutinhonobre.ui.features.parking.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.github.coutinhonobre.ui.features.parking.presentation.model.ParkingScreenModel
import java.util.UUID

@Composable
fun ParkingScreenForm(
    onSaveParkingClick: (ParkingScreenModel) -> Unit,
    parkingScreenModel: ParkingScreenModel
) {

    var name by remember { mutableStateOf(parkingScreenModel.name) }
    var capacityText by remember { mutableStateOf(parkingScreenModel.capacity.toString()) }
    val id by remember { mutableStateOf(parkingScreenModel.id) }

    var isEnabled by remember { mutableStateOf(false) }

    if (parkingScreenModel.id.isNotEmpty()) {
        isEnabled = true
    }

    fun validateForm() {
        val capacity = capacityText.toIntOrNull() ?: 0
        isEnabled = name.isNotEmpty() && capacity > 0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        id.isNotEmpty().takeIf { it }?.let{
            TextField(
                value = id,
                onValueChange = { },
                label = { Text("Id") },
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        TextField(
            value = name,
            onValueChange = {
                name = it
                validateForm()
            },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = capacityText,
            onValueChange = { capacityTemp ->
                capacityText = capacityTemp
                validateForm()
            },
            label = { Text("Capacidade") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        LoadingButton(
            isLoading = false,
            isEnabled = isEnabled,
            onClick = {
                val capacity = capacityText.toIntOrNull() ?: 0
                onSaveParkingClick(ParkingScreenModel(id = id, name = name, capacity = capacity))
            },
            buttonText = "Cadastrar"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ParkingScreenFormPreview() {
    ParkingScreenForm(
        parkingScreenModel = ParkingScreenModel(
            id = UUID.randomUUID().toString(),
            name = "Estacionamento Teste",
            capacity = 100
        ),
        onSaveParkingClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ParkingScreenFormCreatePreview() {
    ParkingScreenForm(
        parkingScreenModel = ParkingScreenModel(
            id = "",
            name = "",
            capacity = 0
        ),
        onSaveParkingClick = {}
    )
}

