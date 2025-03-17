package com.github.coutinhonobre.ui.features.home.presentation.ui.componets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.coutinhonobre.ui.features.home.presentation.model.ParkingLogModel

@Composable
fun ParkingLogCard(
    parkingLogModel: ParkingLogModel
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = parkingLogModel.licensePlate,
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                val icon = if (parkingLogModel.eventType == "Entrada") {
                    Icons.Filled.ArrowUpward
                } else {
                    Icons.Filled.ArrowDownward
                }
                val iconColor = if (parkingLogModel.eventType == "Entrada") {
                    Color.Green
                } else {
                    Color.Red
                }

                Icon(
                    imageVector = icon,
                    contentDescription = parkingLogModel.eventType,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = parkingLogModel.eventType,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = parkingLogModel.timestamp,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ParkingLogCardPreview() {
    ParkingLogCard(
        parkingLogModel = ParkingLogModel(
            eventType = "Entrada",
            licensePlate = "ABC-1234",
            timestamp = "2021-09-01 12:00:00"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun ParkingLogCardPreviewExit() {
    ParkingLogCard(
        parkingLogModel = ParkingLogModel(
            eventType = "Saída",
            licensePlate = "ABC-1234",
            timestamp = "2021-09-01 12:00:00"
        )
    )
}