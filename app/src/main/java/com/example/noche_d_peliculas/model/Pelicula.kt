package com.example.noche_d_peliculas.model

import com.google.gson.annotations.SerializedName

data class ActorRole(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("nombre") val nombre: String = "",
    @SerializedName("personaje") val personaje: String = ""
)

data class Pelicula(
    @SerializedName("_id") val id: String = "",
    @SerializedName("titulo") val titulo: String = "",
    @SerializedName("sinopsis") val sinopsis: String = "",
    @SerializedName("anio_lanzamiento") val anioLanzamiento: Int = 0,
    @SerializedName("generos") val generos: List<String> = emptyList(),
    @SerializedName("portada_url") val portadaUrl: String = "",
    @SerializedName("trailer_youtube_url") val trailerYoutubeUrl: String = "",
    @SerializedName("clasificacion_rtc") val clasificacionRtc: String = "",
    @SerializedName("duracion_minutos") val duracionMinutos: Int = 0,
    @SerializedName("calificacion_promedio") val calificacionPromedio: Double = 0.0,
    @SerializedName("director") val director: String = "",
    @SerializedName("elenco") val elenco: List<ActorRole> = emptyList()
)
