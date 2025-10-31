package com.kamesh.match.domain.usecase

import com.kamesh.match.domain.model.Profile
import com.kamesh.match.domain.model.ProfileType
import com.kamesh.match.domain.repository.ProfileRepository
import javax.inject.Inject

class RemoveProfileByTypeUseCase (
     val repository: ProfileRepository
) {
    suspend operator fun invoke(profile: Profile, type: ProfileType) {
        repository.removeProfileByType(profile, type)
    }
}
