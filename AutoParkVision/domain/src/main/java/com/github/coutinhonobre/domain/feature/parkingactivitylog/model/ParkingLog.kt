package com.github.coutinhonobre.domain.feature.parkingactivitylog.model

data class ParkingLog(
    val eventType: ParkingLogEventType,
    val licensePlate: String,
    val timestamp: String,
    val emailUser: String,
    val id: String
)
