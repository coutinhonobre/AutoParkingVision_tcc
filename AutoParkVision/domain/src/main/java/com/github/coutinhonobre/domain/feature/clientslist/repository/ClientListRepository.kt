package com.github.coutinhonobre.domain.feature.clientslist.repository

import com.github.coutinhonobre.domain.feature.clientslist.model.ClientsGeneric

interface ClientListRepository {
    suspend fun getClientsList(): Result<List<ClientsGeneric>>
}
