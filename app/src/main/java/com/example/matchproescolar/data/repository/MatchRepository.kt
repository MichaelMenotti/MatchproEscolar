package com.example.matchproescolar.data.repository

import com.example.matchproescolar.data.local.dao.TorneoDao
import com.example.matchproescolar.data.model.TorneoEntity
import kotlinx.coroutines.flow.Flow

class MatchRepository(private val torneoDao: TorneoDao) {
    val torneos: Flow<List<TorneoEntity>> = torneoDao.obtenerTorneos()

    suspend fun insertar(torneo: TorneoEntity) {
        torneoDao.insertarTorneo(torneo)
    }

    suspend fun actualizar(torneo: TorneoEntity) {
        torneoDao.actualizarTorneo(torneo)
    }

    suspend fun eliminar(torneo: TorneoEntity) {
        torneoDao.eliminarTorneo(torneo)
    }
}