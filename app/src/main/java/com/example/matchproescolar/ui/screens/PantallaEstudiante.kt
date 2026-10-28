package com.example.matchproescolar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.matchproescolar.data.model.TorneoEntity
import com.example.matchproescolar.viewmodel.MatchViewModel

enum class EstudianteTab(val titulo: String, val icon: ImageVector) {
    CALENDARIO("Torneos", Icons.Default.EmojiEvents),
    POSICIONES("Posiciones", Icons.Default.MilitaryTech),
    CUENTA("Mi Perfil", Icons.Default.AccountCircle)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEstudiante(
    onExit: () -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val viewModel: MatchViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as android.app.Application)
    )

    var selectedTab by remember { mutableStateOf(EstudianteTab.CALENDARIO) }
    val listaTorneos by viewModel.torneos.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                EstudianteTab.values().forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = { Icon(tab.icon, contentDescription = tab.titulo) },
                        label = { Text(tab.titulo, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SportGreen,
                            selectedTextColor = SportGreen,
                            indicatorColor = SportSoftGreen
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when (selectedTab) {
                EstudianteTab.CALENDARIO -> CalendarioEstudianteView(torneos = listaTorneos)
                EstudianteTab.POSICIONES -> PosicionesView()
                EstudianteTab.CUENTA -> CuentaEstudianteView(
                    nombre = "Mateo Gómez",
                    detalle = "Estudiante · Grado 11° A",
                    onExit = onExit
                )
            }
        }
    }
}

/* ---------- VISTA DE TORNEOS PARA EL ESTUDIANTE (Desde Room) ---------- */

@Composable
fun CalendarioEstudianteView(torneos: List<TorneoEntity>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Column {
                Text("Panel del Estudiante", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SportGreen)
                Text("TORNEOS ACTIVOS", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryNavy)
            }
        }

        // Listamos los torneos que el profesor guardó en la BD
        items(torneos) { torneo ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = SportSoftGreen,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                torneo.estado,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SportGreen,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = SportGreen)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(torneo.nombreDeporte, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PrimaryNavy)
                    Text("ID de Registro: ${torneo.idTorneo}", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        if (torneos.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No hay torneos activos disponibles.", fontWeight = FontWeight.Bold, color = Color.Gray)
                        Text("El profesor debe crear un torneo para que aparezca aquí.", fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

/* ---------- VISTA DE PERFIL DEL ESTUDIANTE ---------- */

@Composable
fun CuentaEstudianteView(nombre: String, detalle: String, onExit: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(56.dp), shape = CircleShape, color = SportSoftGreen) {
                Icon(Icons.Default.AccountCircle, contentDescription = null, tint = SportGreen, modifier = Modifier.padding(8.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(detalle, color = Color.Gray, fontSize = 13.sp)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SportSoftGreen)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Mi Equipo", fontWeight = FontWeight.Bold, color = SportGreen, fontSize = 12.sp)
                Text("11° A · Representante", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PrimaryNavy)
                Text("Sincronizado con base de datos local SQLite", fontSize = 12.sp, color = Color.Gray)
            }
        }

        OutlinedButton(
            onClick = onExit,
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("Cerrar Sesión")
        }
    }
}