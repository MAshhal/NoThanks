package com.mystic.nothanks.data.barcode

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Created Using Android Studio
 * User: mE
 * Date: Wednesday, 17 Jul 2024
 * Time: 6:46 PM
 */

class BarcodeScannerRepository @Inject constructor(
    @ApplicationContext private val context: Context
): BarcodeScanner {

    private val scannerOptions by lazy {
        GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .enableAutoZoom()
            .build()
    }

    private val client by lazy { GmsBarcodeScanning.getClient(context, scannerOptions) }

    override suspend fun startScan() = client.startScanAsync()

}

suspend fun GmsBarcodeScanner.startScanAsync() = startScan().asResult()

suspend fun <T: Barcode> Task<T>.asResult(): Result<Barcode> {
    return suspendCancellableCoroutine { cont ->
        addOnSuccessListener {
            it?.let { cont.resume(Result.success(it)) } ?: cont.resume(Result.failure(BarcodeResultException()))
        }
        addOnFailureListener { cont.resume(Result.failure(it)) }
        addOnCanceledListener { cont.cancel() }
    }
}

class BarcodeResultException: Exception("Barcode Result is empty")