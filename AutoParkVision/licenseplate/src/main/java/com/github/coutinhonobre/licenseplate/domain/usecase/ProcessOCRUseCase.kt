package com.github.coutinhonobre.licenseplate.domain.usecase

import android.graphics.Bitmap
import android.util.Log

class ProcessOCRUseCase {
    operator fun invoke(croppedBitmaps: List<Bitmap?>, runTextRecognition: (Bitmap) -> Unit) {
        Log.e("ProcessOCRUseCase", "Placa processOCR ${croppedBitmaps.size} size")
        croppedBitmaps.forEach { croppedBitmap ->
            croppedBitmap?.let {
                runTextRecognition(croppedBitmap)
            }
        }
    }
}