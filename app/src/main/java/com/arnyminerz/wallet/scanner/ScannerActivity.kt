package com.arnyminerz.wallet.scanner

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.lifecycle.LifecycleOwner
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.ui.theme.WalletTheme
import com.arnyminerz.wallet.ui.theme.setContentThemed
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import java.util.concurrent.CancellationException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ScannerActivity : AppCompatActivity(), BarcodeCallback {
    companion object {
        const val RESULT_EXTRA_DATA = "data"
    }

    private var cameraExecutor: ExecutorService? = null

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraExecutor = Executors.newSingleThreadExecutor()

        setContentThemed {
            val permissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
            val scope = rememberCoroutineScope()

            val lifecycleOwner = LocalLifecycleOwner.current

            val onBackRequested: () -> Unit = {
                setResult(RESULT_CANCELED)
                finish()
            }
            BackHandler(onBack = onBackRequested)

            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        navigationIcon = {
                            IconButton(onClick = onBackRequested) {
                                Icon(Icons.Rounded.Close, stringResource(R.string.image_desc_close))
                            }
                        },
                        title = { Text(text = stringResource(R.string.title_scan_code)) },
                    )
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                ) {
                    AnimatedVisibility(
                        visible = !permissionState.status.isGranted,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(.8f),
                    ) {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {
                            Text(
                                text = stringResource(R.string.permission_camera_title),
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp),
                            )
                            Text(
                                text = stringResource(R.string.permission_camera_message),
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp),
                            )
                            OutlinedButton(
                                onClick = { scope.launch { permissionState.launchPermissionRequest() } },
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .padding(horizontal = 8.dp),
                            ) { Text(stringResource(R.string.permission_grant)) }
                        }
                    }

                    if (permissionState.status.isGranted) {
                        val previewView: PreviewView = remember { PreviewView(this@ScannerActivity) }

                        val squares = remember { mutableStateListOf<Rect>() }

                        LaunchedEffect(previewView) {
                            startCamera(previewView, lifecycleOwner, squares)
                        }

                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .zIndex(1f),
                            onDraw = {
                                squares.forEach { qrRect ->
                                    val position = Offset(qrRect.top, qrRect.left)
                                    val size = Size(qrRect.width, qrRect.height)

                                    drawRoundRect(Color.Red, position, size, CornerRadius(10f, 10f), style = Stroke(width = 5f))
                                }
                            },
                        )

                        AndroidView(
                            factory = { previewView },
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor?.shutdown()
    }

    override fun onFound(barcode: Barcode) {
        Timber.w("Got barcode: $barcode")
        setResult(RESULT_OK, Intent().apply { putExtra(RESULT_EXTRA_DATA, barcode.rawValue) })
        finish()
    }

    @ExperimentalGetImage
    private suspend fun startCamera(previewView: PreviewView, lifecycleOwner: LifecycleOwner, squares: SnapshotStateList<Rect>) {
        val provider = getCameraProvider()

        val preview = Preview.Builder()
            .build()
            .also { it.setSurfaceProvider(previewView.surfaceProvider) }

        val qrAnalyzer = QrCodeAnalyzer(previewView.width.toFloat(), previewView.height.toFloat(), this, squares)
        val analyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { it.setAnalyzer(cameraExecutor!!, qrAnalyzer) }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            provider.unbindAll()
            provider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, analyzer)
        } catch (e: IllegalStateException) {
            // If the use case has already been bound to another lifecycle or method is not called on main thread.
        } catch (e: IllegalArgumentException) {
            // If the provided camera selector is unable to resolve a camera to be used for the given use cases.
        }
    }

    private suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCancellableCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { future ->
            try {
                continuation.resume(future.get())
            } catch (e: CancellationException) {
                continuation.cancel(e)
            } catch (e: Exception) {
                continuation.resumeWithException(e)
            }
        }
    }
}