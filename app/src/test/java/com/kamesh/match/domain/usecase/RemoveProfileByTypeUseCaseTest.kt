package com.kamesh.match.domain.usecase

import com.kamesh.match.domain.model.Profile
import com.kamesh.match.domain.model.ProfileType
import com.kamesh.match.domain.repository.ProfileRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RemoveProfileByTypeUseCaseTest {

    private lateinit var repository: ProfileRepository
    private lateinit var useCase: RemoveProfileByTypeUseCase

    private val testProfile = Profile(
        name = "Test User",
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

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = RemoveProfileByTypeUseCase(repository)
    }

    @Test
    fun `invoke should remove profile from HOME type`() = runTest {
        // When
        useCase(testProfile, ProfileType.HOME)

        // Then
        coVerify { repository.removeProfileByType(testProfile, ProfileType.HOME) }
    }

    @Test
    fun `invoke should remove profile from DAILY type`() = runTest {
        // When
        useCase(testProfile, ProfileType.DAILY)

        // Then
        coVerify { repository.removeProfileByType(testProfile, ProfileType.DAILY) }
    }
}
