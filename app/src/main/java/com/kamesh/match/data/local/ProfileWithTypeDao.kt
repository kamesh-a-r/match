package com.kamesh.match.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kamesh.match.domain.model.ProfileType
import com.kamesh.match.domain.model.ProfileWithType
import kotlinx.coroutines.flow.Flow

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
