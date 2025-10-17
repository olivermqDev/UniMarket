package com.unimarket.app.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.unimarket.app.iu.screens.*
import com.unimarket.app.viewmodel.AuthViewModel
import org.koin.androidx.compose.koinViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Catalog : Screen("catalog")
    object Profile : Screen("profile")
    object EditProfile : Screen("edit_profile")
    object PublishProduct : Screen("publish_product")
    object MyProducts : Screen("my_products")
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = koinViewModel()
) {
    val startDestination = if (authViewModel.isLoggedIn) {
        Screen.Home.route
    } else {
        Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCatalog = { navController.navigate(Screen.Catalog.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                onNavigateToPublish = { navController.navigate(Screen.PublishProduct.route) },
                onNavigateToMyProducts = { navController.navigate(Screen.MyProducts.route) }
            )
        }

        composable(Screen.Catalog.route) {
            CatalogScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateToEdit = { navController.navigate(Screen.EditProfile.route) },
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.PublishProduct.route) {
            PublishProductScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.MyProducts.route) {
            MyProductsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}