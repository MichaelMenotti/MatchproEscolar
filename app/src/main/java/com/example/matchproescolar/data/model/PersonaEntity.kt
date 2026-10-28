package com.example.matchproescolar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "torneos")
data class TorneoEntity(
    @PrimaryKey val idTorneo: String,
    val nombreDeporte: String,
    val estado: String
)