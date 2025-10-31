package com.kamesh.match.domain.usecase

import com.kamesh.match.domain.model.Profile
import com.kamesh.match.domain.model.ProfileType
import com.kamesh.match.domain.repository.ProfileRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetProfilesByTypeUseCaseTest {

    private lateinit var repository: ProfileRepository
    private lateinit var useCase: GetProfilesByTypeUseCase

    private val testProfiles = listOf(
        Profile(
            name = "Test User 1",
            age = 25,
            id = 1,
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
    )

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetProfilesByTypeUseCase(repository)
    }

    @Test
    fun `invoke should return profiles for HOME type`() = runTest {
        // Given
        every { repository.getProfilesByType(ProfileType.HOME) } returns flowOf(testProfiles)

        // When
        val result = useCase(ProfileType.HOME).first()

        // Then
        assertEquals(testProfiles, result)
        verify { repository.getProfilesByType(ProfileType.HOME) }
    }

    @Test
    fun `invoke should return profiles for DAILY type`() = runTest {
        // Given
        every { repository.getProfilesByType(ProfileType.DAILY) } returns flowOf(testProfiles)

        // When
        val result = useCase(ProfileType.DAILY).first()

        // Then
        assertEquals(testProfiles, result)
        verify { repository.getProfilesByType(ProfileType.DAILY) }
    }

    @Test
    fun `invoke should return empty list when no profiles exist`() = runTest {
        // Given
        every { repository.getProfilesByType(ProfileType.HOME) } returns flowOf(emptyList())

        // When
        val result = useCase(ProfileType.HOME).first()

        // Then
        assertEquals(emptyList<Profile>(), result)
    }
}
