package com.kamesh.match.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity that stores profiles with their type (HOME or DAILY).
 * This allows maintaining separate lists for different screens.
 */
@Entity(tableName = "profile_with_type")
data class ProfileWithType(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val profileId: Int,
    val profileName: String,
    val type: ProfileType,
    val age: Int,
    val height: String,
    val profession: String,
    val star: String,
    val religion: String,
    val location: String,
    val isVerified: Boolean,
    val isPremiumNri: Boolean,
    val imageUrl: String,
    val attachments: List<String> = emptyList(),
    val photoCount: Int
) {
    fun toProfile(): Profile {
        return Profile(
            name = profileName,
            age = age,
            id = profileId,
            height = height,
            profession = profession,
            star = star,
            religion = religion,
            location = location,
            isVerified = isVerified,
            isPremiumNri = isPremiumNri,
            imageUrl = imageUrl,
            attachments = attachments,
            photoCount = photoCount
        )
    }

    companion object {
        fun fromProfile(profile: Profile, type: ProfileType): ProfileWithType {
            return ProfileWithType(
                profileId = profile.id,
                profileName = profile.name,
                type = type,
                age = profile.age,
                height = profile.height,
                profession = profile.profession,
                star = profile.star,
                religion = profile.religion,
                location = profile.location,
                isVerified = profile.isVerified,
                isPremiumNri = profile.isPremiumNri,
                imageUrl = profile.imageUrl,
                attachments = profile.attachments,
                photoCount = profile.photoCount
            )
        }
    }
}
