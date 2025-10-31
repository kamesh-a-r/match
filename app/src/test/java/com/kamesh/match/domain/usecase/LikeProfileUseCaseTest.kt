package com.kamesh.match.domain.usecase

import com.kamesh.match.domain.model.Profile
import com.kamesh.match.domain.repository.ProfileRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.UUID

class LikeProfileUseCaseTest {

    private lateinit var repository: ProfileRepository
    private lateinit var useCase: LikeProfileUseCase

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

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = LikeProfileUseCase(repository)
    }

    @Test
    fun `invoke should call repository likeProfile`() = runTest {
        // When
        useCase(testProfile)

        // Then
        coVerify { repository.likeProfile(testProfile) }
    }
}
