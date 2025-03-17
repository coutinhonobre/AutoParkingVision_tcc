package com.github.coutinhonobre.ui.features.vehicle.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.coutinhonobre.ui.components.ui.LoadingButton
import com.github.coutinhonobre.ui.features.vehicle.model.AuthorizedVehicleScreenModel
import com.github.coutinhonobre.ui.features.vehicle.model.VehicleScreeModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleScreenForm(
    vehicleScreeModel: VehicleScreeModel,
    onSaveVehicle: (VehicleScreeModel) -> Unit
) {
    val id by remember { mutableStateOf(vehicleScreeModel.id ?: "") }
    var plate by remember { mutableStateOf(vehicleScreeModel.plate) }
    var model by remember { mutableStateOf(vehicleScreeModel.model) }
    var color by remember { mutableStateOf(vehicleScreeModel.color) }
    var year by remember { mutableStateOf(vehicleScreeModel.year) }
    var brand by remember { mutableStateOf(vehicleScreeModel.brand) }
    var authorized by remember { mutableStateOf(vehicleScreeModel.authorized) }

    var isEnabled by remember { mutableStateOf(id.isNotEmpty()) }

    fun validateForm() {
        val isPlateValid = plate.isNotEmpty()
        val isModelValid = model.isNotEmpty()
        val isColorValid = color.isNotEmpty()
        val isYearValid = year.toString().isNotEmpty()
        val isBrandValid = brand.isNotEmpty()

        isEnabled = isPlateValid && isModelValid && isColorValid && isYearValid && isBrandValid
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        id.isNotEmpty().takeIf { it }?.let {
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
            value = plate,
            onValueChange = {
                plate = it
                validateForm()
            },
            label = { Text("Placa") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = model,
            onValueChange = {
                model = it
                validateForm()
            },
            label = { Text("Modelo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = color,
            onValueChange = {
                color = it
                validateForm()
            },
            label = { Text("Cor") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = year.toString(),
            onValueChange = {
                year = it.toIntOrNull() ?: year
                validateForm()
            },
            label = { Text("Ano") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = brand,
            onValueChange = {
                brand = it
                validateForm()
            },
            label = { Text("Marca") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown para selecionar o status
        var expanded by remember { mutableStateOf(false) }
        var selectedStatus by remember { mutableStateOf(authorized) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            TextField(
                value = selectedStatus.value,
                onValueChange = {},
                readOnly = true,
                label = { Text("Status") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                AuthorizedVehicleScreenModel.entries.forEach { status ->
                    DropdownMenuItem(
                        text = { Text(status.value) },
                        onClick = {
                            selectedStatus = status
                            authorized = status
                            validateForm()
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LoadingButton(
            isLoading = false,
            isEnabled = isEnabled,
            onClick = {
                val vehicle = VehicleScreeModel(
                    id = id,
                    plate = plate,
                    model = model,
                    color = color,
                    year = year,
                    brand = brand,
                    authorized = authorized
                )
                onSaveVehicle(vehicle)
            },
            buttonText = "Salvar"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VehicleScreenFormPreview() {
    VehicleScreenForm(
        vehicleScreeModel = VehicleScreeModel(
            id = "123",
            plate = "ABC1234",
            model = "SUV",
            color = "Preto",
            year = 2022,
            brand = "Toyota",
            authorized = AuthorizedVehicleScreenModel.AUTHORIZED
        )
    ) {

    }
}
