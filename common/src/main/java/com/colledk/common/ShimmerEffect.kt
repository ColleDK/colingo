package com.colledk.common

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.shimmerEffect(
    color: Color,
    duration: Int = 1000
): Modifier {
    return composed {
        val angle = 270f

        val shimmerColors = listOf(
            color.copy(.3f),
            color.copy(.5f),
            color,
            color.copy(.5f),
            color.copy(.3f)
        )
        val transition = rememberInfiniteTransition(label = "shimmer transition")

        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = (duration + angle),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = duration,
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "shimmer effect"
        )

        this.background(
            brush = Brush.linearGradient(
                colors = shimmerColors,
                start = Offset(x = translateAnimation.value - angle, y = 0f),
                end = Offset(x = translateAnimation.value, y = angle)
            )
        )
    }
}
