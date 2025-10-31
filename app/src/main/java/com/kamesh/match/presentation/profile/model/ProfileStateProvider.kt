package com.kamesh.match.presentation.profile.model

import kotlinx.coroutines.flow.StateFlow

/**
 * Interface for ViewModels that provide ProfileState.
 * This allows ProfileDetailsScreen to work with both HomeViewModel and ProfileViewModel.
 */
interface ProfileStateProvider {
    val state: StateFlow<ProfileState>
    fun onEvent(event: ProfileEvent)
}
