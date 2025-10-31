package com.kamesh.match.presentation.widget.cardStack.model

/**
 * Represents the direction from which a card should be stacked or animated.
 * This is used to determine the entry/exit animation and position of cards
 * in a card stack layout.
 */
enum class StackFrom {
    None,
    Top,
    Bottom,
    Left,
    Right
}
/**
 * Represents the horizontal drag state of a card in the stack.
 * This indicates the current position or the direction of the swipe gesture.
 */
enum class DragValue {
     Center,
    Left,
    Right
}
