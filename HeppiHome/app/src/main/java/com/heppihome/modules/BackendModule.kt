package com.heppihome.modules

import com.google.firebase.firestore.FirebaseFirestore
import com.heppihome.data.sources.test.Backend
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class BackendModule {
    @Singleton
    @Provides
    fun provideBackend() : Backend {
        return Backend()
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestore() : FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}