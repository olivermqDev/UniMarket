package com.unimarket.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimarket.app.data.model.Producto
import com.unimarket.app.data.model.UiState
import com.unimarket.app.data.model.User
import com.unimarket.app.data.repository.ProductRepository
import com.unimarket.app.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _featuredProducts = MutableStateFlow<UiState<List<Producto>>>(UiState.Idle)
    val featuredProducts: StateFlow<UiState<List<Producto>>> = _featuredProducts

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    fun loadFeaturedProducts() {
        viewModelScope.launch {
            _featuredProducts.value = UiState.Loading
            val result = productRepository.getProducts()
            _featuredProducts.value = if (result.isSuccess) {
                UiState.Success(result.getOrThrow().take(10))
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Error al cargar productos")
            }
        }
    }

    fun loadCurrentUser(userId: String) {
        viewModelScope.launch {
            val result = userRepository.getUser(userId)
            if (result.isSuccess) {
                _currentUser.value = result.getOrNull()
            }
        }
    }
}