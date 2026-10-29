package com.example.matchproescolar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

val SportGreen = Color(0xFF10B981)
val SportSoftGreen = Color(0xFFE8F5E9)
val PrimaryNavy = Color(0xFF0A192F)

enum class ProfesorTab(val titulo: String, val icon: ImageVector) {
    TORNEOS("Torneos", Icons.Default.EmojiEvents),
    CALENDARIO("Calendario", Icons.Default.DateRange),
    POSICIONES("Posiciones", Icons.Default.MilitaryTech),
    CUENTA("Cuenta", Icons.Default.AccountCircle)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaProfesor(
    onExit: () -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val viewModel: MatchViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as android.app.Application)
    )

    var selectedTab by remember { mutableStateOf(ProfesorTab.TORNEOS) }
    var createOpen by remember { mutableStateOf(false) }
    val listaTorneos by viewModel.torneos.collectAsState()

    Scaffold(
        bottomBar = {
            if (!createOpen) {
                NavigationBar(containerColor = Color.White) {
                    ProfesorTab.values().forEach { tab ->
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
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (createOpen) {
                CrearTorneoView(
                    viewModel = viewModel,
                    onBack = { createOpen = false }
                )
            } else {
                when (selectedTab) {
                    ProfesorTab.TORNEOS -> PanelTorneosView(
                        torneos = listaTorneos,
                        onCreateClick = { createOpen = true },
                        viewModel = viewModel
                    )
                    ProfesorTab.CALENDARIO -> FixtureView()
                    ProfesorTab.POSICIONES -> PosicionesView()
                    ProfesorTab.CUENTA -> CuentaView(
                        nombre = "Diana Restrepo",
                        detalle = "Docente de Educación Física",
                        onExit = onExit
                    )
                }
            }
        }
    }
}
@Composable
fun PanelTorneosView(
    torneos: List<TorneoEntity>,
    onCreateClick: () -> Unit,
    viewModel: MatchViewModel
) {
    var torneoSeleccionadoParaVer by remember { mutableStateOf<TorneoEntity?>(null) }
    var torneoEnEdicion by remember { mutableStateOf<TorneoEntity?>(null) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Column {
                Text("Buenas tardes, profe Diana", fontSize = 13.sp, color = Color.Gray)
                Text("TODO LISTO PARA JUGAR", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryNavy)
            }
        }

        item {
            Button(
                onClick = onCreateClick,
                colors = ButtonDefaults.buttonColors(containerColor = SportGreen),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Crear nuevo torneo", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        items(torneos) { torneo ->
            Card(
                colors = CardDefaults.cardColors(containerColor = PrimaryNavy),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { torneoSeleccionadoParaVer = torneo }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                color = SportGreen,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    torneo.estado,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(torneo.nombreDeporte, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("ID: ${torneo.idTorneo} · Categoría Escolar", fontSize = 12.sp, color = Color.LightGray)
                            Text(
                                text = "${torneo.numeroEquipos} equipos inscritos",
                                fontSize = 12.sp,
                                color = Color.LightGray
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IconButton(onClick = { torneoEnEdicion = torneo }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar torneo",
                                    tint = Color.White
                                )
                            }

                            IconButton(onClick = { viewModel.eliminarTorneo(torneo) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar torneo",
                                    tint = Color(0xFFFF6B6B)
                                )
                            }
                        }
                    }
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
                        Text("No hay torneos creados aún.", fontWeight = FontWeight.Bold, color = Color.Gray)
                        Text("Haz clic arriba en 'Crear nuevo torneo' para agregar uno a la base de datos.", fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }

    torneoEnEdicion?.let { torneoActual ->
        EditarTorneoDialog(
            torneo = torneoActual,
            onDismiss = { torneoEnEdicion = null },
            onUpdate = { torneoModificado ->
                viewModel.actualizarTorneo(torneoModificado)
                torneoEnEdicion = null
            }
        )
    }
    torneoSeleccionadoParaVer?.let { torneo ->
        DetalleEquiposDialog(
            torneo = torneo,
            onDismiss = { torneoSeleccionadoParaVer = null }
        )
    }
}
@Composable
fun CrearTorneoView(viewModel: MatchViewModel, onBack: () -> Unit) {
    var nombreTorneo by remember { mutableStateOf("") }
    var categoriaTorneo by remember { mutableStateOf("") }
    var guardadoExitoso by remember { mutableStateOf(false) }
    var numeroEquiposText by remember { mutableStateOf("") }
    var nombresEquiposText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Volver")
        }

        Text("NUEVO TORNEO", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryNavy)

        if (guardadoExitoso) {
            Card(
                colors = CardDefaults.cardColors(containerColor = SportSoftGreen),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SportGreen, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("¡Torneo guardado en Room!", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("El torneo se registró correctamente en la base de datos local.", fontSize = 13.sp, color = Color.Gray, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)) {
                        Text("Ver en el panel")
                    }
                }
            }
        } else {
            OutlinedTextField(
                value = nombreTorneo,
                onValueChange = { nombreTorneo = it },
                label = { Text("Nombre del torneo (ej. Copa Intercursos)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = categoriaTorneo,
                onValueChange = { categoriaTorneo = it },
                label = { Text("Categoría (ej. Secundaria · 8 equipos)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = numeroEquiposText,
                onValueChange = { numeroEquiposText = it },
                label = { Text("Número de equipos (ej. 8)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = nombresEquiposText,
                onValueChange = { nombresEquiposText = it },
                label = { Text("Nombres de equipos (separados por coma)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (nombreTorneo.isNotBlank() && categoriaTorneo.isNotBlank()) {
                        val idUnico = System.currentTimeMillis().toString()
                        val numEquipos = numeroEquiposText.toIntOrNull() ?: 0

                        val nuevoTorneo = TorneoEntity(
                            idTorneo = idUnico,
                            nombreDeporte = nombreTorneo,
                            estado = categoriaTorneo,
                            numeroEquipos = numEquipos,
                            nombresEquipos = nombresEquiposText
                        )

                        viewModel.agregarTorneo(nuevoTorneo)
                        guardadoExitoso = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SportGreen),
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Crear torneo", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

/* ---------- CARTA CLICKEABLE PARA VER DETALLES DEL TORNEO ---------- */

@Composable
fun DetalleEquiposDialog(
    torneo: TorneoEntity,
    onDismiss: () -> Unit
) {
    val listaNombres = torneo.nombresEquipos.split(",").map { it.trim() }.filter { it.isNotEmpty() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(torneo.nombreDeporte, fontWeight = FontWeight.Bold, color = PrimaryNavy, fontSize = 20.sp)
                Text("Participantes (${torneo.numeroEquipos} equipos)", fontSize = 13.sp, color = Color.Gray)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (listaNombres.isEmpty()) {
                    Text("No se especificaron nombres de equipos para este torneo.", color = Color.Gray, fontSize = 14.sp)
                } else {
                    listaNombres.forEachIndexed { index, nombre ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SportSoftGreen),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${index + 1}.",
                                    fontWeight = FontWeight.Bold,
                                    color = SportGreen,
                                    modifier = Modifier.width(24.dp)
                                )
                                Text(
                                    text = nombre,
                                    fontWeight = FontWeight.Medium,
                                    color = PrimaryNavy,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
            ) {
                Text("Cerrar", color = Color.White)
            }
        }
    )
}


/* ---------- CALENDARIO Y POSICIONES ---------- */

@Composable
fun FixtureView() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("CALENDARIO DE PARTIDOS", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PrimaryNavy)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("🔴 EN VIVO", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("Fútbol", color = Color.Gray, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("11° A", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("2 - 1", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text("11° B", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun PosicionesView() {
    val equipos = listOf(
        Triple("11° A", "9", "+7"),
        Triple("10° C", "7", "+4"),
        Triple("11° B", "6", "+2")
    )
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("TABLA DE POSICIONES", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PrimaryNavy)
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().background(PrimaryNavy).padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("#  Equipo", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        Text("DG", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("PTS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
                equipos.forEachIndexed { i, eq ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${i + 1}.  ${eq.first}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(28.dp)) {
                            Text(eq.third, fontSize = 14.sp, color = Color.Gray)
                            Text(eq.second, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CuentaView(nombre: String, detalle: String, onExit: () -> Unit) {
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
        OutlinedButton(
            onClick = onExit,
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("Cerrar Sesión")
        }
    }
}
@Composable
fun EditarTorneoDialog(
    torneo: TorneoEntity,
    onDismiss: () -> Unit,
    onUpdate: (TorneoEntity) -> Unit
) {
    var nuevoNombre by remember { mutableStateOf(torneo.nombreDeporte) }
    var nuevaCategoria by remember { mutableStateOf(torneo.estado) }
    var nuevoNumeroEquipos by remember { mutableStateOf(torneo.numeroEquipos.toString()) }
    var nuevosNombresEquipos by remember { mutableStateOf(torneo.nombresEquipos) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Torneo", fontWeight = FontWeight.Bold, color = PrimaryNavy) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = nuevoNombre,
                    onValueChange = { nuevoNombre = it },
                    label = { Text("Nombre del deporte/torneo") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = nuevaCategoria,
                    onValueChange = { nuevaCategoria = it },
                    label = { Text("Categoría / Estado") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = nuevoNumeroEquipos,
                    onValueChange = { nuevoNumeroEquipos = it },
                    label = { Text("Número de equipos") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = nuevosNombresEquipos,
                    onValueChange = { nuevosNombresEquipos = it },
                    label = { Text("Nombres de equipos") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nuevoNombre.isNotBlank() && nuevaCategoria.isNotBlank()) {
                        val torneoActualizado = torneo.copy(
                            nombreDeporte = nuevoNombre,
                            estado = nuevaCategoria,
                            numeroEquipos = nuevoNumeroEquipos.toIntOrNull() ?: 0,
                            nombresEquipos = nuevosNombresEquipos
                        )
                        onUpdate(torneoActualizado)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SportGreen)
            ) {
                Text("Actualizar", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
            }
        }
    )
}