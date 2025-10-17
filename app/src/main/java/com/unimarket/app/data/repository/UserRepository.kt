package com.unimarket.app.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.unimarket.app.data.model.User
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    suspend fun getUser(userId: String): Result<User> {
        return try {
            val userData = firestore.collection("users").document(userId).get().await()
            val user = userData.toObject(User::class.java) ?: throw Exception("Usuario no encontrado")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(userId: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            firestore.collection("users").document(userId).update(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfilePhoto(userId: String, photoUri: Uri): Result<String> {
        return try {
            val storageRef = storage.reference.child("profile_images/$userId/${System.currentTimeMillis()}.jpg")
            storageRef.putFile(photoUri).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()

            firestore.collection("users").document(userId)
                .update("fotoPerfil", downloadUrl).await()

            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}