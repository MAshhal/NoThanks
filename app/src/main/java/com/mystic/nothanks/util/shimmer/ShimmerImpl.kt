package com.mystic.nothanks.util.shimmer

import androidx.annotation.FloatRange
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.util.lerp
import kotlin.math.max

/**
 * Created Using Android Studio
 * User: mE
 * Date: Thursday, 11 Jul 2024
 * Time: 11:44 PM
 */

/**
 * A class which provides a brush to paint placeholder based on progress.
 */
@Stable
interface PlaceholderHighlight {

    /**
     * The optional [AnimationSpec] to use when running the animation for this highlight.
     */
    val animationSpec: InfiniteRepeatableSpec<Float>?

    /**
     * Return a [Brush] to draw for the given [progress] and [size].
     *
     * @param progress the current animated progress in the range of 0f..1f.
     * @param size The size of the current layout to draw in.
     */
    fun brush(
        @FloatRange(from = 0.0, to = 1.0) progress: Float,
        size: Size
    ): Brush

    /**
     * Return the desired alpha value used for drawing the [Brush] returned from [brush].
     *
     * @param progress the current animated progress in the range of 0f..1f.
     */
    @FloatRange(from = 0.0, to = 1.0)
    fun alpha(progress: Float): Float

    companion object
}

fun PlaceholderHighlight.Companion.shimmer(
    highlightColor: Color,
    animationSpec: InfiniteRepeatableSpec<Float> = PlaceholderDefaults.shimmerAnimationSpec,
    @FloatRange(from = 0.0, to = 1.0) progressForMaxAlpha: Float = 0.6f,
): PlaceholderHighlight = Shimmer(
    highlightColor = highlightColor,
    animationSpec = animationSpec,
    progressForMaxAlpha = progressForMaxAlpha,
)

private data class Shimmer(
    private val highlightColor: Color,
    override val animationSpec: InfiniteRepeatableSpec<Float>?,
    private val progressForMaxAlpha: Float = 0.6f
) : PlaceholderHighlight {
    override fun brush(progress: Float, size: Size): Brush =
        Brush.radialGradient(
            colors = listOf(
                highlightColor.copy(alpha = 0f),
                highlightColor,
                highlightColor.copy(alpha = 0f),
            ),
            center = Offset(x = 0f, y = 0f),
            radius = (max(size.width, size.height) * progress * 2).coerceAtLeast(0.01f),
        )

    override fun alpha(progress: Float): Float = when {
        // From 0f...ProgressForOpaqueAlpha we animate from 0..1
        progress <= progressForMaxAlpha -> {
            lerp(
                start = 0f,
                stop = 1f,
                fraction = progress / progressForMaxAlpha
            )
        }

        // From ProgressForOpaqueAlpha..1f we animate from 1..0
        else -> {
            lerp(
                start = 1f,
                stop = 0f,
                fraction = (progress - progressForMaxAlpha) / (1f - progressForMaxAlpha)
            )
        }
    }
}

object PlaceholderDefaults {
    /**
     * The default [InfiniteRepeatableSpec] to use for [shimmer].
     */
    val shimmerAnimationSpec: InfiniteRepeatableSpec<Float> by lazy {
        infiniteRepeatable(
            animation = tween(durationMillis = 1700, delayMillis = 200),
            repeatMode = RepeatMode.Restart
        )
    }
}