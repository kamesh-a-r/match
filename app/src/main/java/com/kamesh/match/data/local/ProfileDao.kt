package com.kamesh.match.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kamesh.match.domain.model.Profile
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the profile table.
 * Provides methods for accessing and manipulating profile data in the local database.
 */
@Dao
interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfiles(profiles: List<Profile>)

    @Query("SELECT * FROM profiles")
    fun getProfiles(): Flow<List<Profile>>

    @Delete
    suspend fun deleteProfile(profile: Profile)
}

