package com.heppihome.modules

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.heppihome.data.sources.web.FirestoreDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class FirestoreModule {

    @Singleton
    @Provides
    fun provideFirestore() : FirebaseFirestore
        = FirebaseFirestore.getInstance()

    @Provides
    fun provideFirestoreDao(firestore: FirebaseFirestore) : FirestoreDAO {
        return FirestoreDAO()
    }
}