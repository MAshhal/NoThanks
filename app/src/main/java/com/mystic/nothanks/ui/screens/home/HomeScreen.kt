package com.mystic.nothanks.ui.screens.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.mr0xf00.easycrop.crop
import com.mr0xf00.easycrop.rememberImageCropper
import com.mr0xf00.easycrop.ui.ImageCropperDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Home") },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        ScreenContent(modifier = Modifier.padding(innerPadding), uiState, viewModel::onEvent)
    }
}

@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    uiState: UiState?,
    onEvent: (UiEvent) -> Unit
) {

//    LaunchedEffect(key1 = true) {
//        viewModel.toastMessages.collectLatest {
//            onShowSnackbar(it, null)
//        }
//    }

    val cropper = rememberImageCropper()
    val scope = rememberCoroutineScope()

    Surface(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if (uiState == null) {
                ImageNotSelectedLayout(
                    onImageSelected = { onEvent(UiEvent.ImageSelected(it)) }
                )
            } else {
                ImageSelectedLayout(uiState = uiState, onEvent = onEvent)
            }
        }
    }
}

@Composable
fun ImageNotSelectedLayout(
    modifier: Modifier = Modifier,
    onImageSelected: (Uri) -> Unit
) {

    val imagePicker =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
            it?.let { onImageSelected(it) }
        }

    val selectImage: () -> Unit = {
        imagePicker.launch(
            PickVisualMediaRequest(
                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
            )
        )
    }

    Button(
        modifier = modifier,
        onClick = selectImage
    ) {
        Icon(imageVector = Icons.Outlined.Image, contentDescription = null)
        Text(text = "Select Image")
    }
}

@Composable
fun ImageSelectedLayout(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onEvent: (UiEvent) -> Unit
) {
    val cropper = rememberImageCropper()
    val cropState = cropper.cropState
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        AsyncImage(
            model = uiState.croppedImage?.asAndroidBitmap() ?: uiState.selectedImage,
            contentDescription = null
        )

        Button(
            onClick = {
                coroutineScope.launch {
                    onEvent(
                        UiEvent.CroppedImage(
                            cropper.crop(uri = uiState.selectedImage, context = context)
                        )
                    )
                }
            }
        ) {
            Text(text = "Crop Image")
        }

        if (cropState != null) {
            ImageCropperDialog(state = cropState)
        }

    }
}