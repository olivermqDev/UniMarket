package com.unimarket.app.data.model

import com.google.firebase.Timestamp

data class Producto(
    val id: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val imagenes: List<String> = emptyList(),
    val categoria: String = "",
    val fechaPublicacion: Timestamp = Timestamp.now(),
    val estado: String = "Disponible", // Disponible, Vendido, Reservado
    val condicion: String = "Nuevo", // Nuevo, Usado
    val vendedorId: String = "",
    val vendedorNombre: String = "",
    val vendedorFoto: String = "",
    val vendedorUniversidad: String = ""
) {
    fun toMap(): Map<String, Any> = mapOf(
        "id" to id,
        "nombre" to nombre,
        "descripcion" to descripcion,
        "precio" to precio,
        "imagenes" to imagenes,
        "categoria" to categoria,
        "fechaPublicacion" to fechaPublicacion,
        "estado" to estado,
        "condicion" to condicion,
        "vendedorId" to vendedorId,
        "vendedorNombre" to vendedorNombre,
        "vendedorFoto" to vendedorFoto,
        "vendedorUniversidad" to vendedorUniversidad
    )
}