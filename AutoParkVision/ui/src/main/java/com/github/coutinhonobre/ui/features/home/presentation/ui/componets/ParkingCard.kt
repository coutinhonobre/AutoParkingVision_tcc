package com.github.coutinhonobre.ui.features.home.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BeautifulParkingCard(
    totalSpaces: Int,
    availableSpaces: Int,
    status: String,
    onClick: () -> Unit,
    parkingName: String = "Estacionamento XYZ",
    modifier: Modifier = Modifier
) {
    val occupiedSpaces = totalSpaces - availableSpaces
    val occupancyRate = (occupiedSpaces.toDouble() / totalSpaces.toDouble()) * 100

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() }
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp)) // Sombra para profundidade
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        listOf(Color(0xFF6A1B9A), Color(0xFF8E24AA)) // Gradiente roxo
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Linha 1: Nome do estacionamento e status
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = parkingName,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                    Text(
                        text = status,
                        fontWeight = FontWeight.Bold,
                        color = when (status) {
                            "Aberto" -> Color.Green
                            "Lotado" -> Color.Yellow
                            else -> Color.Red
                        },
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Linha 2: Ocupadas e Disponíveis com ícones
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DirectionsCar,
                            contentDescription = "Vagas Ocupadas",
                            tint = Color.White,
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = Color(0xFFFF5252), // Vermelho para vagas ocupadas
                                    shape = CircleShape
                                )
                                .padding(8.dp)
                        )
                        Text(
                            text = "$occupiedSpaces",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 24.sp
                        )
                        Text(
                            text = "Ocupadas",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocalParking,
                            contentDescription = "Vagas Disponíveis",
                            tint = Color.White,
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = Color(0xFF4CAF50), // Verde para vagas disponíveis
                                    shape = CircleShape
                                )
                                .padding(8.dp)
                        )
                        Text(
                            text = "$availableSpaces",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 24.sp
                        )
                        Text(
                            text = "Disponíveis",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Linha 3: Taxa de Ocupação
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Taxa de Ocupação",
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Percent,
                            contentDescription = "Taxa de Ocupação",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "%.1f%%".format(occupancyRate),
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BeautifulParkingCardPreview() {
    BeautifulParkingCard(
        totalSpaces = 150,
        availableSpaces = 40,
        status = "Aberto",
        onClick = { /* Ação ao clicar */ }
    )
}
