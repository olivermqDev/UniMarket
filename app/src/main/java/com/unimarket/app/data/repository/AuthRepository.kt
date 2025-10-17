package com.unimarket.app.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.unimarket.app.data.model.User
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    val currentUser: FirebaseUser? get() = auth.currentUser

    suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: throw Exception("Usuario no encontrado")
            val userData = firestore.collection("users").document(userId).get().await()
            val user = userData.toObject(User::class.java) ?: throw Exception("Datos de usuario no encontrados")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(
        email: String,
        password: String,
        nombre: String,
        universidad: String,
        numeroCelular: String,
        ubicacion: String,
        fotoPerfilUri: Uri?
    ): Result<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: throw Exception("Error al crear usuario")

            var fotoPerfilUrl = ""
            if (fotoPerfilUri != null) {
                val storageRef = storage.reference.child("profile_images/$userId/${System.currentTimeMillis()}.jpg")
                storageRef.putFile(fotoPerfilUri).await()
                fotoPerfilUrl = storageRef.downloadUrl.await().toString()
            }

            val user = User(
                uid = userId,
                nombre = nombre,
                email = email,
                universidad = universidad,
                fotoPerfil = fotoPerfilUrl,
                numeroCelular = numeroCelular,
                ubicacion = ubicacion
            )

            firestore.collection("users").document(userId).set(user.toMap()).await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }
}