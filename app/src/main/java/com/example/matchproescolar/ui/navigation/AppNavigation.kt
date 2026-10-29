package com.example.matchproescolar.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current

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
                    navController.navigate("profesor") {
                        popUpTo("login") { inclusive = true }
                    }
                } else {
                    navController.navigate("estudiante") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        }

        // 3. Ruta de Estudiante
        composable("estudiante") {
            PantallaEstudiante(onExit = {
                val sharedPreferences = context.getSharedPreferences("sesion_matchpro", Context.MODE_PRIVATE)
                sharedPreferences.edit().clear().apply()

                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            })
        }

        // 4. Ruta de Profesor
        composable("profesor") {
            PantallaProfesor(
                onExit = {
                    val sharedPreferences = context.getSharedPreferences("sesion_matchpro", Context.MODE_PRIVATE)
                    sharedPreferences.edit().clear().apply()

                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}