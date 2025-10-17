package com.unimarket.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimarket.app.data.model.Producto
import com.unimarket.app.data.model.UiState
import com.unimarket.app.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatalogViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<UiState<List<Producto>>>(UiState.Idle)
    val products: StateFlow<UiState<List<Producto>>> = _products

    private val _searchResults = MutableStateFlow<UiState<List<Producto>>>(UiState.Idle)
    val searchResults: StateFlow<UiState<List<Producto>>> = _searchResults

    fun loadProducts(
        categoria: String? = null,
        precioMin: Double? = null,
        precioMax: Double? = null,
        ordenarPor: String = "fechaPublicacion"
    ) {
        viewModelScope.launch {
            _products.value = UiState.Loading
            val result = productRepository.getProducts(categoria, precioMin, precioMax, ordenarPor)
            _products.value = if (result.isSuccess) {
                UiState.Success(result.getOrThrow())
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Error al cargar productos")
            }
        }
    }

    fun searchProducts(query: String) {
        viewModelScope.launch {
            _searchResults.value = UiState.Loading
            val result = productRepository.searchProducts(query)
            _searchResults.value = if (result.isSuccess) {
                UiState.Success(result.getOrThrow())
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Error en búsqueda")
            }
        }
    }

    fun resetSearch() {
        _searchResults.value = UiState.Idle
    }
}