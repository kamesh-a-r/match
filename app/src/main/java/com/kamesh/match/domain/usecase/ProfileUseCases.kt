package com.kamesh.match.domain.usecase

import javax.inject.Inject

/**
 * Facade class that combines all profile-related use cases.
 * This provides a single interface for the ProfileViewModel to interact with.
 */
data class ProfileUseCases (
    val getProfiles: GetProfileUseCase,
    val getProfilesByType: GetProfilesByTypeUseCase,
    val likeProfile: LikeProfileUseCase,
    val dislikeProfile: DislikeProfileUseCase,
    val removeProfile: RemoveProfileUseCase,
    val removeProfileByType: RemoveProfileByTypeUseCase
)
