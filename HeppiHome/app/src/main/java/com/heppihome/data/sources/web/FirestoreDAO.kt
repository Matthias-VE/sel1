package com.heppihome.data.sources.web

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.heppihome.BuildConfig
import com.heppihome.data.models.ResultState
import com.heppihome.data.models.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreDAO (private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {



}