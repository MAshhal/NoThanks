package com.mystic.nothanks.util.shimmer.m3

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.spring
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.isSpecified
import com.mystic.nothanks.util.shimmer.PlaceholderDefaults
import com.mystic.nothanks.util.shimmer.PlaceholderHighlight
import com.mystic.nothanks.util.shimmer.placeholder

/**
 * Created Using Android Studio
 * User: mE
 * Date: Friday, 12 Jul 2024
 * Time: 12:10 AM
 */

@Composable
fun PlaceholderDefaults.color(
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    contentAlpha: Float = 0.1f,
): Color = contentColor.copy(contentAlpha).compositeOver(backgroundColor)

@Composable
fun PlaceholderDefaults.shimmerHighlightColor(
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    alpha: Float = 0.75f,
): Color {
    return backgroundColor.copy(alpha = alpha)
}


fun Modifier.placeholder(
    visible: Boolean,
    color: Color = Color.Unspecified,
    shape: Shape? = null,
    highlight: PlaceholderHighlight? = null,
    placeholderFadeTransitionSpec: @Composable Transition.Segment<Boolean>.() -> FiniteAnimationSpec<Float> = { spring() },
    contentFadeTransitionSpec: @Composable Transition.Segment<Boolean>.() -> FiniteAnimationSpec<Float> = { spring() },
): Modifier = this.composed {
    Modifier.placeholder(
        visible = visible,
        color = if (color.isSpecified) color else PlaceholderDefaults.color(),
        shape = shape ?: MaterialTheme.shapes.small,
        highlight = highlight,
        placeholderFadeTransitionSpec = placeholderFadeTransitionSpec,
        contentFadeTransitionSpec = contentFadeTransitionSpec,
    )
}
