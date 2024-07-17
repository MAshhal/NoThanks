package com.mystic.nothanks.util.shimmer.m3

import androidx.annotation.FloatRange
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.runtime.Composable
import com.mystic.nothanks.util.shimmer.PlaceholderDefaults
import com.mystic.nothanks.util.shimmer.PlaceholderHighlight
import com.mystic.nothanks.util.shimmer.shimmer

/**
 * Created Using Android Studio
 * User: mE
 * Date: Friday, 12 Jul 2024
 * Time: 12:11 AM
 */

@Composable
fun PlaceholderHighlight.Companion.shimmer(
    animationSpec: InfiniteRepeatableSpec<Float> = PlaceholderDefaults.shimmerAnimationSpec,
    @FloatRange(from = 0.0, to = 1.0) progressForMaxAlpha: Float = 0.6f,
): PlaceholderHighlight = PlaceholderHighlight.shimmer(
    highlightColor = PlaceholderDefaults.shimmerHighlightColor(),
    animationSpec = animationSpec,
    progressForMaxAlpha = progressForMaxAlpha,
)