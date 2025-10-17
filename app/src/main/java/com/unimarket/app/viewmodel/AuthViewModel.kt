package com.unimarket.app.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimarket.app.data.model.UiState
import com.unimarket.app.data.model.User
import com.unimarket.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow<UiState<User>>(UiState.Idle)
    val authState: StateFlow<UiState<User>> = _authState

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    val isLoggedIn: Boolean get() = authRepository.currentUser != null

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = UiState.Loading
            val result = authRepository.signIn(email, password)
            _authState.value = if (result.isSuccess) {
                _currentUser.value = result.getOrNull()
                UiState.Success(result.getOrThrow())
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Error al iniciar sesión")
            }
        }
    }

    fun signUp(
        email: String,
        password: String,
        nombre: String,
        universidad: String,
        numeroCelular: String,
        ubicacion: String,
        fotoPerfilUri: Uri?
    ) {
        viewModelScope.launch {
            _authState.value = UiState.Loading
            val result = authRepository.signUp(
                email, password, nombre, universidad,
                numeroCelular, ubicacion, fotoPerfilUri
            )
            _authState.value = if (result.isSuccess) {
                _currentUser.value = result.getOrNull()
                UiState.Success(result.getOrThrow())
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Error al registrar")
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _currentUser.value = null
        _authState.value = UiState.Idle
    }

    fun resetAuthState() {
        _authState.value = UiState.Idle
    }
}