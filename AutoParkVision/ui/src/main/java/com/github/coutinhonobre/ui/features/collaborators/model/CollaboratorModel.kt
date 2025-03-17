package com.github.coutinhonobre.ui.features.collaborators.model

enum class CollaboratorStatusModel(
    val value: String
) {
    ACTIVE("Ativo"),
    INACTIVE("Inativo")
}
class CollaboratorModel(
    val id: String = "",
    val name: String = "",
    val accessCode: String = "",
    val status: CollaboratorStatusModel = CollaboratorStatusModel.ACTIVE
)
