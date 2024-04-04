package com.notesmakers.ui.animations

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically

fun getEnterScrollTransition() = slideInVertically(
    initialOffsetY = { -it }
) + fadeIn(initialAlpha = 0.3f)

fun getExitScrollTransition() = slideOutVertically(
    targetOffsetY = { -it }
) + fadeOut()


fun navEnterTransition() = fadeIn(animationSpec = tween(300))
fun navExitTransition() = fadeOut(animationSpec = tween(300))
fun popEnterTransition() = slideInHorizontally(
    initialOffsetX = { fullSize -> fullSize },
    animationSpec = tween(300)
)

fun popExitTransition() = slideOutHorizontally(
    targetOffsetX = { fullSize -> -fullSize },
    animationSpec = tween(300)
)
