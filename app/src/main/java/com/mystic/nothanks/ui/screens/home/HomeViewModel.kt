package com.mystic.nothanks.ui.screens.home

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.common.Barcode
import com.mr0xf00.easycrop.CropResult
import com.mystic.nothanks.data.barcode.BarcodeScanner
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created Using Android Studio
 * User: mE
 * Date: Wednesday, 17 Jul 2024
 * Time: 7:14 PM
 */

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scanner: BarcodeScanner
) : ViewModel() {

    private val _toastMessages = MutableSharedFlow<String>()
    val toastMessages = _toastMessages.asSharedFlow()

    private val _selectedImage = MutableStateFlow<Uri?>(null)
    private val _croppedImage = MutableStateFlow<ImageBitmap?>(null)

    val uiState = combine(_selectedImage.filterNotNull(), _croppedImage) { selectedImage, croppedImage ->
        UiState(selectedImage, croppedImage)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.ImageSelected -> {
                _selectedImage.update { event.image }
            }

            is UiEvent.CroppedImage -> {
                handleCropResult(event.cropResult)
            }
        }
    }

    private fun handleCropResult(cropResult: CropResult) {
        when(cropResult) {
            is CropResult.Success -> {
                _croppedImage.update { cropResult.bitmap }
            }
            CropResult.Cancelled -> {
                _toastMessages.tryEmit("Crop cancelled")
            }
            else -> {
                _toastMessages.tryEmit("Crop failed")
            }
        }
    }

    fun scanBarcode() {
        viewModelScope.launch {
            val result = scanner.startScan()
            if (result.isSuccess) {
                val value = result.getOrNull()!!

                when (value.valueType) {
                    Barcode.TYPE_PRODUCT -> {
                        println(
                            """
                            ${value.rawValue}
                            ${value.displayValue}
                            ${value.sms}
                            ${value.url}
                            ${value.boundingBox}
                            ${value.calendarEvent}
                            ${value.contactInfo}
                            ${value.cornerPoints}
                            ${value.driverLicense}
                            ${value.email}
                            ${value.format}
                            ${value.geoPoint}
                            ${value.phone}
                            ${value.rawBytes}
                            ${value.wifi}
                        """.trimIndent()
                        )
                    }
                }

                _toastMessages.emit("Scan success: ${result.getOrNull()!!.rawValue}")
            } else {
                _toastMessages.emit("Scan failed ${result.exceptionOrNull()?.message}")
            }
        }
    }
}

data class UiState(
    val selectedImage: Uri,
    val croppedImage: ImageBitmap?
)

sealed interface UiEvent {
    data class ImageSelected(val image: Uri) : UiEvent
    data class CroppedImage(val cropResult: CropResult) : UiEvent
}