package com.github.coutinhonobre.domain.feature.client.model

data class Client(
    val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val vehicles: List<String> = emptyList()
)
