package com.unimarket.app.data.model

import com.google.firebase.Timestamp

data class User(
    val uid: String = "",
    val nombre: String = "",
    val email: String = "",
    val universidad: String = "",
    val fotoPerfil: String = "",
    val numeroCelular: String = "",
    val ubicacion: String = "",
    val fechaRegistro: Timestamp = Timestamp.now()
) {
    fun toMap(): Map<String, Any> = mapOf(
        "uid" to uid,
        "nombre" to nombre,
        "email" to email,
        "universidad" to universidad,
        "fotoPerfil" to fotoPerfil,
        "numeroCelular" to numeroCelular,
        "ubicacion" to ubicacion,
        "fechaRegistro" to fechaRegistro
    )
}
