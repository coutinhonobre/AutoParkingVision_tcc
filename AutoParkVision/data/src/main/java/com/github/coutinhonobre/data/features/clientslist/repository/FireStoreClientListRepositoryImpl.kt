package com.github.coutinhonobre.data.features.clientslist.repository

import com.github.coutinhonobre.domain.feature.clientslist.exception.ClientListNoClientsFoundException
import com.github.coutinhonobre.domain.feature.clientslist.exception.ClientListUnknownErrorException
import com.github.coutinhonobre.domain.feature.clientslist.model.ClientsGeneric
import com.github.coutinhonobre.domain.feature.clientslist.repository.ClientListRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FireStoreClientListRepositoryImpl(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ClientListRepository {

    override suspend fun getClientsList(): Result<List<ClientsGeneric>> {
        return suspendCoroutine { continuation ->
            db.collection("clients")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val clientDocuments = task.result?.documents

                        if (clientDocuments.isNullOrEmpty()) {
                            continuation.resume(Result.failure(ClientListNoClientsFoundException()))
                        } else {
                            val clients = clientDocuments.map { document ->
                                ClientsGeneric(
                                    id = document.id,
                                    name = document.getString("name") ?: "",
                                    email = document.getString("email") ?: "",
                                    cellphone = document.getString("phone") ?: ""
                                )
                            }
                            continuation.resume(Result.success(clients))
                        }
                    } else {
                        continuation.resume(Result.failure(ClientListUnknownErrorException()))
                    }
                }

        }
    }

}