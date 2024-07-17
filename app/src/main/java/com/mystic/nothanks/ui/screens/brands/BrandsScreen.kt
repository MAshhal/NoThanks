package com.mystic.nothanks.ui.screens.brands

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.mystic.nothanks.R
import com.mystic.nothanks.core.models.Brand
import com.mystic.nothanks.util.shimmer.PlaceholderHighlight
import com.mystic.nothanks.util.shimmer.m3.placeholder
import com.mystic.nothanks.util.shimmer.m3.shimmer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandsScreen(
    modifier: Modifier = Modifier,
    viewModel: BrandsViewModel = hiltViewModel(),
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MediumTopAppBar(
                title = { Text(text = "Brands") },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        ScreenContent(
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            uiState = uiState,
            onShowSnackbar = onShowSnackbar
        )
    }
}

@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    uiState: BrandsUiState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    val state = rememberLazyGridState()
    val scope = rememberCoroutineScope()
    Surface(
        modifier = modifier,
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp),
            state = state,
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                uiState.brands,
                key = { it.id }
            ) { brand ->
                BrandItem(
                    item = brand
                ) {
                    scope.launch {
                        onShowSnackbar("Clicked ${brand.name}", null)
                    }
                }
            }
        }
    }
}


@Composable
fun BrandItem(
    modifier: Modifier = Modifier,
    item: Brand,
    onBrandSelected: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }

    OutlinedCard(
        modifier = modifier,
        onClick = onBrandSelected,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy()
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .aspectRatio(1f)
                    .placeholder(visible = isLoading, highlight = PlaceholderHighlight.shimmer()),
                model = item.logoUrl,
                contentDescription = null,
                onLoading = { isLoading = true },
                error = {
                    Column(
                        modifier = Modifier.aspectRatio(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            modifier = Modifier.scale(.8f),
                            painter = painterResource(id = R.drawable.no_image_provided),
                            contentDescription = null
                        )
                        Text(
                            text = "Could not load image! ${it.result.throwable.message}",
                            fontSize = 11.sp
                        )
                    }
                },
                onSuccess = { isLoading = false },
                onError = { isLoading = false },
            )

            Text(
                modifier = Modifier.padding(8.dp),
                text = item.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }
    }
}