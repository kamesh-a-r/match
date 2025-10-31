package com.kamesh.match.domain.model

/**
 * Represents the different types of profile lists or contexts within the app.
 * This is used to distinguish between profiles shown on the main home screen
 * and those presented as daily recommendations.
 */
enum class ProfileType {
    HOME,      // For home screen matches
    DAILY      // For daily recommendations (profile screen)
}
