package com.kamesh.match.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class Profile(
    @PrimaryKey
    val name: String,
    val age: Int,
    val id: Int,
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
)
