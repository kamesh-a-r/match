package com.kamesh.match.presentation.widget.cardStack.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.kamesh.match.domain.model.Profile
import com.kamesh.match.presentation.widget.cardStack.model.DragValue
import com.kamesh.match.presentation.widget.cardStack.model.StackFrom
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeableCard(
    profile: Profile,
    index: Int,
    isTopCard: Boolean,
    visibleCount: Int,
    stackFrom: StackFrom,
    translationInterval: Dp,
    scaleInterval: Float,
    swipeThreshold: Float,
    density: Density,
    onSwiped: (DragValue) -> Unit,
    onCardClicked: () -> Unit
) {
    // Create anchors for the draggable state
    val anchors = DraggableAnchors<DragValue> {
        DragValue.Left at -1000f
        DragValue.Center at 0f
        DragValue.Right at 1000f
    }

    // AnchoredDraggableState for managing swipe gestures
    val state = remember {
        AnchoredDraggableState(
            initialValue = DragValue.Center,
            anchors = anchors,
            positionalThreshold = { distance: Float -> distance * swipeThreshold },
            velocityThreshold = { with(density) { 400.dp.toPx() } },
            snapAnimationSpec = tween(300),
            decayAnimationSpec = exponentialDecay(),
            confirmValueChange = { true }
        )
    }

    val flingBehavior = AnchoredDraggableDefaults.flingBehavior(
        state = state,
        positionalThreshold = { distance: Float -> distance * swipeThreshold }
    )

    val scope = rememberCoroutineScope()

    // Monitor state changes and trigger swipe callback
    LaunchedEffect(state.settledValue) {
        when (state.settledValue) {
            DragValue.Left, DragValue.Right -> {
                onSwiped(state.settledValue)
            }
            DragValue.Center -> {
                // Card is at center, do nothing
            }
        }
    }

    // Calculate translation - cards behind should be ABOVE (negative Y to peek from top)
    val (baseTranslationX, baseTranslationY) = with(density) {
        when (stackFrom) {
            StackFrom.None -> 0f to 0f
            StackFrom.Top -> 0f to -index * translationInterval.toPx()
            StackFrom.Bottom -> 0f to index * translationInterval.toPx()
            StackFrom.Left -> index * translationInterval.toPx() to 0f
            StackFrom.Right -> -index * translationInterval.toPx() to 0f
        }
    }

    val scale = when (stackFrom) {
        StackFrom.None -> 1f
        else -> 1f - (index * (1f - scaleInterval))
    }

    val alpha = when (stackFrom) {
        StackFrom.None -> 1f
        else -> 1f - (index * 0.2f)
    }

    // Calculate rotation based on drag offset
    val rotation by animateFloatAsState(
        targetValue = if (isTopCard) {
            val offset = state.requireOffset()
            (offset / 20f).coerceIn(-20f, 20f)
        } else 0f,
        label = "rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .zIndex((visibleCount - index).toFloat())
            .graphicsLayer {
                this.scaleX = scale
                this.scaleY = scale
                this.alpha = alpha
                this.rotationZ = rotation
                this.translationX = baseTranslationX + if (isTopCard) state.requireOffset() else 0f
                this.translationY = baseTranslationY
                transformOrigin = TransformOrigin(0.5f, 0f)
                clip = false
            }
            .then(
                if (isTopCard) {
                    Modifier.anchoredDraggable(
                        state = state,
                        orientation = Orientation.Horizontal,
                        flingBehavior = flingBehavior
                    )
                } else {
                    Modifier
                }
            )
    ) {
        Box(modifier = Modifier.wrapContentWidth()) {
            ProfileCard(
                profile = profile,
                modifier = Modifier.clickable { onCardClicked() },
                onDislike = {
                    if (isTopCard) {
                        scope.launch {
                            state.animateTo(DragValue.Left)
                        }
                    }
                },
                onLike = {
                    if (isTopCard) {
                        scope.launch {
                            state.animateTo(DragValue.Right)
                        }
                    }
                }
            )

            if (isTopCard) {
                val offset = state.requireOffset()
                if (abs(offset) > 100f) {
                    SwipeOverlay(
                        offset = offset,
                        modifier = Modifier.matchParentSize()
                    )
                }
            }
        }
    }
}

@Composable
fun SwipeOverlay(
    offset: Float,
    modifier: Modifier = Modifier
) {
    val alpha = (abs(offset) / 500f).coerceIn(0f, 0.7f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(600.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                when {
                    offset > 0 -> Color.Green.copy(alpha = alpha)
                    else -> Color.Red.copy(alpha = alpha)
                }
            ),

        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = when {
                offset > 0 -> Icons.Default.Favorite
                else -> Icons.Default.Close
            },
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(80.dp)
        )
    }
}