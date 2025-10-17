package com.unimarket.app.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimarket.app.data.model.Producto
import com.unimarket.app.data.model.UiState
import com.unimarket.app.data.repository.AuthRepository
import com.unimarket.app.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _userProducts = MutableStateFlow<UiState<List<Producto>>>(UiState.Idle)
    val userProducts: StateFlow<UiState<List<Producto>>> = _userProducts

    private val _createState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val createState: StateFlow<UiState<String>> = _createState

    fun loadUserProducts() {
        val userId = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            _userProducts.value = UiState.Loading
            val result = productRepository.getUserProducts(userId)
            _userProducts.value = if (result.isSuccess) {
                UiState.Success(result.getOrThrow())
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Error al cargar productos")
            }
        }
    }

    fun createProduct(producto: Producto, imageUris: List<Uri>) {
        viewModelScope.launch {
            _createState.value = UiState.Loading
            val result = productRepository.createProduct(producto, imageUris)
            _createState.value = if (result.isSuccess) {
                loadUserProducts()
                UiState.Success(result.getOrThrow())
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Error al crear producto")
            }
        }
    }

    fun updateProduct(productId: String, updates: Map<String, Any>) {
        viewModelScope.launch {
            val result = productRepository.updateProduct(productId, updates)
            if (result.isSuccess) {
                loadUserProducts()
            }
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            val result = productRepository.deleteProduct(productId)
            if (result.isSuccess) {
                loadUserProducts()
            }
        }
    }
}