package com.github.coutinhonobre.domain.feature.licenseplate

class CorrectMercosurPlateUseCase {
    operator fun invoke(ocrText: String): String {
        val cleanedText = ocrText.replace("[^A-Z0-9]".toRegex(), "").uppercase()

        if (cleanedText.length < 4) {
            return ""
        }

        val letterCorrections = mapOf('0' to 'O', '1' to 'I', '8' to 'B', '5' to 'S')
        val numberCorrections = mapOf('O' to '0', 'I' to '1', 'B' to '8', 'S' to '5')

        val correctedPlate = StringBuilder()

        for (i in 0 until 7) {
            var correctedChar = if (i < cleanedText.length) cleanedText[i] else '_'

            when (i) {
                in 0..2, 4 -> {
                    if (correctedChar in '0'..'9') {
                        correctedChar = letterCorrections[correctedChar] ?: correctedChar
                    }
                }
                3, 5, 6 -> {
                    if (correctedChar in 'A'..'Z') {
                        correctedChar = numberCorrections[correctedChar] ?: correctedChar
                    }
                }
            }

            correctedPlate.append(correctedChar)
        }

        if (cleanedText.length < 7) {
            return correctedPlate.toString()
        }

        val platePattern = "^[A-Z]{3}[0-9][A-Z][0-9]{2}$".toRegex()
        return if (correctedPlate.toString().matches(platePattern)) {
            correctedPlate.toString()
        } else {
            correctedPlate.toString()
        }
    }
}