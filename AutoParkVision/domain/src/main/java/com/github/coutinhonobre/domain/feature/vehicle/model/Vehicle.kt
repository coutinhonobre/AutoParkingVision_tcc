package com.github.coutinhonobre.domain.feature.vehicle.model


enum class AuthorizedVehicle {
    AUTHORIZED,
    UNAUTHORIZED,
    PENDING,
    DENIED
}

data class Vehicle(
    val id: String,
    val plate: String,
    val model: String,
    val color: String,
    val year: Int,
    val brand: String,
    val authorized: AuthorizedVehicle
)
