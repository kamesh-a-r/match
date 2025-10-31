package com.kamesh.match.presentation.widget.cardStack.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.kamesh.match.domain.model.Profile
import com.kamesh.match.presentation.widget.cardStack.model.DragValue
import com.kamesh.match.presentation.widget.cardStack.model.StackFrom
import kotlinx.coroutines.launch


/**
 * A swipeable card stack composable that displays profiles in a stacked layout.
 *
 * This composable creates a visually appealing card stack where:
 * - The top card is fully interactive and can be swiped left (dislike) or right (like)
 * - Cards behind are slightly scaled down and offset to create a depth effect
 * - Swipe gestures trigger animations and callbacks
 * - Supports both finite and infinite loop modes
 *
 * ## Features:
 * - **Swipe Gestures**: Drag the top card horizontally to like/dislike
 * - **Visual Feedback**: Rotation and overlay effects during swipe
 * - **Snackbar Notifications**: Shows feedback after each swipe
 * - **Stacking Options**: Configure how cards stack (top, bottom, left, right, or none)
 * - **Infinite Loop**: Optionally cycle through profiles endlessly
 * - **Click Handling**: Tap cards to view full profile details
 *
 * ## Implementation Notes:
 * - Cards are rendered in reverse order to ensure proper z-ordering
 * - The `initialTopIndex` should remain constant while `profiles` list shrinks
 * - In infinite mode, profiles wrap around using modulo arithmetic
 * - Swipe threshold is a fraction (0.0-1.0) of the drag distance needed to trigger a swipe
 *
 * @param profiles The list of profiles to display in the card stack
 * @param modifier Modifier to apply to the root container
 * @param initialTopIndex The starting index of the top card (useful for maintaining state)
 * @param visibleCount Number of cards visible in the stack (default: 3). Higher values show more depth
 * @param infiniteLoop If true, cycles through profiles endlessly. If false, stops when list ends
 * @param stackFrom Direction from which cards stack (Top, Bottom, Left, Right, or None)
 * @param translationInterval Spacing between stacked cards in dp (default: 8.dp)
 * @param scaleInterval Scale reduction per card (default: 0.95f). Each card is 5% smaller than the one in front
 * @param swipeThreshold Fraction of screen width needed to trigger a swipe (default: 0.3f = 30%)
 * @param onSwiped Callback invoked when a card is swiped, receives the profile and swipe direction
 * @param onAllCardsSwiped Callback invoked when all cards have been swiped (only in non-infinite mode)
 * @param onCardClicked Callback invoked when a card is tapped/clicked
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopStackedCardView(
    profiles: List<Profile>,
    modifier: Modifier = Modifier,
    initialTopIndex: Int = 0,
    visibleCount: Int = 3,
    infiniteLoop: Boolean = false,
    stackFrom: StackFrom = StackFrom.Top,
    translationInterval: Dp = 8.dp,
    scaleInterval: Float = 0.95f,
    swipeThreshold: Float = 0.3f,
    onSwiped: (Profile, DragValue) -> Unit = { _, _ -> },
    onAllCardsSwiped: () -> Unit = {},
    onCardClicked: (Profile) -> Unit = {}
) {
    // Get current density for converting dp to pixels in gesture calculations
    val density = LocalDensity.current
    
    // State for showing snackbar messages after swipes
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Coroutine scope for launching snackbar animations
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Calculate which profiles should be visible in the stack
        val visibleProfiles = if (infiniteLoop) {
            // Infinite mode: Use modulo to wrap around the list
            (0 until visibleCount).map { offset ->
                val index = (initialTopIndex + offset) % profiles.size
                profiles[index]
            }
        } else {
            // Finite mode: Only show profiles that exist in the list
            (0 until visibleCount)
                .map { offset -> initialTopIndex + offset }
                .filter { it < profiles.size }  // Don't go beyond list bounds
                .map { profiles[it] }
        }

        // Show empty state when no profiles are available
        if (visibleProfiles.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No more profiles!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Render cards in reverse order so the top card is drawn last (on top)
        // This ensures proper z-ordering for the stack effect
        visibleProfiles.reversed().forEachIndexed { reverseIndex, profile ->
            // Calculate the actual index (0 = top card, higher = cards behind)
            val index = visibleProfiles.size - 1 - reverseIndex

            // Use profile ID as key for proper recomposition when list changes
            key(profile.id) {
                SwipeableCard(
                    profile = profile,
                    index = index,
                    isTopCard = index == 0,
                    visibleCount = visibleCount,
                    stackFrom = stackFrom,
                    translationInterval = translationInterval,
                    scaleInterval = scaleInterval,
                    swipeThreshold = swipeThreshold,
                    density = density,
                    onSwiped = { direction ->
                        // Display feedback message based on swipe direction
                        val message = when (direction) {
                            DragValue.Left -> "You disliked ${profile.name}"
                            DragValue.Right -> "You liked ${profile.name} ❤️"
                            DragValue.Center -> ""  // No message for center (shouldn't happen)
                        }

                        // Show snackbar with the feedback message
                        if (message.isNotEmpty()) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = message,
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }

                        // Notify parent composable about the swipe
                        // Parent should handle removing the profile from the list
                        // Note: initialTopIndex stays the same as list shrinks
                        onSwiped(profile, direction)

                        // Trigger callback when the last card is swiped (finite mode only)
                        if (!infiniteLoop && profiles.size <= 1) {
                            onAllCardsSwiped()
                        }
                    },
                    onCardClicked = { onCardClicked(profile) }
                )
            }
        }

        // SnackbarHost at the bottom with high zIndex to appear above cards
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .zIndex(Float.MAX_VALUE)  // Ensure snackbar appears above all cards
        )
    }
}