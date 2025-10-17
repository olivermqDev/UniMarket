package com.unimarket.app

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.unimarket.app.data.repository.AuthRepository
import com.unimarket.app.data.repository.ProductRepository
import com.unimarket.app.data.repository.UserRepository
import com.unimarket.app.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Firebase instances
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }

    // Repositories
    single { AuthRepository(get(), get(), get()) }
    single { UserRepository(get(), get()) }
    single { ProductRepository(get(), get()) }

    // ViewModels
    viewModel { AuthViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { ProductViewModel(get(), get()) }
    viewModel { CatalogViewModel(get()) }
}