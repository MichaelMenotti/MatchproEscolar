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
import com.example.matchproescolar.data.model.PartidoRemotoModel
import com.example.matchproescolar.data.model.aPartidoRemoto
import com.example.matchproescolar.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow

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

    private val _cargandoApi = MutableStateFlow(false)
    val cargandoApi: StateFlow<Boolean> = _cargandoApi

    private val _partidosRemotos = MutableStateFlow<List<PartidoRemotoModel>>(emptyList())
    val partidosRemotos: StateFlow<List<PartidoRemotoModel>> = _partidosRemotos
    private val _errorApi = MutableStateFlow<String?>(null)
    val errorApi: StateFlow<String?> = _errorApi

    fun cargarPartidosDeApi() {
        viewModelScope.launch {
            _cargandoApi.value = true
            _errorApi.value = null
            try {
                val equipos = RetrofitClient.apiService.buscarEquipos("Spanish La Liga").teams.orEmpty()
                _partidosRemotos.value = equipos.take(10).mapIndexed { index, item -> item.aPartidoRemoto(index) }
            } catch (e: Exception) {
                android.util.Log.e("API_MATCHPRO", "Fallo al consultar API", e)
                _errorApi.value = "Error: ${e.message ?: "fallo de red"}"
                _partidosRemotos.value = emptyList()
            } finally {
                _cargandoApi.value = false
            }
        }
    }
}