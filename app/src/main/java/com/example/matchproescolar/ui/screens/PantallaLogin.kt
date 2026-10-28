package com.example.matchproescolar.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class Role {
    ADMIN, STUDENT
}

@Composable
fun PantallaLogin(
    onRoleSelected: (Role) -> Unit
) {
    val context = LocalContext.current

    fun seleccionarRol(role: Role) {
        // Guardamos el rol en SharedPreferences para la sesión
        val preferences = context.getSharedPreferences("sesion_matchpro", Context.MODE_PRIVATE)
        val rolString = if (role == Role.ADMIN) "ADMIN" else "STUDENT"
        preferences.edit().putString("user_rol", rolString).apply()

        // Continuamos la navegación
        onRoleSelected(role)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A192F)), // Azul marino deportivo
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icono Trofeo Verde
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color(0xFF10B981), shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Logo",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "TU TORNEO. TU EQUIPO.",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF10B981),
                    letterSpacing = 1.5.sp
                )

                Text(
                    text = "MATCHPRO ESCOLAR",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0A192F)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "¿Cómo quieres ingresar?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0A192F)
                )

                Text(
                    text = "Selecciona tu perfil para continuar.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Botón Docente
                OptionRoleCard(
                    title = "Soy docente",
                    subtitle = "Administrar torneos y resultados",
                    onClick = { seleccionarRol(Role.ADMIN) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Botón Estudiante
                OptionRoleCard(
                    title = "Soy estudiante",
                    subtitle = "Consultar partidos y posiciones",
                    onClick = { seleccionarRol(Role.STUDENT) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Institución Educativa San Martín",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun OptionRoleCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF3F4F6), shape = RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE5E7EB), shape = RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0A192F)
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF10B981)
            )
        }
    }
}