package com.github.coutinhonobre.ui.features.vehicle.model

enum class AuthorizedVehicleScreenModel(
    val value: String
) {
    AUTHORIZED("Autorizado"),
    UNAUTHORIZED("Não autorizado"),
    PENDING("Pendente"),
    DENIED("Negado")
}

data class VehicleScreeModel(
    val id: String = "",
    val plate: String = "",
    val model: String = "",
    val color: String = "",
    val year: Int = 1900,
    val brand: String = "",
    val authorized: AuthorizedVehicleScreenModel = AuthorizedVehicleScreenModel.AUTHORIZED
)
