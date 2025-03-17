package com.github.coutinhonobre.ui.features.collaborator.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.coutinhonobre.ui.components.ui.LoadingButton
import com.github.coutinhonobre.ui.features.collaborators.model.CollaboratorModel
import com.github.coutinhonobre.ui.features.collaborators.model.CollaboratorStatusModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollaboratorScreenForm(
    collaboratorModel: CollaboratorModel,
    onSaveCollaborator: (CollaboratorModel) -> Unit
) {

    val id by remember { mutableStateOf(collaboratorModel.id) }
    var name by remember { mutableStateOf(collaboratorModel.name) }
    var accessCode by remember {
        mutableStateOf(collaboratorModel.accessCode.ifEmpty { generateAccessCode() })
    }
    var statusCollaborator by remember { mutableStateOf(collaboratorModel.status) }

    var isEnabled by remember { mutableStateOf(id.isNotEmpty()) }

    fun validateForm() {
        val isNameValid = name.isNotEmpty()
        val isAccessCodeValid = accessCode.isNotEmpty()

        isEnabled = isNameValid && isAccessCodeValid
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = accessCode,
                onValueChange = {},
                label = { Text("Código de Acesso") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                readOnly = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = { accessCode = generateAccessCode() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Gerar Código"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        var expanded by remember { mutableStateOf(false) }
        var selectedStatus by remember { mutableStateOf(statusCollaborator) }

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
                CollaboratorStatusModel.entries.forEach { selected ->
                    DropdownMenuItem(
                        text = { Text(selected.value) },
                        onClick = {
                            selectedStatus = selected
                            statusCollaborator = selected
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
                val collaborator = CollaboratorModel(
                    id = id,
                    name = name,
                    accessCode = accessCode,
                    status = statusCollaborator
                )
                onSaveCollaborator(collaborator)
            },
            buttonText = "Salvar"
        )

    }

}

fun generateAccessCode(): String {
    val sdf = SimpleDateFormat("mmHHddMMssSSS", Locale.getDefault())
    return sdf.format(Date())
}

@Preview(showBackground = true)
@Composable
fun CollaboratorScreenFormPreview() {
    CollaboratorScreenForm(
        collaboratorModel = CollaboratorModel(),
        onSaveCollaborator = {}
    )
}