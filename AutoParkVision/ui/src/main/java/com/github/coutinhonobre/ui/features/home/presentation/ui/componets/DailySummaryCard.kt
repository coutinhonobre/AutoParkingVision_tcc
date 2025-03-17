package com.github.coutinhonobre.ui.features.home.presentation.ui.componets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DailySummaryCard(
    totalEntries: Int,
    totalExits: Int,
    totalCapacity: Int
) {
    val occupiedSpots = (totalEntries - totalExits).coerceAtLeast(0)
    val availableSpots = (totalCapacity - occupiedSpots).coerceAtLeast(0)

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inversePrimary)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            TitleText("Resumo do Estacionamento")
            Spacer(modifier = Modifier.height(16.dp))

            RowData(
                leftData = "Capacidade Máxima",
                leftValue = "$totalCapacity",
                rightData = "Vagas Disponíveis",
                rightValue = "$availableSpots"
            )

            Spacer(modifier = Modifier.height(16.dp))

            RowData(
                leftData = "Entradas",
                leftValue = "$totalEntries",
                rightData = "Saídas",
                rightValue = "$totalExits"
            )

        }
    }
}

@Composable
private fun TitleText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
    )
}

@Composable
private fun RowData(leftData: String, leftValue: String, rightData: String, rightValue: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DataColumn(dataLabel = leftData, dataValue = leftValue)
        if (rightData.isNotEmpty()) {
            DataColumn(dataLabel = rightData, dataValue = rightValue)
        }
    }
}

@Composable
private fun DataColumn(dataLabel: String, dataValue: String) {
    Column {
        Text(
            text = dataLabel,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
        )
        Text(
            text = dataValue,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ParkingSummaryCardPreview() {
    DailySummaryCard(
        totalEntries = 75,
        totalExits = 60,
        totalCapacity = 100
    )
}
