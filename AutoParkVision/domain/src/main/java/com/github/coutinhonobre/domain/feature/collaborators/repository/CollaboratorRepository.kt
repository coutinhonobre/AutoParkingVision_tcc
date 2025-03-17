package com.github.coutinhonobre.domain.feature.collaborators.repository

import com.github.coutinhonobre.domain.feature.collaborators.model.Collaborator

interface CollaboratorRepository {
    suspend fun getAllCollaborators(): Result<List<Collaborator>> = Result.failure(Exception("Not implemented yet"))
    suspend fun saveCollaborator(collaborator: Collaborator): Result<Unit> = Result.failure(Exception("Not implemented yet"))
    suspend fun getCollaboratorById(id: String): Result<Collaborator> = Result.failure(Exception("Not implemented yet"))
    suspend fun validateCode(code: String): Result<Boolean> = Result.failure(Exception("Not implemented yet"))
}