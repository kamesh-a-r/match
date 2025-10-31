package com.kamesh.match.domain.usecase

import com.kamesh.match.domain.model.Profile
import com.kamesh.match.domain.model.ProfileType
import com.kamesh.match.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * A use case that retrieves a list of profiles filtered by a specific type.
 *
 * This class acts as an intermediary between the ViewModel/UI layer and the data layer (repository),
 * encapsulating the business logic for fetching profiles based on their type (e.g., liked, passed).
 * It returns a Flow of a list of profiles, allowing for reactive updates to the UI.
 *
 * @param repository The [ProfileRepository] from which to fetch the profiles.
 */
class GetProfilesByTypeUseCase (
     val repository: ProfileRepository
) {
    operator fun invoke(type: ProfileType): Flow<List<Profile>> {
        return repository.getProfilesByType(type)
    }
}
