package com.mystic.nothanks.ui.screens.brands

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mystic.nothanks.core.models.Brand
import com.mystic.nothanks.core.models.toBrand
import com.mystic.nothanks.data.database.dao.BoycottDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

/**
 * Created Using Android Studio
 * User: mE
 * Date: Sunday, 07 Jul 2024
 * Time: 9:22 PM
 */
@HiltViewModel
class BrandsViewModel @Inject constructor(
    private val database: BoycottDao
) : ViewModel() {

    val uiState = database.getAllBrands()
        .map { brands ->
            BrandsUiState(
                brands = brands.map {
                    println(it)
                    it.toBrand()
                }
            )
        }
        .conflate()
        .stateIn(
            scope = viewModelScope,
            initialValue = BrandsUiState(emptyList()),
            started = SharingStarted.WhileSubscribed(5.seconds)
        )

}

data class BrandsUiState(
    val brands: List<Brand>
)