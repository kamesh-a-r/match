package com.kamesh.match.domain.usecase

import com.kamesh.match.domain.model.Profile
import com.kamesh.match.domain.model.ProfileType
import com.kamesh.match.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * A use case that removes a specific profile based on its type from the repository.
 *
 * This class follows the single-responsibility principle, encapsulating the business logic
 * for removing a profile of a certain type (e.g., liked, disliked). It uses a repository
 * to perform the actual data manipulation.
 *
 * @property repository The [ProfileRepository] used to access and modify profile data.
 */
class RemoveProfileByTypeUseCase (
     val repository: ProfileRepository
) {
    suspend operator fun invoke(profile: Profile, type: ProfileType) {
        repository.removeProfileByType(profile, type)
    }
}
