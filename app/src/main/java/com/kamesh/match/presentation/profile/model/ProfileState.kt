package com.kamesh.match.presentation.profile.model

import com.kamesh.match.domain.model.Profile

data class ProfileState(
    val profiles: List<Profile> = emptyList(),
    val isLoading: Boolean = false,
    val selectedProfile: Profile? = null,
    val currentCardIndex: Int = 0
)