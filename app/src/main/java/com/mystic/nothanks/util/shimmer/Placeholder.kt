package com.mystic.nothanks.util.shimmer

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.node.Ref
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.LayoutDirection

/**
 * Created Using Android Studio
 * User: mE
 * Date: Thursday, 11 Jul 2024
 * Time: 11:53 PM
 */

fun Modifier.placeholder(
    visible: Boolean,
    color: Color,
    shape: Shape = RectangleShape,
    highlight: PlaceholderHighlight? = null,
    placeholderFadeTransitionSpec: @Composable Transition.Segment<Boolean>.() -> FiniteAnimationSpec<Float> = { spring() },
    contentFadeTransitionSpec: @Composable Transition.Segment<Boolean>.() -> FiniteAnimationSpec<Float> = { spring() },
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "placeholder"
        value = visible
        properties["visible"] = visible
        properties["color"] = color
        properties["highlight"] = highlight
        properties["shape"] = shape
    }
) {
    // Values used for caching purposes
    val lastSize = remember { Ref<Size>() }
    val lastLayoutDirection = remember { Ref<LayoutDirection>() }
    val lastOutline = remember { Ref<Outline>() }

    // The current highlight animation progress
    var highlightProgress: Float by remember { mutableFloatStateOf(0f) }

    // This is our crossfade transition
    val transitionState = remember { MutableTransitionState(visible) }.apply {
        targetState = visible
    }
    val transition = updateTransition(transitionState, "placeholder_crossfade")

    val placeholderAlpha by transition.animateFloat(
        transitionSpec = placeholderFadeTransitionSpec,
        label = "placeholder_fade",
        targetValueByState = { placeholderVisible -> if (placeholderVisible) 1f else 0f }
    )
    val contentAlpha by transition.animateFloat(
        transitionSpec = contentFadeTransitionSpec,
        label = "content_fade",
        targetValueByState = { placeholderVisible -> if (placeholderVisible) 0f else 1f }
    )

    // Run the optional animation spec and update the progress if the placeholder is visible
    val animationSpec = highlight?.animationSpec
    if (animationSpec != null && (visible || placeholderAlpha >= 0.01f)) {
        val infiniteTransition = rememberInfiniteTransition(label = "")
        highlightProgress = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = animationSpec, label = "",
        ).value
    }

    val paint = remember { Paint() }
    remember(color, shape, highlight) {
        drawWithContent {
            // Draw the composable content first
            if (contentAlpha in 0.01f..0.99f) {
                // If the content alpha is between 1% and 99%, draw it in a layer with
                // the alpha applied
                paint.alpha = contentAlpha
                withLayer(paint) {
                    with(this@drawWithContent) {
                        drawContent()
                    }
                }
            } else if (contentAlpha >= 0.99f) {
                // If the content alpha is > 99%, draw it with no alpha
                drawContent()
            }

            if (placeholderAlpha in 0.01f..0.99f) {
                // If the placeholder alpha is between 1% and 99%, draw it in a layer with
                // the alpha applied
                paint.alpha = placeholderAlpha
                withLayer(paint) {
                    lastOutline.value = drawPlaceholder(
                        shape = shape,
                        color = color,
                        highlight = highlight,
                        progress = highlightProgress,
                        lastOutline = lastOutline.value,
                        lastLayoutDirection = lastLayoutDirection.value,
                        lastSize = lastSize.value,
                    )
                }
            } else if (placeholderAlpha >= 0.99f) {
                // If the placeholder alpha is > 99%, draw it with no alpha
                lastOutline.value = drawPlaceholder(
                    shape = shape,
                    color = color,
                    highlight = highlight,
                    progress = highlightProgress,
                    lastOutline = lastOutline.value,
                    lastLayoutDirection = lastLayoutDirection.value,
                    lastSize = lastSize.value,
                )
            }

            // Keep track of the last size & layout direction
            lastSize.value = size
            lastLayoutDirection.value = layoutDirection
        }
    }
}

private fun DrawScope.drawPlaceholder(
    shape: Shape,
    color: Color,
    highlight: PlaceholderHighlight?,
    progress: Float,
    lastOutline: Outline?,
    lastLayoutDirection: LayoutDirection?,
    lastSize: Size?,
): Outline? {
    // shortcut to avoid Outline calculation and allocation
    if (shape === RectangleShape) {
        // Draw the initial background color
        drawRect(color = color)

        if (highlight != null) {
            drawRect(
                brush = highlight.brush(progress, size),
                alpha = highlight.alpha(progress),
            )
        }
        // We didn't create an outline so return null
        return null
    }

    // Otherwise we need to create an outline from the shape
    val outline = lastOutline.takeIf {
        size == lastSize && layoutDirection == lastLayoutDirection
    } ?: shape.createOutline(size, layoutDirection, this)

    // Draw the placeholder color
    drawOutline(outline = outline, color = color)

    if (highlight != null) {
        drawOutline(
            outline = outline,
            brush = highlight.brush(progress, size),
            alpha = highlight.alpha(progress),
        )
    }

    // Return the outline we used
    return outline
}

private inline fun DrawScope.withLayer(
    paint: Paint,
    drawBlock: DrawScope.() -> Unit,
) = drawIntoCanvas { canvas ->
    canvas.saveLayer(size.toRect(), paint)
    drawBlock()
    canvas.restore()
}