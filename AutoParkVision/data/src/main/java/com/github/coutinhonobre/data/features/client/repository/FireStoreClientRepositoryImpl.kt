package com.github.coutinhonobre.data.features.client.repository

import com.github.coutinhonobre.domain.feature.client.model.Client
import com.github.coutinhonobre.domain.feature.client.repository.ClientRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FireStoreClientRepositoryImpl(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ClientRepository {
    override suspend fun saveClient(client: Client): Result<Boolean> {
        return try {
            val clientData = hashMapOf(
                "name" to client.name,
                "phone" to client.phone,
                "email" to client.email
            )

            if (client.id.isNotEmpty()) {
                db.collection("clients")
                    .document(client.id)
                    .set(clientData)
                    .await()
            } else {
                db.collection("clients")
                    .add(clientData)
                    .await()
            }

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addVehicleToClient(clientId: String, vehicleId: String): Result<Boolean> {
        return try {
            val clientRef = db.collection("clients")
                .document(clientId)
                .get()
                .await()

            if (!clientRef.exists()) {
                throw Exception("Cliente não encontrado")
            }

            val vehicles = clientRef.get("vehicles") as? MutableList<String> ?: mutableListOf()
            vehicles.add(vehicleId)

            db.collection("clients")
                .document(clientId)
                .update("vehicles", vehicles)
                .await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getClient(clientId: String): Result<Client> {
        return try {
            val clientRef = db.collection("clients")
                .document(clientId)
                .get()
                .await()

            if (!clientRef.exists()) {
                throw Exception("Cliente não encontrado")
            }

            var vehicles = emptyList<String>()
            kotlin.runCatching {
                vehicles = clientRef.get("vehicles") as? MutableList<String> ?: emptyList()
            }

            val client = Client(
                id = clientRef.id,
                name = clientRef.getString("name") ?: "",
                phone = clientRef.getString("phone") ?: "",
                email = clientRef.getString("email") ?: "",
                vehicles = vehicles
            )

            Result.success(client)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}