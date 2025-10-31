package com.kamesh.match.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.kamesh.match.domain.model.Profile
import com.kamesh.match.domain.model.ProfileType
import com.kamesh.match.domain.model.ProfileWithType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Room Database for the Match App
 * 
 * Contains two main tables:
 * 1. profiles - Store basic profile information
 * 2. profile_with_type - Store profiles categorized by screen type (HOME/DAILY)
 * 
 * Features:
 * - Auto-populates with dummy data on first run
 * - Resets to fresh data every app restart (for demo purposes)
 * - Uses TypeConverters to handle complex data types like Lists
 */
@Database(entities = [Profile::class, ProfileWithType::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    // Data Access Objects for database operations
    abstract fun profileDao(): ProfileDao
    abstract fun profileWithTypeDao(): ProfileWithTypeDao

    companion object {
        
        /**
         * Clear all existing data and repopulate with fresh static profiles
         * 
         * Called every time the app opens to ensure demo data is always available
         * Runs synchronously to prevent race conditions with UI queries
         */
        fun clearAndRepopulate(db: SupportSQLiteDatabase) {
            // Run synchronously to ensure data is ready before ViewModels query
            db.beginTransaction()
            try {
                // Step 1: Clear all existing profile data
                db.execSQL("DELETE FROM profile_with_type")
                db.execSQL("DELETE FROM profiles")
                
                // Step 2: Insert fresh static profiles
                insertProfiles(db)
                
                // Step 3: Commit the transaction
                db.setTransactionSuccessful()
            } finally {
                db.endTransaction()
            }
        }
        
        /**
         * Initial population of database on first app install
         * Runs asynchronously since no UI is waiting for this data yet
         */
        fun prepopulate(db: SupportSQLiteDatabase) {
            CoroutineScope(Dispatchers.IO).launch {
                insertProfiles(db)
            }
        }
        
        /**
         * Insert static demo profiles into the database
         * 
         * Creates 5 sample profiles with different characteristics:
         * - Different professions, ages, locations
         * - Some verified, some premium NRI status
         * - Various photo counts and attachment URLs
         * 
         * Each profile is inserted into:
         * 1. profiles table (basic profile data)
         * 2. profile_with_type table (for HOME screen)
         * 3. profile_with_type table (for DAILY screen)
         */
        private fun insertProfiles(db: SupportSQLiteDatabase) {
            // Define 5 static demo profiles
            val profiles = listOf(
                Profile(
                    name = "Ananya Sharma",
                    age = 26,
                    id = 1,
                    height = "5'6\"",
                    profession = "UI/UX Designer",
                    star = "Libra",
                    religion = "Hindu",
                    location = "Flat No. 12B, Greenview Apartments, T. Nagar, Chennai - 600017, Tamil Nadu",
                    isVerified = true,
                    isPremiumNri = false,
                    photoCount = 3,
                    imageUrl = "https://randomuser.me/api/portraits/women/68.jpg",
                    attachments = listOf(
                        "https://randomuser.me/api/portraits/women/68.jpg",
                        "https://randomuser.me/api/portraits/women/69.jpg",
                        "https://randomuser.me/api/portraits/women/70.jpg"
                    )
                ),
                Profile(
                    name = "Rachel Thomas",
                    age = 30,
                    id = 2,
                    height = "5'7\"",
                    profession = "iOS Developer",
                    star = "Aries",
                    religion = "Christian",
                    location = "No. 45, 2nd Cross Road, Indiranagar Stage 1, Bengaluru - 560038, Karnataka",
                    isVerified = false,
                    isPremiumNri = true,
                    photoCount = 1,
                    imageUrl = "https://randomuser.me/api/portraits/women/44.jpg",
                    attachments = listOf("https://randomuser.me/api/portraits/women/44.jpg")
                ),
                Profile(
                    name = "Priya Reddy",
                    age = 25,
                    id = 3,
                    height = "5'5\"",
                    profession = "Web Developer",
                    star = "Virgo",
                    religion = "Hindu",
                    location = "H.No 8-2-293/82, Road No. 36, Jubilee Hills, Hyderabad - 500033, Telangana",
                    isVerified = true,
                    isPremiumNri = false,
                    photoCount = 0,
                    imageUrl = "https://randomuser.me/api/portraits/women/32.jpg",
                    attachments = emptyList()
                ),
                Profile(
                    name = "Neha Patel",
                    age = 29,
                    id = 4,
                    height = "5'8\"",
                    profession = "Data Analyst",
                    star = "Cancer",
                    religion = "Hindu",
                    location = "B-302, Sunrise Residency, Baner Pashan Link Road, Pune - 411045, Maharashtra",
                    isVerified = false,
                    isPremiumNri = false,
                    photoCount = 5,
                    imageUrl = "https://randomuser.me/api/portraits/women/51.jpg",
                    attachments = listOf(
                        "https://randomuser.me/api/portraits/women/51.jpg",
                        "https://randomuser.me/api/portraits/women/52.jpg",
                        "https://randomuser.me/api/portraits/women/53.jpg",
                        "https://randomuser.me/api/portraits/women/54.jpg",
                        "https://randomuser.me/api/portraits/women/55.jpg"
                    )
                ),
                Profile(
                    name = "Sofia D’Souza",
                    age = 27,
                    id = 5,
                    height = "5'6\"",
                    profession = "AI Engineer",
                    star = "Scorpio",
                    religion = "Christian",
                    location = "Apartment No. 702, Palm Grove Towers, Carter Road, Bandra West, Mumbai - 400050, Maharashtra",
                    isVerified = true,
                    isPremiumNri = true,
                    photoCount = 8,
                    imageUrl = "https://randomuser.me/api/portraits/women/12.jpg",
                    attachments = listOf(
                        "https://randomuser.me/api/portraits/women/12.jpg",
                        "https://randomuser.me/api/portraits/women/13.jpg",
                        "https://randomuser.me/api/portraits/women/14.jpg",
                        "https://randomuser.me/api/portraits/women/15.jpg",
                        "https://randomuser.me/api/portraits/women/16.jpg",
                        "https://randomuser.me/api/portraits/women/17.jpg",
                        "https://randomuser.me/api/portraits/women/18.jpg",
                        "https://randomuser.me/api/portraits/women/19.jpg"
                    )
                )
            )

            // Process each profile and insert into all required tables
            profiles.forEach { profile ->
                // Step 1: Insert into main profiles table
                db.execSQL(
                    """
                    INSERT INTO profiles 
                    (id, name, age, height, profession, star, religion, location, isVerified, isPremiumNri, imageUrl, attachments, photoCount) 
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """.trimIndent(),
                    arrayOf<Any?>(
                        profile.id,
                        profile.name,
                        profile.age,
                        profile.height,
                        profile.profession,
                        profile.star,
                        profile.religion,
                        profile.location,
                        if (profile.isVerified) 1 else 0,
                        if (profile.isPremiumNri) 1 else 0,
                        profile.imageUrl,
                        Gson().toJson(profile.attachments),
                        profile.photoCount
                    )
                )
                
                // Step 2: Insert copy for HOME screen (user can swipe/remove independently)
                db.execSQL(
                    """
                    INSERT INTO profile_with_type 
                    (profileId, profileName, type, age, height, profession, star, religion, location, isVerified, isPremiumNri, imageUrl, attachments, photoCount) 
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """.trimIndent(),
                    arrayOf<Any?>(
                        profile.id,
                        profile.name,
                        ProfileType.HOME.name,
                        profile.age,
                        profile.height,
                        profile.profession,
                        profile.star,
                        profile.religion,
                        profile.location,
                        if (profile.isVerified) 1 else 0,
                        if (profile.isPremiumNri) 1 else 0,
                        profile.imageUrl,
                        Gson().toJson(profile.attachments),
                        profile.photoCount
                    )
                )
                
                // Step 3: Insert copy for DAILY screen (separate from HOME screen state)
                db.execSQL(
                    """
                    INSERT INTO profile_with_type 
                    (profileId, profileName, type, age, height, profession, star, religion, location, isVerified, isPremiumNri, imageUrl, attachments, photoCount) 
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """.trimIndent(),
                    arrayOf<Any?>(
                        profile.id,
                        profile.name,
                        ProfileType.DAILY.name,
                        profile.age,
                        profile.height,
                        profile.profession,
                        profile.star,
                        profile.religion,
                        profile.location,
                        if (profile.isVerified) 1 else 0,
                        if (profile.isPremiumNri) 1 else 0,
                        profile.imageUrl,
                        Gson().toJson(profile.attachments),
                        profile.photoCount
                    )
                )
            }
        }
    }
}
