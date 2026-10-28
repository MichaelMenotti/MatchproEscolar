package com.example.matchproescolar.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.matchproescolar.ui.screens.SplashScreen
import com.example.matchproescolar.ui.screens.PantallaLogin
import com.example.matchproescolar.ui.screens.PantallaEstudiante
import com.example.matchproescolar.ui.screens.PantallaProfesor
import com.example.matchproescolar.ui.screens.Role

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {

        // 1. Ruta de SplashScreen con lectura de SharedPreferences
        composable("splash") {
            SplashScreen { destino ->
                navController.navigate(destino) {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }

        // 2. Ruta de Login
        composable("login") {
            PantallaLogin { role ->
                if (role == Role.ADMIN) {
                    navController.navigate("profesor")
                } else {
                    navController.navigate("estudiante")
                }
            }
        }

        // 3. Ruta de Estudiante
        composable("estudiante") {
            PantallaEstudiante(onExit = {
                navController.popBackStack("login", inclusive = false)
            })
        }

        // 4. Ruta de Profesor
        composable("profesor") {
            PantallaProfesor(onExit = {
                navController.popBackStack("login", inclusive = false)
            })
        }
    }
}