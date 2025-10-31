package com.kamesh.match.domain.usecase

import com.kamesh.match.domain.model.Profile
import com.kamesh.match.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * A use case responsible for handling the business logic of disliking a user profile.
 *
 * This class acts as an intermediary between the ViewModel/UI layer and the data layer (repository).
 * It simplifies the action of disliking a profile to a single, invokable function.
 *
 * @property repository The [ProfileRepository] used to perform the dislike action on the data source.
 */
class DislikeProfileUseCase (
     val repository: ProfileRepository
) {
    suspend operator fun invoke(profile: Profile) {
        repository.dislikeProfile(profile)
    }
}
