package com.github.coutinhonobre.domain.feature.camera

interface LandmarkClassifier<T> {
    fun classify(bitmap: T, rotation: Int): List<Classification>
}