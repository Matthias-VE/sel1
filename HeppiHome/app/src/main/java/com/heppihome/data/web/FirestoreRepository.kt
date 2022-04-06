package com.heppihome.data.web

import com.google.firebase.firestore.FirebaseFirestore
import com.heppihome.BuildConfig

class FirestoreRepository(private val Firestore : FirebaseFirestore = FirebaseFirestore.getInstance()) {

    private val useEmulators : Boolean = BuildConfig.DEBUG

    fun getFirestore() : FirebaseFirestore {
            if (useEmulators) {
                Firestore.useEmulator("10.0.2.2", 8080)
            }
        return Firestore
    }

}