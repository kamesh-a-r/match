package com.kamesh.match.domain.usecase

import com.kamesh.match.domain.model.Profile
import com.kamesh.match.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * A use case that retrieves a list of profiles from the repository.
 *
 * This class follows the single responsibility principle, encapsulating the business logic
 * for fetching all available profiles. It acts as an intermediary between the ViewModel
 * and the repository layer.
 *
 * @property repository The [ProfileRepository] from which to fetch the profiles.
 */
class GetProfileUseCase (val repository: ProfileRepository) {
    operator fun invoke(): Flow<List<Profile>> {
        return repository.getProfiles()
    }
}
