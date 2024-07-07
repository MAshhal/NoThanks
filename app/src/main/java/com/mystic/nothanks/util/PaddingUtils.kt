package com.mystic.nothanks.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

fun PaddingValues.copy(
    layoutDirection: LayoutDirection,
    top: Dp? = null,
    bottom: Dp? = null,
    start: Dp? = null,
    end: Dp? = null
) = PaddingValues(
    top = top ?: calculateTopPadding(),
    bottom = bottom ?: calculateBottomPadding(),
    start = start ?: calculateStartPadding(layoutDirection),
    end = end ?: calculateEndPadding(layoutDirection)
)