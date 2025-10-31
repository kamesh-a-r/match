package com.kamesh.match.domain.usecase

import com.kamesh.match.domain.model.Profile
import com.kamesh.match.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfileUseCase ( val repository: ProfileRepository) {
    operator fun invoke(): Flow<List<Profile>> {
        return repository.getProfiles()
    }
}
