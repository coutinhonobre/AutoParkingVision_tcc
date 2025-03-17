package com.github.coutinhonobre.domain.feature.parkingactivitylog.model

enum class ParkingLogEventType(
    val description: String
) {
    ENTRY("Entrada"),
    EXIT("Saída");
}