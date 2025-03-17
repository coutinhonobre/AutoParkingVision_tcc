package com.github.coutinhonobre.data.features.parking.repository

import com.github.coutinhonobre.domain.feature.parking.exception.ParkingNotFoundException
import com.github.coutinhonobre.domain.feature.parking.model.Parking
import com.github.coutinhonobre.domain.feature.parking.repository.ParkingRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FireStoreParkingRepositoryImpl(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ParkingRepository {

    override suspend fun saveParking(parking: Parking): Result<Boolean> {
        return try {
            val parkingData = hashMapOf(
                "capacity" to parking.capacity,
                "name" to parking.name,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )

            val parkingRef = db.collection("parkingLots")
                .add(parkingData)
                .await()

            val userId = auth.currentUser?.uid ?: throw Exception("Usuário não autenticado")

            val userParkingData = hashMapOf(
                "userId" to userId,
                "parkingLots" to parkingRef,
                "role" to "Admin",
                "assignedAt" to FieldValue.serverTimestamp()
            )

            db.collection("users")
                .add(userParkingData)
                .await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getParkingForId(): Result<Parking> {
        return suspendCoroutine { continuation ->
            db.collection("parkingLots")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val documents = task.result
                        if (!documents.isEmpty) {
                            val document = documents.documents.first()
                            val parking = Parking(
                                id = document.id,
                                capacity = document.getLong("capacity")?.toInt() ?: 0,
                                name = document.getString("name") ?: ""
                            )
                            continuation.resume(Result.success(parking))
                        } else {
                            continuation.resume(Result.failure(ParkingNotFoundException()))
                        }
                    } else {
                        continuation.resume(Result.failure(task.exception ?: Exception("Erro ao buscar estacionamento")))
                    }
                }
        }
    }


    override suspend fun updateParking(parking: Parking): Result<Boolean> {
        return try {
            val parkingData = hashMapOf(
                "capacity" to parking.capacity,
                "name" to parking.name,
                "updatedAt" to FieldValue.serverTimestamp()
            )

            db.collection("parkingLots")
                .document(parking.id)
                .update(parkingData)
                .await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

