package com.example.matchproescolar.data.network

import com.example.matchproescolar.data.model.RespuestaEquiposApi
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/v1/json/123/search_all_teams.php")
    suspend fun buscarEquipos(@Query("l") liga: String = "Spanish La Liga"): RespuestaEquiposApi
}