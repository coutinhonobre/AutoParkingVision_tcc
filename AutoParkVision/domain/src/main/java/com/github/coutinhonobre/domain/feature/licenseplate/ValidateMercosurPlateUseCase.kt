package com.github.coutinhonobre.domain.feature.licenseplate

class ValidateMercosurPlateUseCase {
    operator fun invoke(plate: String): Boolean {
        val mercosurPattern = "^[A-Z]{3}[0-9][A-Z][0-9]{2}$".toRegex()
        return plate.uppercase().matches(mercosurPattern)
    }
}