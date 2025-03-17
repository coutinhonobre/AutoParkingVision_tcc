package com.github.coutinhonobre.domain.feature.licenseplate

class CleanLicensePlateUseCase {
    operator fun invoke(plate: String): String {
        return plate.replace(Regex("[^A-Za-z0-9]"), "")
    }
}