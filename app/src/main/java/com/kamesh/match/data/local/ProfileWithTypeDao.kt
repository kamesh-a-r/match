package com.kamesh.match.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kamesh.match.domain.model.ProfileType
import com.kamesh.match.domain.model.ProfileWithType
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the `profile_with_type` table.
 *
 * This interface provides methods for interacting with the local database storage
 * of profiles that have been categorized by a specific type (e.g., accepted, declined).
 * It supports operations like inserting, querying, and deleting profiles based on their type.
 */
@Dao
interface ProfileWithTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfiles(profiles: List<ProfileWithType>)

    @Query("SELECT * FROM profile_with_type WHERE type = :type")
    fun getProfilesByType(type: ProfileType): Flow<List<ProfileWithType>>

    @Query("DELETE FROM profile_with_type WHERE profileId = :profileId AND type = :type")
    suspend fun deleteProfileByIdAndType(profileId: Int, type: ProfileType)

    @Query("SELECT COUNT(*) FROM profile_with_type WHERE type = :type")
    suspend fun getProfileCountByType(type: ProfileType): Int

    @Query("DELETE FROM profile_with_type WHERE type = :type")
    suspend fun deleteAllByType(type: ProfileType)
}
