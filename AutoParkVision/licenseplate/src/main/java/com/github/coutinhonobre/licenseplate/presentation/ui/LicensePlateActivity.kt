package com.github.coutinhonobre.licenseplate.presentation.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.github.coutinhonobre.data.features.parkingactivitylog.FireStoreParkingActivityLogRepositoryImpl
import com.github.coutinhonobre.domain.feature.parkingactivitylog.usecase.SaveParkingActivityLogUseCase
import com.github.coutinhonobre.licenseplate.R
import com.github.coutinhonobre.licenseplate.databinding.ActivityLicensePlateBinding
import com.github.coutinhonobre.licenseplate.domain.usecase.ProcessOCRUseCase
import com.github.coutinhonobre.licenseplate.domain.usecase.RunTextRecognitionUseCase
import com.github.coutinhonobre.licenseplate.presentation.factory.LicensePlateViewModelFactory
import com.github.coutinhonobre.licenseplate.presentation.ui.dialog.SuccessDialogFragment
import com.github.coutinhonobre.licenseplate.presentation.viewmodel.LicensePlateViewAction
import com.github.coutinhonobre.licenseplate.presentation.viewmodel.LicensePlateViewModel
import com.github.coutinhonobre.licenseplate.presentation.viewmodel.LicensePlateViewState
import com.github.coutinhonobre.licenseplate.utils.BoundingBox
import com.github.coutinhonobre.licenseplate.utils.Constants.LABELS_PATH
import com.github.coutinhonobre.licenseplate.utils.Constants.MODEL_PATH
import com.github.coutinhonobre.licenseplate.utils.Detector
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class LicensePlateActivity : AppCompatActivity(), Detector.DetectorListener {
    private val binding: ActivityLicensePlateBinding by lazy {
        ActivityLicensePlateBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<LicensePlateViewModel> {
        LicensePlateViewModelFactory(
            saveParkingActivityLogUseCase = SaveParkingActivityLogUseCase(
                parkingActivityLogRepository = FireStoreParkingActivityLogRepositoryImpl()
            )
        )
    }

    private var rotation = 0
    private var inProccess = false
    private var isActivityActive = true

    private val preview: Preview by lazy {
        Preview.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setTargetRotation(binding.viewFinder.display.rotation)
            .build()
    }
    private val imageAnalyzer: ImageAnalysis by lazy {
        ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetRotation(binding.viewFinder.display.rotation)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .build()
    }
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var detector: Detector? = null

    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.camera_container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        cameraExecutor.execute {
            detector = Detector(baseContext, MODEL_PATH, LABELS_PATH, this)
        }

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        observeViewModel()

        binding.buttonSave.setOnClickListener {
            viewModel.onEvent(LicensePlateViewAction.SendLicensePlate(binding.textInputPlate.text.toString()))
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is LicensePlateViewState.Loading -> {
                        inProccess = true
                        with(binding) {
                            progressBar.visibility = android.view.View.VISIBLE
                            textInputPlateLayout.isEnabled = false
                            buttonSave.isEnabled = false
                        }
                    }
                    is LicensePlateViewState.Success -> {
                        vibratePhone()
                        inProccess = false
                        with(binding) {
                            progressBar.visibility = android.view.View.GONE
                            textInputPlateLayout.isEnabled = true
                            buttonSave.isEnabled = true
                        }

                        SuccessDialogFragment(
                            onConfirm = {
                                binding.textInputPlate.text?.clear()
                                        },
                            onDismiss = { finish() }
                        ).show(supportFragmentManager, "SuccessDialog")

                    }
                    is LicensePlateViewState.Error -> {
                        inProccess = false
                        with(binding) {
                            progressBar.visibility = android.view.View.GONE
                            textInputPlateLayout.isEnabled = true
                            buttonSave.isEnabled = true
                        }
                        Toast.makeText(this@LicensePlateActivity, state.message, Toast.LENGTH_LONG).show()
                    }
                    is LicensePlateViewState.Idle -> {
                        inProccess = false
                        with(binding) {
                            progressBar.visibility = android.view.View.GONE
                            textInputPlateLayout.isEnabled = true
                            buttonSave.isEnabled = true
                        }
                    }
                }
            }
        }
    }

    private fun startCamera() {
        camera?.cameraControl?.enableTorch(false)

        camera?.cameraControl?.apply {
            val hasStabilization = camera?.cameraInfo?.hasFlashUnit() ?: false
            if (hasStabilization) {
                Log.d(TAG, "Estabilização ativada")
            }
        }

        camera?.cameraControl?.startFocusAndMetering(
            FocusMeteringAction.Builder(
                binding.viewFinder.meteringPointFactory.createPoint(0.5f, 0.5f),
                FocusMeteringAction.FLAG_AF
            ).apply {
                setAutoCancelDuration(5, java.util.concurrent.TimeUnit.SECONDS)
            }.build()
        )


        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraUseCases() {
        val cameraProvider =
            cameraProvider ?: throw IllegalStateException("Camera initialization failed.")

        val cameraSelector = CameraSelector
            .Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()


        imageAnalyzer.setAnalyzer(cameraExecutor) { imageProxy ->
            val bitmapBuffer =
                Bitmap.createBitmap(
                    imageProxy.width,
                    imageProxy.height,
                    Bitmap.Config.ARGB_8888
                )
            imageProxy.use { bitmapBuffer.copyPixelsFromBuffer(imageProxy.planes[0].buffer) }
            imageProxy.close()

            rotation = imageProxy.imageInfo.rotationDegrees
            val matrix = Matrix().apply {
                postRotate(rotation.toFloat())
            }

            val rotatedBitmap = Bitmap.createBitmap(
                bitmapBuffer, 0, 0, bitmapBuffer.width, bitmapBuffer.height,
                matrix, true
            )

            Log.e(TAG, "Rotação: $rotation")

            if (isActivityActive && detector != null) {
                detector?.detect(rotatedBitmap)
            }
        }

        cameraProvider.unbindAll()

        try {
            camera = cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageAnalyzer
            )

            preview.surfaceProvider = binding.viewFinder.surfaceProvider
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it[Manifest.permission.CAMERA] == true) {
            startCamera()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isActivityActive = false
        detector?.close()
        cameraExecutor.shutdown()
    }

    override fun onResume() {
        super.onResume()
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
        }
    }


    companion object {
        private const val TAG = "Camera"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA
        ).toTypedArray()
    }

    override fun onEmptyDetect() {
        runOnUiThread {
            binding.overlay.clear()
        }
    }

    override fun onDetect(
        boundingBoxes: List<BoundingBox>,
        croppedBitmaps: List<Bitmap>,
        inferenceTime: Long
    ) {
        runOnUiThread {
            if (inProccess) return@runOnUiThread
            binding.inferenceTime.text = "${inferenceTime}ms"
            binding.overlay.apply {
                setResults(boundingBoxes)
                invalidate()
            }

            if (boundingBoxes.isNotEmpty()) {
                ProcessOCRUseCase().invoke(croppedBitmaps) { croppedBitmap ->
                    RunTextRecognitionUseCase().invoke(
                        croppedBitmap = croppedBitmap,
                        rotation = rotation,
                        binding = binding,
                        runOnUiThread = ::runOnUiThread
                    )
                }
            }
        }
    }

    private fun vibratePhone() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}