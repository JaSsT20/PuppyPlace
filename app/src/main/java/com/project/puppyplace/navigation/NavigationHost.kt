package com.project.puppyplace.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.puppyplace.ui.LoadingScreen.LoadingScreen
import com.project.puppyplace.ui.login.LoginScreen
import com.project.puppyplace.ui.signup.SignUpScreen

@Composable
fun NavigationHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destination.loading.route
    ){
        composable(Destination.loading.route){
            LoadingScreen(navController = navController)
        }
        composable(Destination.login.route){
            LoginScreen(navController = navController)
        }
        composable(Destination.signUp.route){
            SignUpScreen(navController = navController)
        }
    }
}