package com.github.coutinhonobre.ui.features.googlecard.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CarRental
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeCard(
    title: String,
    description: String,
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(240.dp)
            .padding(8.dp)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(56.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TallNarrowCardPreview() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        WelcomeCard(
            title = "Controle de Entrada/Saída",
            description = "Registre a entrada e saída de veículos.",
            icon = Icons.Filled.CarRental,
            contentDescription = "Icone de carro"
        )
        WelcomeCard(
            title = "Login",
            description = "Faça login para acessar o sistema.",
            icon = Icons.Default.Lock,
            contentDescription = "Icone de carro"
        )
    }
}
