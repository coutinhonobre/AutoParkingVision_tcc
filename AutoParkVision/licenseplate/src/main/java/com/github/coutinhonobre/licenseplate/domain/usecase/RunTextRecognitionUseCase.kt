package com.github.coutinhonobre.licenseplate.domain.usecase

import android.graphics.Bitmap
import android.text.Editable
import android.util.Log
import com.github.coutinhonobre.domain.feature.licenseplate.CleanLicensePlateUseCase
import com.github.coutinhonobre.domain.feature.licenseplate.CorrectMercosurPlateUseCase
import com.github.coutinhonobre.domain.feature.licenseplate.ValidateMercosurPlateUseCase
import com.github.coutinhonobre.licenseplate.databinding.ActivityLicensePlateBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

private const val TAG = "RunTextRecognitionUseCase"

class RunTextRecognitionUseCase {
    operator fun invoke(
        croppedBitmap: Bitmap,
        rotation: Int,
        runOnUiThread: (Runnable) -> Unit,
        binding: ActivityLicensePlateBinding
    ) {
        val processedBitmap = PreprocessImageUseCase().invoke(croppedBitmap)
        val image = InputImage.fromBitmap(processedBitmap, rotation)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val detectedText = visionText.text

                val cleanLicensePlate = CleanLicensePlateUseCase().invoke(detectedText)
                val correctMerciful = CorrectMercosurPlateUseCase().invoke(cleanLicensePlate)
                binding.imageViewPlate.setImageBitmap(processedBitmap)

                Log.d(TAG, "Placa Texto detectado: $detectedText")
                Log.d(TAG, "Placa Texto detectado corrigido: $correctMerciful")
                runOnUiThread {
                    val isPlateMercosul = ValidateMercosurPlateUseCase().invoke(correctMerciful)
                    if (isPlateMercosul && binding.textInputPlate.text.isNullOrBlank()) {
                        binding.textInputPlate.text =
                            Editable.Factory.getInstance().newEditable(correctMerciful)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Placa Falha no OCR", e)
            }
    }
}