package com.github.coutinhonobre.ui.features.client.presentation.ui

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
import com.github.coutinhonobre.ui.features.client.presentation.model.ClientScreenModel

@Composable
fun ClientScreenForm(
    clientScreenModel: ClientScreenModel,
    onSaveClient: (ClientScreenModel) -> Unit
) {

    val id by remember { mutableStateOf(clientScreenModel.id ?: "") }
    var name by remember { mutableStateOf(clientScreenModel.name) }
    var email by remember { mutableStateOf(clientScreenModel.email) }
    var cellphone by remember { mutableStateOf(clientScreenModel.cellphone) }

    var isEnabled by remember { mutableStateOf(id.isNotEmpty()) }

    fun validateForm() {
        val isNameValid = name.isNotEmpty()
        val isEmailValid = email.isNotEmpty()
        val isCellphoneValid = cellphone.isNotEmpty()

        isEnabled = isNameValid && isEmailValid && isCellphoneValid
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
            value = email,
            onValueChange = {
                email = it
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

        TextField(
            value = cellphone,
            onValueChange = {
                cellphone = it
                validateForm()
            },
            label = { Text("Celular") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        LoadingButton(
            isLoading = false,
            isEnabled = isEnabled,
            onClick = {
                val client = ClientScreenModel(
                    id = id,
                    name = name,
                    email = email,
                    cellphone = cellphone
                )
                onSaveClient(client)
            },
            buttonText = "Cadastrar"
        )


    }



}

@Preview(showBackground = true)
@Composable
fun ClientScreenFormPreview() {
    val clientScreenModel = ClientScreenModel(
        id = "1",
        name = "Teste",
        email = "email@email.com",
        cellphone = "999999999"
    )

    ClientScreenForm(
        clientScreenModel = clientScreenModel
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun ClientScreenFormIdNullPreview() {
    val clientScreenModel = ClientScreenModel(
        name = "Teste",
        email = "email@email.com",
        cellphone = "999999999"
    )

    ClientScreenForm(
        clientScreenModel = clientScreenModel
    ) {

    }
}