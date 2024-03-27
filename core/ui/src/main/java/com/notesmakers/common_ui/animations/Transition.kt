package com.notesmakers.common_ui.animations

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically

fun getEnterScrollTransition() = slideInVertically(
    initialOffsetY = { -it }
) + fadeIn(initialAlpha = 0.3f)

fun getExitScrollTransition() = slideOutVertically(
    targetOffsetY = { -it }
) + fadeOut()