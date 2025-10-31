package com.kamesh.match.domain.repository

import com.kamesh.match.domain.model.Profile
import com.kamesh.match.domain.model.ProfileType
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for a data repository that manages user profiles.
 * This repository is responsible for fetching, and performing actions like liking, disliking,
 * and removing profiles from the data source.
 */
interface ProfileRepository {
    fun getProfiles(): Flow<List<Profile>>
    fun getProfilesByType(type: ProfileType): Flow<List<Profile>>
    suspend fun likeProfile(profile: Profile)
    suspend fun dislikeProfile(profile: Profile)
    suspend fun removeProfile(profile: Profile)
    suspend fun removeProfileByType(profile: Profile, type: ProfileType)
}
