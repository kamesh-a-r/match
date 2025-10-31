package com.kamesh.match.domain.usecase

import com.kamesh.match.domain.model.Profile
import com.kamesh.match.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * A use case responsible for removing a user profile from the repository.
 *
 * This class follows the single responsibility principle, encapsulating the business logic
 * for deleting a specific profile. It acts as an intermediary between the ViewModel/Presenter
 * and the data layer (repository).
 *
 * @property repository The [ProfileRepository] used to perform the delete operation.
 */
class RemoveProfileUseCase (
    val repository: ProfileRepository
) {
    suspend operator fun invoke(profile: Profile) {
        repository.removeProfile(profile)
    }
}
