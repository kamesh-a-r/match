package com.kamesh.match.domain.usecase

import javax.inject.Inject

/**
 * A container for all profile-related use cases, acting as a single entry point
 * for the UI layer (e.g., a ViewModel) to interact with profile business logic.
 *
 * This class follows the Facade pattern to simplify the interface to a set of
 * underlying use case classes.
 *
 * @property getProfiles Use case to retrieve a specific profile.
 * @property getProfilesByType Use case to retrieve profiles of a certain type.
 * @property likeProfile Use case for liking a profile.
 * @property dislikeProfile Use case for disliking a profile.
 * @property removeProfile Use case for removing a specific profile.
 * @property removeProfileByType Use case for removing all profiles of a certain type.
 */
data class ProfileUseCases (
    val getProfiles: GetProfileUseCase,
    val getProfilesByType: GetProfilesByTypeUseCase,
    val likeProfile: LikeProfileUseCase,
    val dislikeProfile: DislikeProfileUseCase,
    val removeProfile: RemoveProfileUseCase,
    val removeProfileByType: RemoveProfileByTypeUseCase
)
