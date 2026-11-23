package com.example.matchproescolar.data.model

import com.google.gson.annotations.SerializedName
import kotlin.math.abs

data class RespuestaEquiposApi(
    @SerializedName("teams") val teams: List<EquipoApiItem>? = null
)

data class EquipoApiItem(
    @SerializedName("idTeam") val idTeam: String = "0",
    @SerializedName("strTeam") val strTeam: String = "Equipo",
    @SerializedName("strLeague") val strLeague: String? = null
)

data class PartidoRemotoModel(
    val id: Int,
    val equipoLocal: String,
    val equipoVisita: String,
    val estado: String,
    val golesLocal: Int,
    val golesVisita: Int
)

private val rivalesEscolares = listOf(
    "Grado 11° A",
    "Grado 10° B",
    "Selección Escolar",
    "Deportivo Central",
    "Atlético Intercursos"
)

fun EquipoApiItem.aPartidoRemoto(indice: Int): PartidoRemotoModel {
    val numeroId = idTeam.toIntOrNull() ?: (indice + 1)
    val finalizado = indice % 2 == 0
    return PartidoRemotoModel(
        id = numeroId,
        equipoLocal = strTeam.ifBlank { "Equipo Local" },
        equipoVisita = rivalesEscolares[abs(numeroId) % rivalesEscolares.size],
        estado = if (finalizado) "FINALIZADO" else "PROGRAMADO",
        golesLocal = abs(numeroId * 2) % 4,
        golesVisita = abs(numeroId + 1) % 4
    )
}