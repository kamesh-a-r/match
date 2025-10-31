package com.kamesh.match.presentation.profile.model

import com.kamesh.match.domain.model.Profile

sealed class ProfileEvent {
    data class SelectProfile(val profile: Profile) : ProfileEvent()
    data class LikeProfile(val profile: Profile) : ProfileEvent()
    data class DislikeProfile(val profile: Profile) : ProfileEvent()
    data object CardSwiped : ProfileEvent()
    data object ClearProfileSelection : ProfileEvent()
}