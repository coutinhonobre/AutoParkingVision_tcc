package com.github.coutinhonobre.data.features.collaborators.collaborators

import com.github.coutinhonobre.domain.feature.collaborators.model.Collaborator
import com.github.coutinhonobre.domain.feature.collaborators.model.CollaboratorStatus
import com.github.coutinhonobre.domain.feature.collaborators.repository.CollaboratorRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FireStoreCollaboratorRepositoryImpl(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : CollaboratorRepository {
    override suspend fun getAllCollaborators(): Result<List<Collaborator>> {
        return suspendCoroutine { continuation ->
            db.collection("collaborators")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val collaboratorDocuments = task.result?.documents

                        if (collaboratorDocuments.isNullOrEmpty()) {
                            continuation.resume(Result.success(emptyList()))
                        } else {
                            val collaborators = collaboratorDocuments.map { document ->
                                Collaborator(
                                    id = document.id,
                                    name = document.getString("name") ?: "",
                                    accessCode = document.getString("accessCode") ?: "",
                                    status = when (document.getString("status")) {
                                        "ACTIVE" -> CollaboratorStatus.ACTIVE
                                        "INACTIVE" -> CollaboratorStatus.INACTIVE
                                        else -> CollaboratorStatus.ACTIVE
                                    }
                                )
                            }
                            continuation.resume(Result.success(collaborators))
                        }
                    } else {
                        continuation.resume(Result.failure(Exception("Unknown error")))
                    }
                }
        }
    }

    override suspend fun saveCollaborator(collaborator: Collaborator): Result<Unit> {
        return try {
            val collaboratorData = hashMapOf(
                "name" to collaborator.name,
                "accessCode" to collaborator.accessCode,
                "status" to collaborator.status.name
            )

            if (collaborator.id.isNotEmpty()) {
                db.collection("collaborators")
                    .document(collaborator.id)
                    .set(collaboratorData)
            } else {
                db.collection("collaborators")
                    .add(collaboratorData)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCollaboratorById(id: String): Result<Collaborator> {
        return suspendCoroutine { continuation ->
            db.collection("collaborators")
                .document(id)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val collaboratorDocument = task.result

                        if (collaboratorDocument != null) {
                            val collaborator = Collaborator(
                                id = collaboratorDocument.id,
                                name = collaboratorDocument.getString("name") ?: "",
                                accessCode = collaboratorDocument.getString("accessCode") ?: "",
                                status = when (collaboratorDocument.getString("status")) {
                                    "ACTIVE" -> CollaboratorStatus.ACTIVE
                                    "INACTIVE" -> CollaboratorStatus.INACTIVE
                                    else -> CollaboratorStatus.ACTIVE
                                }
                            )
                            continuation.resume(Result.success(collaborator))
                        } else {
                            continuation.resume(Result.failure(Exception("Collaborator not found")))
                        }
                    } else {
                        continuation.resume(Result.failure(Exception("Unknown error")))
                    }
                }
        }
    }

    override suspend fun validateCode(code: String): Result<Boolean> {
        return suspendCoroutine { continuation ->
            db.collection("collaborators")
                .whereEqualTo("accessCode", code)
                .whereEqualTo("status", "ACTIVE")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val collaboratorDocuments = task.result?.documents

                        if (collaboratorDocuments.isNullOrEmpty()) {
                            continuation.resume(Result.failure(Exception("Collaborator not found")))
                        } else {
                            continuation.resume(Result.success(true))
                        }
                    } else {
                        continuation.resume(Result.failure(Exception("Unknown error")))
                    }
                }
        }
    }
}