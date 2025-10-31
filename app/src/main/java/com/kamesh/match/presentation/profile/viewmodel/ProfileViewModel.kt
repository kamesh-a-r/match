package com.kamesh.match.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamesh.match.domain.model.ProfileType
import com.kamesh.match.domain.usecase.ProfileUseCases
import com.kamesh.match.presentation.profile.model.ProfileEvent
import com.kamesh.match.presentation.profile.model.ProfileState
import com.kamesh.match.presentation.profile.model.ProfileStateProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Profile Screen (Daily Matches)
 * 
 * Manages profiles shown on the DAILY screen where users can:
 * - View daily recommended profiles
 * - Like/dislike profiles (removes from daily list)
 * - Navigate to detailed profile view
 * 
 * Key Features:
 * - Loads profiles with DAILY type from database
 * - Reactive UI updates via StateFlow
 * - Handles user interactions (like, dislike, select)
 * - Navigation events via SharedFlow
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases
) : ViewModel(), ProfileStateProvider {

    // UI state for the profile screen
    private val _state = MutableStateFlow(ProfileState())
    override val state: StateFlow<ProfileState> = _state.asStateFlow()

    // Navigation events (when user taps on profile card)
    private val _navigation = MutableSharedFlow<Unit>()
    val navigation = _navigation.asSharedFlow()

    init {
        // Load daily profiles when ViewModel is created
        loadProfiles()
    }

    /**
     * Load profiles for DAILY screen from database
     * Updates UI state with loading status and profile list
     */
    private fun loadProfiles() {
        _state.update { it.copy(isLoading = true) }
        profileUseCases.getProfilesByType(ProfileType.DAILY).onEach { profiles ->
            _state.update {
                it.copy(
                    isLoading = false,
                    profiles = profiles
                )
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Handle user interactions with profiles
     * Processes different types of events from the UI
     */
    override fun onEvent(event: ProfileEvent) {
        when (event) {
            // User tapped on a profile card to view details
            is ProfileEvent.SelectProfile -> {
                _state.update { it.copy(selectedProfile = event.profile) }
                // Trigger navigation to detail screen
                viewModelScope.launch { _navigation.emit(Unit) }
            }
            
            // User liked a profile (heart button)
            is ProfileEvent.LikeProfile -> {
                viewModelScope.launch {
                    // Record the like action
                    profileUseCases.likeProfile(event.profile)
                    // Remove from DAILY screen (but stays in HOME if present)
                    profileUseCases.removeProfileByType(event.profile, ProfileType.DAILY)
                }
            }
            
            // User disliked a profile (X button)
            is ProfileEvent.DislikeProfile -> {
                viewModelScope.launch {
                    // Record the dislike action
                    profileUseCases.dislikeProfile(event.profile)
                    // Remove from DAILY screen (but stays in HOME if present)
                    profileUseCases.removeProfileByType(event.profile, ProfileType.DAILY)
                }
            }
            
            // Card swipe animation completed
            ProfileEvent.CardSwiped -> {
                // Optional: Handle any post-swipe actions
            }
            
            // Clear selected profile (after navigation or back press)
            ProfileEvent.ClearProfileSelection -> {
                _state.update { it.copy(selectedProfile = null) }
            }
        }
    }

}