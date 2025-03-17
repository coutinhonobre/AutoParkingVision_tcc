package com.github.coutinhonobre.domain.feature.collaborators.model

enum class CollaboratorStatus {
    ACTIVE,
    INACTIVE
}
class Collaborator(
    val id: String,
    val name: String,
    val accessCode: String,
    val status: CollaboratorStatus = CollaboratorStatus.ACTIVE
)