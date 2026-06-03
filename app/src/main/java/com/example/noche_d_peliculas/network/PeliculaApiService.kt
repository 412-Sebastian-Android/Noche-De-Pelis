package com.example.noche_d_peliculas.network

import com.example.noche_d_peliculas.model.Pelicula
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PeliculaApiService {

    @GET("api/peliculas")
    suspend fun getPeliculas(): List<Pelicula>

    @GET("api/peliculas/buscar")
    suspend fun buscarPeliculas(@Query("titulo") titulo: String): List<Pelicula>

    @GET("api/peliculas/genero")
    suspend fun getPeliculasPorGenero(@Query("tipo") tipo: String): List<Pelicula>

    @GET("api/peliculas/{id}")
    suspend fun getPeliculaById(@Path("id") id: String): Pelicula
}
