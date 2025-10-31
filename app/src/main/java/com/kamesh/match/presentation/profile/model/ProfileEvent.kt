package com.kamesh.match.presentation.profile.model

import com.kamesh.match.domain.model.Profile

/**
 * Represents all possible user interactions and events on the profile screen.
 * This sealed class is used to pass events from the UI to the ViewModel.
 */
sealed class ProfileEvent {
    data class SelectProfile(val profile: Profile) : ProfileEvent()
    data class LikeProfile(val profile: Profile) : ProfileEvent()
    data class DislikeProfile(val profile: Profile) : ProfileEvent()
    data object CardSwiped : ProfileEvent()
    data object ClearProfileSelection : ProfileEvent()
}