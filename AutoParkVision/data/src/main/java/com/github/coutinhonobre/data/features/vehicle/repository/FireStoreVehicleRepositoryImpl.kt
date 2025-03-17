package com.github.coutinhonobre.data.features.vehicle.repository

import com.github.coutinhonobre.domain.feature.vehicle.model.AuthorizedVehicle
import com.github.coutinhonobre.domain.feature.vehicle.model.Vehicle
import com.github.coutinhonobre.domain.feature.vehicle.repository.VehicleRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FireStoreVehicleRepositoryImpl(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : VehicleRepository {
    override suspend fun searchAuthorizedVehicles(plate: String): Result<List<Vehicle>> {
        return suspendCoroutine { continuation ->
            Firebase.auth.signInAnonymously()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        db.collection("vehicles")
                            .whereEqualTo("plate", plate)
                            .get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val vehicleDocuments = task.result?.documents

                                    if (vehicleDocuments.isNullOrEmpty()) {
                                        continuation.resume(Result.failure(Exception("No vehicles found")))
                                    } else {
                                        val vehicles = vehicleDocuments.map { document ->
                                            Vehicle(
                                                id = document.id,
                                                plate = document.getString("plate") ?: "",
                                                model = document.getString("model") ?: "",
                                                color = document.getString("color") ?: "",
                                                year = document.getLong("year")?.toInt() ?: 0,
                                                brand = document.getString("brand") ?: "",
                                                authorized = AuthorizedVehicle.valueOf(
                                                    document.getString(
                                                        "authorized"
                                                    ) ?: "DENIED"
                                                )
                                            )
                                        }
                                        continuation.resume(Result.success(vehicles))
                                    }
                                } else {
                                    continuation.resume(Result.failure(Exception("Unknown error")))
                                }
                            }
                    } else {
                        continuation.resume(Result.failure(Exception("Unknown error")))
                    }
                }
        }
    }

    override suspend fun getAllVehicles(): Result<List<Vehicle>> {
        return suspendCoroutine { continuation ->
            Firebase.auth.signInAnonymously()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        db.collection("vehicles")
                            .get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val vehicleDocuments = task.result?.documents

                                    if (vehicleDocuments.isNullOrEmpty()) {
                                        continuation.resume(Result.failure(Exception("No vehicles found")))
                                    } else {
                                        val vehicles = vehicleDocuments.map { document ->
                                            Vehicle(
                                                id = document.id,
                                                plate = document.getString("plate") ?: "",
                                                model = document.getString("model") ?: "",
                                                color = document.getString("color") ?: "",
                                                year = document.getLong("year")?.toInt() ?: 0,
                                                brand = document.getString("brand") ?: "",
                                                authorized = AuthorizedVehicle.valueOf(
                                                    document.getString(
                                                        "authorized"
                                                    ) ?: "DENIED"
                                                )
                                            )
                                        }
                                        continuation.resume(Result.success(vehicles))
                                    }
                                } else {
                                    continuation.resume(Result.failure(Exception("Unknown error")))
                                }
                            }
                    } else {
                        continuation.resume(Result.failure(Exception("Unknown error")))
                    }
                }
        }
    }

    override suspend fun saveVehicle(vehicle: Vehicle): Result<Unit> {
        return suspendCoroutine { continuation ->
            Firebase.auth.signInAnonymously()
                .addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        val vehiclesCollection = db.collection("vehicles")

                        if (!vehicle.id.isNullOrEmpty()) {
                            // Se o ID existe, atualiza o veículo
                            vehiclesCollection.document(vehicle.id)
                                .set(
                                    mapOf(
                                        "plate" to vehicle.plate,
                                        "model" to vehicle.model,
                                        "color" to vehicle.color,
                                        "year" to vehicle.year,
                                        "brand" to vehicle.brand,
                                        "authorized" to vehicle.authorized.name
                                    )
                                )
                                .addOnSuccessListener {
                                    continuation.resume(Result.success(Unit))
                                }
                                .addOnFailureListener {
                                    continuation.resume(Result.failure(Exception("Erro ao atualizar veículo.")))
                                }
                        } else {
                            // Verifica se já existe um veículo com a mesma placa
                            vehiclesCollection.whereEqualTo("plate", vehicle.plate)
                                .get()
                                .addOnSuccessListener { documents ->
                                    if (!documents.isEmpty) {
                                        // Placa já cadastrada
                                        continuation.resume(Result.failure(Exception("Placa já cadastrada.")))
                                    } else {
                                        // Adiciona um novo veículo
                                        vehiclesCollection.add(
                                            mapOf(
                                                "plate" to vehicle.plate,
                                                "model" to vehicle.model,
                                                "color" to vehicle.color,
                                                "year" to vehicle.year,
                                                "brand" to vehicle.brand,
                                                "authorized" to vehicle.authorized.name
                                            )
                                        )
                                            .addOnSuccessListener { documentReference ->
                                                continuation.resume(Result.success(Unit))
                                            }
                                            .addOnFailureListener {
                                                continuation.resume(Result.failure(Exception("Erro ao salvar veículo.")))
                                            }
                                    }
                                }
                                .addOnFailureListener {
                                    continuation.resume(Result.failure(Exception("Erro ao verificar a placa.")))
                                }
                        }
                    } else {
                        continuation.resume(Result.failure(Exception("Erro ao autenticar usuário.")))
                    }
                }
        }
    }


    override suspend fun searchVehicleId(id: String): Result<Vehicle> {
        return suspendCoroutine { continuation ->
            db.collection("vehicles")
                .document(id)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result

                        if (document == null) {
                            continuation.resume(Result.failure(Exception("No vehicle found")))
                        } else {
                            val vehicle = Vehicle(
                                id = document.id,
                                plate = document.getString("plate") ?: "",
                                model = document.getString("model") ?: "",
                                color = document.getString("color") ?: "",
                                year = document.getLong("year")?.toInt() ?: 0,
                                brand = document.getString("brand") ?: "",
                                authorized = AuthorizedVehicle.valueOf(
                                    document.getString("authorized") ?: "DENIED"
                                )
                            )
                            continuation.resume(Result.success(vehicle))
                        }
                    } else {
                        continuation.resume(Result.failure(Exception("Unknown error")))
                    }
                }
        }
    }

    override suspend fun searchListVehiclePlates(plates: List<String>): Result<List<Vehicle>> {
        return suspendCoroutine { continuation ->
            Firebase.auth.signInAnonymously()
                .addOnCompleteListener { task ->
                    if (plates.isEmpty()) {
                        continuation.resume(Result.success(emptyList()))
                    } else {
                        if (task.isSuccessful) {
                            db.collection("vehicles")
                                .whereIn("plate", plates)
                                .get()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val vehicleDocuments = task.result?.documents

                                        if (vehicleDocuments.isNullOrEmpty()) {
                                            continuation.resume(Result.failure(Exception("No vehicles found")))
                                        } else {
                                            val vehicles = vehicleDocuments.map { document ->
                                                Vehicle(
                                                    id = document.id,
                                                    plate = document.getString("plate") ?: "",
                                                    model = document.getString("model") ?: "",
                                                    color = document.getString("color") ?: "",
                                                    year = document.getLong("year")?.toInt() ?: 0,
                                                    brand = document.getString("brand") ?: "",
                                                    authorized = AuthorizedVehicle.valueOf(
                                                        document.getString(
                                                            "authorized"
                                                        ) ?: "DENIED"
                                                    )
                                                )
                                            }
                                            continuation.resume(Result.success(vehicles))
                                        }
                                    } else {
                                        continuation.resume(Result.failure(Exception("Unknown error")))
                                    }
                                }
                        } else {
                            continuation.resume(Result.failure(Exception("Unknown error")))
                        }
                    }
                }
        }
    }
}