package com.kamesh.match.domain.usecase

import com.kamesh.match.domain.model.Profile
import com.kamesh.match.domain.repository.ProfileRepository
import javax.inject.Inject

class RemoveProfileUseCase (
    val repository: ProfileRepository
) {
    suspend operator fun invoke(profile: Profile) {
        repository.removeProfile(profile)
    }
}
