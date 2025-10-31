package com.kamesh.match.data.repository

import android.util.Log
import com.kamesh.match.data.local.ProfileDao
import com.kamesh.match.data.local.ProfileWithTypeDao
import com.kamesh.match.domain.model.Profile
import com.kamesh.match.domain.model.ProfileType
import com.kamesh.match.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDao: ProfileDao,
    private val profileWithTypeDao: ProfileWithTypeDao
) : ProfileRepository {

    override fun getProfiles(): Flow<List<Profile>> {
        return profileDao.getProfiles()
    }

    override fun getProfilesByType(type: ProfileType): Flow<List<Profile>> {
        return profileWithTypeDao.getProfilesByType(type).map { profilesWithType ->
            profilesWithType.map { it.toProfile() }
        }
    }

    override suspend fun likeProfile(profile: Profile) {
        // TODO: Implement API call to record like
        Log.d("ProfileRepository", "Liked profile: ${profile.name}")
    }

    override suspend fun dislikeProfile(profile: Profile) {
        // TODO: Implement API call to record dislike
        Log.d("ProfileRepository", "Disliked profile: ${profile.name}")
    }

    override suspend fun removeProfile(profile: Profile) {
        profileDao.deleteProfile(profile)
    }

    override suspend fun removeProfileByType(profile: Profile, type: ProfileType) {
        profileWithTypeDao.deleteProfileByIdAndType(profile.id, type)
    }
}
