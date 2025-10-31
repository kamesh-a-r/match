package com.kamesh.match.presentation.home.viewmodel

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

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases
) : ViewModel(), ProfileStateProvider {

    private val _state = MutableStateFlow(ProfileState())
    override val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _navigation = MutableSharedFlow<Unit>()
    val navigation = _navigation.asSharedFlow()

    init {
        loadProfiles()
    }

    private fun loadProfiles() {
        _state.update { it.copy(isLoading = true) }
        profileUseCases.getProfilesByType(ProfileType.HOME).onEach { profiles ->
            _state.update {
                it.copy(
                    isLoading = false,
                    profiles = profiles
                )
            }
        }.launchIn(viewModelScope)
    }

    override fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.SelectProfile -> {
                _state.update { it.copy(selectedProfile = event.profile) }
                viewModelScope.launch { _navigation.emit(Unit) }
            }
            is ProfileEvent.LikeProfile -> {
                viewModelScope.launch {
                    profileUseCases.likeProfile(event.profile)
                    profileUseCases.removeProfileByType(event.profile, ProfileType.HOME)
                }
            }
            is ProfileEvent.DislikeProfile -> {
                viewModelScope.launch {
                    profileUseCases.dislikeProfile(event.profile)
                    profileUseCases.removeProfileByType(event.profile, ProfileType.HOME)
                }
            }
            ProfileEvent.CardSwiped -> {
                // Optional: Handle card swiped event
            }
            ProfileEvent.ClearProfileSelection -> {
                _state.update { it.copy(selectedProfile = null) }
            }
        }
    }
}
