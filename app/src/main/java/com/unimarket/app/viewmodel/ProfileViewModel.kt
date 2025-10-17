package com.unimarket.app.viewmodel


import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimarket.app.data.model.UiState
import com.unimarket.app.data.model.User
import com.unimarket.app.data.repository.AuthRepository
import com.unimarket.app.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _userState = MutableStateFlow<UiState<User>>(UiState.Idle)
    val userState: StateFlow<UiState<User>> = _userState

    private val _updateState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val updateState: StateFlow<UiState<Unit>> = _updateState

    fun loadUser() {
        val userId = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            _userState.value = UiState.Loading
            val result = userRepository.getUser(userId)
            _userState.value = if (result.isSuccess) {
                UiState.Success(result.getOrThrow())
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Error al cargar perfil")
            }
        }
    }

    fun updateProfile(updates: Map<String, Any>) {
        val userId = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            _updateState.value = UiState.Loading
            val result = userRepository.updateUser(userId, updates)
            _updateState.value = if (result.isSuccess) {
                loadUser()
                UiState.Success(Unit)
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Error al actualizar")
            }
        }
    }

    fun updateProfilePhoto(photoUri: Uri) {
        val userId = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            _updateState.value = UiState.Loading
            val result = userRepository.updateProfilePhoto(userId, photoUri)
            _updateState.value = if (result.isSuccess) {
                loadUser()
                UiState.Success(Unit)
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Error al actualizar foto")
            }
        }
    }
}
