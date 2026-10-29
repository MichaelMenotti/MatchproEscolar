package com.example.matchproescolar.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.matchproescolar.data.local.database.AppDatabase
import com.example.matchproescolar.data.model.TorneoEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MatchViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.obtenerBaseDeDatos(application).torneoDao()

    val torneos: StateFlow<List<TorneoEntity>> = dao.obtenerTorneos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun agregarTorneo(torneo: TorneoEntity) = viewModelScope.launch {
        dao.insertarTorneo(torneo)
    }

    fun actualizarTorneo(torneo: TorneoEntity) {
        viewModelScope.launch {
            dao.actualizarTorneo(torneo)
        }
    }

    fun eliminarTorneo(torneo: TorneoEntity) {
        viewModelScope.launch {
            dao.eliminarTorneo(torneo)
        }
    }
}