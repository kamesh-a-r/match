package com.kamesh.match.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.UUID

/**
 * Unit tests for ProfileWithType model class
 * 
 * These tests verify the conversion functions between Profile and ProfileWithType:
 * - toProfile(): Converts ProfileWithType back to Profile
 * - fromProfile(): Creates ProfileWithType from Profile with a specific type
 * 
 * Why we need ProfileWithType:
 * - Same profile can appear in different screens (HOME, DAILY)
 * - Each screen needs its own copy to track separate state
 * - When user removes from HOME, it stays in DAILY
 */
class ProfileWithTypeTest {

    // Sample profile data used across all tests
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
        attachments = listOf("https://example.com/1.jpg", "https://example.com/2.jpg"),
        photoCount = 2
    )

    /**
     * Test: Convert ProfileWithType back to Profile
     * Verifies that all profile data is preserved during conversion
     */
    @Test
    fun `toProfile should convert ProfileWithType to Profile correctly`() {
        // Given: Create a ProfileWithType with test data
        val profileWithType = ProfileWithType(
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
            attachments = listOf("https://example.com/1.jpg", "https://example.com/2.jpg"),
            photoCount = 2
        )

        // When: Convert ProfileWithType back to Profile
        val result = profileWithType.toProfile()

        // Then: Verify all fields match original profile
        assertEquals(testProfile.name, result.name)
        assertEquals(testProfile.age, result.age)
        assertEquals(testProfile.id, result.id)
        assertEquals(testProfile.height, result.height)
        assertEquals(testProfile.profession, result.profession)
        assertEquals(testProfile.star, result.star)
        assertEquals(testProfile.religion, result.religion)
        assertEquals(testProfile.location, result.location)
        assertEquals(testProfile.isVerified, result.isVerified)
        assertEquals(testProfile.isPremiumNri, result.isPremiumNri)
        assertEquals(testProfile.imageUrl, result.imageUrl)
        assertEquals(testProfile.attachments, result.attachments)
        assertEquals(testProfile.photoCount, result.photoCount)
    }

    /**
     * Test: Create ProfileWithType from Profile for HOME screen
     * Verifies the profile gets tagged correctly for HOME screen usage
     */
    @Test
    fun `fromProfile should convert Profile to ProfileWithType with HOME type`() {
        // When: Convert profile for HOME screen
        val result = ProfileWithType.fromProfile(testProfile, ProfileType.HOME)

        // Then: Verify correct type assignment and data preservation
        assertEquals(testProfile.id, result.profileId)
        assertEquals(testProfile.name, result.profileName)
        assertEquals(ProfileType.HOME, result.type)
        assertEquals(testProfile.age, result.age)
        assertEquals(testProfile.height, result.height)
        assertEquals(testProfile.profession, result.profession)
    }

    /**
     * Test: Create ProfileWithType from Profile for DAILY screen
     * Verifies the same profile can be tagged for different screen types
     */
    @Test
    fun `fromProfile should convert Profile to ProfileWithType with DAILY type`() {
        // When: Convert profile for DAILY screen
        val result = ProfileWithType.fromProfile(testProfile, ProfileType.DAILY)

        // Then: Verify correct DAILY type assignment
        assertEquals(testProfile.id, result.profileId)
        assertEquals(testProfile.name, result.profileName)
        assertEquals(ProfileType.DAILY, result.type)
    }

    /**
     * Test: Conversion functions are reversible
     * Verifies that Profile → ProfileWithType → Profile preserves all data
     * This ensures no data loss during conversions
     */
    @Test
    fun `toProfile and fromProfile should be reversible`() {
        // Given: Convert profile to ProfileWithType
        val profileWithType = ProfileWithType.fromProfile(testProfile, ProfileType.HOME)

        // When: Convert back to Profile
        val convertedProfile = profileWithType.toProfile()

        // Then: Verify original data is preserved through both conversions
        assertEquals(testProfile.name, convertedProfile.name)
        assertEquals(testProfile.id, convertedProfile.id)
        assertEquals(testProfile.age, convertedProfile.age)
        assertEquals(testProfile.attachments, convertedProfile.attachments)
    }
}
