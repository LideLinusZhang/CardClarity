package edu.card.clarity.presentation.recordReceiptScreen

import android.content.Context
import android.os.Environment
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import edu.card.clarity.presentation.common.CustomButton
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor

@Composable
fun CameraCapture(onImageCaptured: (String) -> Unit, onError: (ImageCaptureException) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val imageCapture = remember { ImageCapture.Builder().build() }
    val executor = ContextCompat.getMainExecutor(context)  // Define executor here

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp)
    ) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            imageCapture
                        )
                    } catch (exc: Exception) {
                        onError(exc as ImageCaptureException)
                    }
                }, executor)

                previewView
            },
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(4f / 3f)
        )

        CustomButton(
            onClick = {
                takePhoto(context, imageCapture, executor, onImageCaptured, onError)
            },
            text = "Take Photo",
            transparency = 0.5f,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .width(200.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    executor: Executor,
    onImageCaptured: (String) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    val photoFile = createFile(context)
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                onImageCaptured(photoFile.absolutePath)
            }

            override fun onError(exc: ImageCaptureException) {
                onError(exc)
            }
        }
    )
}

fun createFile(context: Context): File {
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val fileName = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
        .format(System.currentTimeMillis()) + ".jpg"

    return File(storageDir, fileName)
}

