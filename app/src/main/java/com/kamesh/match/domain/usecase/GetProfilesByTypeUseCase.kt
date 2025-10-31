package com.kamesh.match.domain.usecase

import com.kamesh.match.domain.model.Profile
import com.kamesh.match.domain.model.ProfileType
import com.kamesh.match.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfilesByTypeUseCase (
     val repository: ProfileRepository
) {
    operator fun invoke(type: ProfileType): Flow<List<Profile>> {
        return repository.getProfilesByType(type)
    }
}
