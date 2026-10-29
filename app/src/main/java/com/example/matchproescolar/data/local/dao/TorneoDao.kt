package com.example.matchproescolar.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.matchproescolar.data.model.TorneoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TorneoDao {
    @Query("SELECT * FROM torneos")
    fun obtenerTorneos(): Flow<List<TorneoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTorneo(torneo: TorneoEntity)

    @Update
    suspend fun actualizarTorneo(torneo: TorneoEntity)

    @Delete
    suspend fun eliminarTorneo(torneo: TorneoEntity)
}