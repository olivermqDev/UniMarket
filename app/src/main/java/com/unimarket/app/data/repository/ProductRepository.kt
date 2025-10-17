package com.unimarket.app.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.unimarket.app.data.model.Producto
import kotlinx.coroutines.tasks.await

class ProductRepository(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    suspend fun createProduct(
        producto: Producto,
        imageUris: List<Uri>
    ): Result<String> {
        return try {
            val productId = firestore.collection("productos").document().id

            val imageUrls = imageUris.mapIndexed { index, uri ->
                val storageRef = storage.reference
                    .child("product_images/${producto.vendedorId}/$productId/$index.jpg")
                storageRef.putFile(uri).await()
                storageRef.downloadUrl.await().toString()
            }

            val productoConImagenes = producto.copy(
                id = productId,
                imagenes = imageUrls
            )

            firestore.collection("productos")
                .document(productId)
                .set(productoConImagenes.toMap())
                .await()

            Result.success(productId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProducts(
        categoria: String? = null,
        precioMin: Double? = null,
        precioMax: Double? = null,
        ordenarPor: String = "fechaPublicacion"
    ): Result<List<Producto>> {
        return try {
            var query: Query = firestore.collection("productos")
                .whereEqualTo("estado", "Disponible")

            if (categoria != null && categoria != "Todos") {
                query = query.whereEqualTo("categoria", categoria)
            }

            val snapshot = query.get().await()
            var productos = snapshot.documents.mapNotNull {
                it.toObject(Producto::class.java)
            }

            // Filtros de precio
            if (precioMin != null) {
                productos = productos.filter { it.precio >= precioMin }
            }
            if (precioMax != null) {
                productos = productos.filter { it.precio <= precioMax }
            }

            // Ordenamiento
            productos = when (ordenarPor) {
                "precio_asc" -> productos.sortedBy { it.precio }
                "precio_desc" -> productos.sortedByDescending { it.precio }
                else -> productos.sortedByDescending { it.fechaPublicacion }
            }

            Result.success(productos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProducts(userId: String): Result<List<Producto>> {
        return try {
            val snapshot = firestore.collection("productos")
                .whereEqualTo("vendedorId", userId)
                .get()
                .await()

            val productos = snapshot.documents.mapNotNull {
                it.toObject(Producto::class.java)
            }

            Result.success(productos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProduct(productId: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            firestore.collection("productos")
                .document(productId)
                .update(updates)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProduct(productId: String): Result<Unit> {
        return try {
            firestore.collection("productos")
                .document(productId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchProducts(query: String): Result<List<Producto>> {
        return try {
            val snapshot = firestore.collection("productos")
                .whereEqualTo("estado", "Disponible")
                .get()
                .await()

            val productos = snapshot.documents.mapNotNull {
                it.toObject(Producto::class.java)
            }.filter { producto ->
                producto.nombre.contains(query, ignoreCase = true) ||
                        producto.descripcion.contains(query, ignoreCase = true)
            }

            Result.success(productos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}