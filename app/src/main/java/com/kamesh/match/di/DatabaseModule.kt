package com.kamesh.match.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kamesh.match.data.local.AppDatabase
import com.kamesh.match.data.local.ProfileDao
import com.kamesh.match.data.local.ProfileWithTypeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt Dependency Injection Module for Database Components
 * 
 * This module provides:
 * - Room database instance (singleton)
 * - Data Access Objects (DAOs) for database operations
 * 
 * Key Features:
 * - Database is created only once per app lifecycle
 * - Auto-populates with demo data on first install
 * - Resets data every app restart for consistent demo experience
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides the main Room database instance
     * 
     * Configuration:
     * - Uses "match_database" as database name
     * - Handles schema changes with destructive migration (demo app)
     * - Sets up callbacks for data management
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "match_database"
        )
            // Allow destructive migration for demo (loses data but prevents crashes)
            .fallbackToDestructiveMigration(false)
            .addCallback(object : RoomDatabase.Callback() {
                // Called only on first install
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    AppDatabase.prepopulate(db)
                }
                
                // Called every time app opens (including after kill/restart)
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    // Reset to fresh demo data - ensures consistent experience
                    AppDatabase.clearAndRepopulate(db)
                }
            })
            .build()
    }

    /**
     * Provides DAO for basic profile operations
     * Used for general profile data access
     */
    @Provides
    fun provideProfileDao(appDatabase: AppDatabase): ProfileDao {
        return appDatabase.profileDao()
    }

    /**
     * Provides DAO for screen-specific profile operations  
     * Used to manage profiles separately for HOME and DAILY screens
     */
    @Provides
    fun provideProfileWithTypeDao(appDatabase: AppDatabase): ProfileWithTypeDao {
        return appDatabase.profileWithTypeDao()
    }
}
