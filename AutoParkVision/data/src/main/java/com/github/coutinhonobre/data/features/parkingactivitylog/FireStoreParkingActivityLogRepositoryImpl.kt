package com.github.coutinhonobre.data.features.parkingactivitylog

import com.github.coutinhonobre.domain.feature.parkingactivitylog.model.ParkingLog
import com.github.coutinhonobre.domain.feature.parkingactivitylog.model.ParkingLogEventType
import com.github.coutinhonobre.domain.feature.parkingactivitylog.repository.ParkingActivityLogRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class FireStoreParkingActivityLogRepositoryImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ParkingActivityLogRepository {
    override suspend fun saveLog(
        licensePlate: String
    ): Result<String> {
        return try {

            val authorizedVehicles = db.collection("vehicles")
                .whereEqualTo("plate", licensePlate)
                .whereEqualTo("authorized", "AUTHORIZED")
                .limit(1)
                .get()
                .await()

            println("Documentos retornados: ${authorizedVehicles.documents}")

            authorizedVehicles.documents.firstOrNull()
                ?: return Result.failure(Exception("Veículo não autorizado! Nenhum documento encontrado."))


            val lastEvent = db.collection("parkingLogs")
                .whereEqualTo("licensePlate", licensePlate)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .await()
                .documents
                .firstOrNull()

            val nextEventType = if (lastEvent != null && lastEvent.getString("eventType") == ParkingLogEventType.ENTRY.name) {
                ParkingLogEventType.EXIT
            } else {
                ParkingLogEventType.ENTRY
            }

            val logData = hashMapOf(
                "eventType" to nextEventType.name,
                "licensePlate" to licensePlate,
                "timestamp" to Timestamp.now()
            )

            db.collection("parkingLogs")
                .add(logData)
                .await()

            Result.success("Veículo ${nextEventType.description} com sucesso!")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getLogs(): Result<List<ParkingLog>> {
        return try {
            val calendar = Calendar.getInstance()

            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            val startOfDay = Timestamp(calendar.time)

            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            val endOfDay = Timestamp(calendar.time)

            val logs = db.collection("parkingLogs")
                .whereGreaterThanOrEqualTo("timestamp", startOfDay)
                .whereLessThan("timestamp", endOfDay)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
                .documents
                .map {
                    ParkingLog(
                        eventType = ParkingLogEventType.valueOf(it["eventType"] as String),
                        licensePlate = it["licensePlate"] as String,
                        timestamp = (it["timestamp"] as Timestamp).toDate().toString(),
                        emailUser = auth.currentUser?.email ?: "",
                        id = it.id
                    )
                }

            Result.success(logs)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getLogs(
        licensePlate: String,
        startDate: Long,
        endDate: Long
    ): Result<List<ParkingLog>> {
        return try {
            var query = db.collection("parkingLogs") as Query

            if (licensePlate.isNotEmpty()) {
                query = query.whereEqualTo("licensePlate", licensePlate)
            }

            if (startDate > 0L) {
                val startTimestamp = getStartOfDay(startDate)
                query = query.whereGreaterThanOrEqualTo("timestamp", startTimestamp)
            }

            if (endDate > 0L) {
                val endTimestamp = getEndOfDay(endDate)
                query = query.whereLessThanOrEqualTo("timestamp", endTimestamp)
            }

            query = query.orderBy("timestamp", Query.Direction.DESCENDING)

            val logs = query.get().await()
                .documents
                .mapNotNull { doc ->
                    val eventType = doc.getString("eventType")?.let { ParkingLogEventType.valueOf(it) }
                    val timestamp = doc.getTimestamp("timestamp")?.toDate()
                    val license = doc.getString("licensePlate")

                    if (eventType != null && timestamp != null && license != null) {
                        ParkingLog(
                            eventType = eventType,
                            licensePlate = license,
                            timestamp = timestamp.toString(),
                            emailUser = auth.currentUser?.email ?: "",
                            id = doc.id
                        )
                    } else {
                        null
                    }
                }

            Result.success(logs)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getStartOfDay(timestamp: Long): Timestamp {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return Timestamp(calendar.timeInMillis / 1000, 0)
    }

    private fun getEndOfDay(timestamp: Long): Timestamp {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return Timestamp(calendar.timeInMillis / 1000, 0)
    }

    override suspend fun getTodayEntryExitCount(): Result<Pair<Int, Int>> {
        return try {
            val currentTime = System.currentTimeMillis() // Obtém o timestamp atual
            val startOfDay = getStartOfDay(currentTime)
            val endOfDay = getEndOfDay(currentTime)

            val logs = db.collection("parkingLogs")
                .whereGreaterThanOrEqualTo("timestamp", startOfDay)
                .whereLessThan("timestamp", endOfDay)
                .get()
                .await()
                .documents

            val entryCount = logs.count { it.getString("eventType") == ParkingLogEventType.ENTRY.name }
            val exitCount = logs.count { it.getString("eventType") == ParkingLogEventType.EXIT.name }

            Result.success(Pair(entryCount, exitCount))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}