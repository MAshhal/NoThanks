package com.mystic.nothanks.data.barcode

import com.google.mlkit.vision.barcode.common.Barcode

/**
 * Created Using Android Studio
 * User: mE
 * Date: Wednesday, 17 Jul 2024
 * Time: 7:15 PM
 */
interface BarcodeScanner {
    suspend fun startScan(): Result<Barcode>
}