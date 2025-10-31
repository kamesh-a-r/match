package com.kamesh.match.di

import com.kamesh.match.data.local.ProfileDao
import com.kamesh.match.data.local.ProfileWithTypeDao
import com.kamesh.match.data.repository.ProfileRepositoryImpl
import com.kamesh.match.domain.repository.ProfileRepository
import com.kamesh.match.domain.usecase.DislikeProfileUseCase
import com.kamesh.match.domain.usecase.GetProfileUseCase
import com.kamesh.match.domain.usecase.GetProfilesByTypeUseCase
import com.kamesh.match.domain.usecase.LikeProfileUseCase
import com.kamesh.match.domain.usecase.ProfileUseCases
import com.kamesh.match.domain.usecase.RemoveProfileByTypeUseCase
import com.kamesh.match.domain.usecase.RemoveProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun bindProfileRepository(profileDao: ProfileDao,profileWithTypeDao: ProfileWithTypeDao): ProfileRepository{
        return ProfileRepositoryImpl(
            profileDao = profileDao,
            profileWithTypeDao = profileWithTypeDao
        )
    }
    @Provides
    @Singleton
    fun provideGetProfileUseCase(repository: ProfileRepository): GetProfileUseCase {
        return GetProfileUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetProfilesByTypeUseCase(repository: ProfileRepository): GetProfilesByTypeUseCase {
        return GetProfilesByTypeUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLikeProfileUseCase(repository: ProfileRepository): LikeProfileUseCase {
        return LikeProfileUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDislikeProfileUseCase(repository: ProfileRepository): DislikeProfileUseCase {
        return DislikeProfileUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRemoveProfileUseCase(repository: ProfileRepository): RemoveProfileUseCase {
        return RemoveProfileUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRemoveProfileByTypeUseCase(repository: ProfileRepository): RemoveProfileByTypeUseCase {
        return RemoveProfileByTypeUseCase(repository)
    }


    @Provides
    @Singleton
    fun provideProfileUseCases(
        getProfileUseCase: GetProfileUseCase,
        getProfilesByTypeUseCase: GetProfilesByTypeUseCase,
        likeProfileUseCase: LikeProfileUseCase,
        dislikeProfileUseCase: DislikeProfileUseCase,
        removeProfileUseCase: RemoveProfileUseCase,
        removeProfileByTypeUseCase: RemoveProfileByTypeUseCase
    ): ProfileUseCases {
        return ProfileUseCases(
            getProfiles = getProfileUseCase,
            getProfilesByType = getProfilesByTypeUseCase,
            likeProfile = likeProfileUseCase,
            dislikeProfile = dislikeProfileUseCase,
            removeProfile = removeProfileUseCase,
            removeProfileByType = removeProfileByTypeUseCase
        )
    }
}
