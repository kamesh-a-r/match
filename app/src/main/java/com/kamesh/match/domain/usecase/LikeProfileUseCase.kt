package com.kamesh.match.domain.usecase

import com.kamesh.match.domain.model.Profile
import com.kamesh.match.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * A use case that handles the business logic for liking a user profile.
 *
 * This class is a single-action class that can be invoked as a function. It delegates the
 * actual data operation of liking the profile to the provided repository.
 *
 * @property repository The repository responsible for profile data operations.
 */
class LikeProfileUseCase (
     val repository: ProfileRepository
) {
    suspend operator fun invoke(profile: Profile) {
        repository.likeProfile(profile)
    }
}
