package com.kamesh.match.presentation.home.viewmodel

import com.kamesh.match.domain.model.Profile
import com.kamesh.match.domain.model.ProfileType
import com.kamesh.match.domain.usecase.DislikeProfileUseCase
import com.kamesh.match.domain.usecase.GetProfilesByTypeUseCase
import com.kamesh.match.domain.usecase.GetProfileUseCase
import com.kamesh.match.domain.usecase.LikeProfileUseCase
import com.kamesh.match.domain.usecase.ProfileUseCases
import com.kamesh.match.domain.usecase.RemoveProfileByTypeUseCase
import com.kamesh.match.domain.usecase.RemoveProfileUseCase
import com.kamesh.match.presentation.profile.model.ProfileEvent
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var getProfileUseCase: GetProfileUseCase
    private lateinit var getProfilesByTypeUseCase: GetProfilesByTypeUseCase
    private lateinit var likeProfileUseCase: LikeProfileUseCase
    private lateinit var dislikeProfileUseCase: DislikeProfileUseCase
    private lateinit var removeProfileUseCase: RemoveProfileUseCase
    private lateinit var removeProfileByTypeUseCase: RemoveProfileByTypeUseCase
    private lateinit var profileUseCases: ProfileUseCases
    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val testProfiles = listOf(
        Profile(
            name = "Test User 1",
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
        ),
        Profile(
            name = "Test User 2",
            age = 28,
            id = UUID.randomUUID().toString(),
            height = "5'8\"",
            profession = "Doctor",
            star = "Leo",
            religion = "Christian",
            location = "Delhi",
            isVerified = false,
            isPremiumNri = true,
            imageUrl = "https://example.com/2.jpg",
            attachments = emptyList(),
            photoCount = 2
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getProfileUseCase = mockk(relaxed = true)
        getProfilesByTypeUseCase = mockk()
        likeProfileUseCase = mockk(relaxed = true)
        dislikeProfileUseCase = mockk(relaxed = true)
        removeProfileUseCase = mockk(relaxed = true)
        removeProfileByTypeUseCase = mockk(relaxed = true)

        profileUseCases = ProfileUseCases(
            getProfiles = getProfileUseCase,
            getProfilesByType = getProfilesByTypeUseCase,
            likeProfile = likeProfileUseCase,
            dislikeProfile = dislikeProfileUseCase,
            removeProfile = removeProfileUseCase,
            removeProfileByType = removeProfileByTypeUseCase
        )

        every { getProfilesByTypeUseCase(ProfileType.HOME) } returns flowOf(testProfiles)

        viewModel = HomeViewModel(profileUseCases)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be loading`() {
        // Then
        assertTrue(viewModel.state.value.isLoading)
    }

    @Test
    fun `should load HOME profiles on init`() = runTest {
        // When
        advanceUntilIdle()

        // Then
        assertFalse(viewModel.state.value.isLoading)
        assertEquals(testProfiles, viewModel.state.value.profiles)
        io.mockk.verify { getProfilesByTypeUseCase.invoke(ProfileType.HOME) }
    }

    @Test
    fun `SelectProfile event should update selectedProfile`() = runTest {
        // Given
        advanceUntilIdle()
        val profile = testProfiles[0]

        // When
        viewModel.onEvent(ProfileEvent.SelectProfile(profile))

        // Then
        assertEquals(profile, viewModel.state.value.selectedProfile)
    }

    @Test
    fun `LikeProfile event should call likeProfile and removeProfileByType`() = runTest {
        // Given
        advanceUntilIdle()
        val profile = testProfiles[0]

        // When
        viewModel.onEvent(ProfileEvent.LikeProfile(profile))
        advanceUntilIdle()

        // Then
        coVerify { likeProfileUseCase.invoke(profile) }
        coVerify { removeProfileByTypeUseCase.invoke(profile, ProfileType.HOME) }
    }

    @Test
    fun `DislikeProfile event should call dislikeProfile and removeProfileByType`() = runTest {
        // Given
        advanceUntilIdle()
        val profile = testProfiles[0]

        // When
        viewModel.onEvent(ProfileEvent.DislikeProfile(profile))
        advanceUntilIdle()

        // Then
        coVerify { dislikeProfileUseCase.invoke(profile) }
        coVerify { removeProfileByTypeUseCase.invoke(profile, ProfileType.HOME) }
    }

    @Test
    fun `ClearProfileSelection event should clear selectedProfile`() = runTest {
        // Given
        advanceUntilIdle()
        viewModel.onEvent(ProfileEvent.SelectProfile(testProfiles[0]))
        assertEquals(testProfiles[0], viewModel.state.value.selectedProfile)

        // When
        viewModel.onEvent(ProfileEvent.ClearProfileSelection)

        // Then
        assertNull(viewModel.state.value.selectedProfile)
    }
}
