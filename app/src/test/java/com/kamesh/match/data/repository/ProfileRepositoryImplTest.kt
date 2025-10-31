package com.kamesh.match.data.repository

import com.kamesh.match.data.local.ProfileDao
import com.kamesh.match.data.local.ProfileWithTypeDao
import com.kamesh.match.domain.model.Profile
import com.kamesh.match.domain.model.ProfileType
import com.kamesh.match.domain.model.ProfileWithType
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.UUID

class ProfileRepositoryImplTest {

    private lateinit var profileDao: ProfileDao
    private lateinit var profileWithTypeDao: ProfileWithTypeDao
    private lateinit var repository: ProfileRepositoryImpl

    private val testProfile = Profile(
        name = "Test User",
        age = 25,
        id = UUID.randomUUID().toString(),
        height = "5'6\"",
        profession = "Engineer",
        star = "Aries",
        religion = "Hindu",
        location = "Mumbai",
        isVerified = true,
        isPremiumNri = false,
        imageUrl = "https://example.com/1.jpg",
        attachments = emptyList(),
        photoCount = 1
    )

    private val testProfileWithType = ProfileWithType(
        id = 1,
        profileId = UUID.randomUUID().toString(),
        profileName = "Test User",
        type = ProfileType.HOME,
        age = 25,
        height = "5'6\"",
        profession = "Engineer",
        star = "Aries",
        religion = "Hindu",
        location = "Mumbai",
        isVerified = true,
        isPremiumNri = false,
        imageUrl = "https://example.com/1.jpg",
        attachments = emptyList(),
        photoCount = 1
    )

    @Before
    fun setup() {
        profileDao = mockk(relaxed = true)
        profileWithTypeDao = mockk(relaxed = true)
        repository = ProfileRepositoryImpl(profileDao, profileWithTypeDao)
    }

    @Test
    fun `getProfiles should return profiles from dao`() = runTest {
        // Given
        val profiles = listOf(testProfile)
        every { profileDao.getProfiles() } returns flowOf(profiles)

        // When
        val result = repository.getProfiles().first()

        // Then
        assertEquals(profiles, result)
        io.mockk.verify { profileDao.getProfiles() }
    }

    @Test
    fun `getProfilesByType should return profiles for HOME type`() = runTest {
        // Given
        val profilesWithType = listOf(testProfileWithType)
        every { profileWithTypeDao.getProfilesByType(ProfileType.HOME) } returns flowOf(profilesWithType)

        // When
        val result = repository.getProfilesByType(ProfileType.HOME).first()

        // Then
        assertEquals(1, result.size)
        assertEquals(testProfile.name, result[0].name)
        io.mockk.verify { profileWithTypeDao.getProfilesByType(ProfileType.HOME) }
    }

    @Test
    fun `removeProfile should call dao deleteProfile`() = runTest {
        // When
        repository.removeProfile(testProfile)

        // Then
        coVerify { profileDao.deleteProfile(testProfile) }
    }

    @Test
    fun `removeProfileByType should call dao deleteProfileByIdAndType`() = runTest {
        // When
        repository.removeProfileByType(testProfile, ProfileType.HOME)

        // Then
        coVerify { profileWithTypeDao.deleteProfileByIdAndType(testProfile.id, ProfileType.HOME) }
    }
}
