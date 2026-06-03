package com.example.noche_d_peliculas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noche_d_peliculas.model.Pelicula
import com.example.noche_d_peliculas.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PeliculaViewModel : ViewModel() {

    private val _peliculas = MutableStateFlow<List<Pelicula>>(emptyList())
    val peliculas: StateFlow<List<Pelicula>> = _peliculas.asStateFlow()

    private val _peliculasFiltradas = MutableStateFlow<List<Pelicula>>(emptyList())
    val peliculasFiltradas: StateFlow<List<Pelicula>> = _peliculasFiltradas.asStateFlow()

    private val _peliculaSeleccionada = MutableStateFlow<Pelicula?>(null)
    val peliculaSeleccionada: StateFlow<Pelicula?> = _peliculaSeleccionada.asStateFlow()

    private val _generos = MutableStateFlow<List<String>>(emptyList())
    val generos: StateFlow<List<String>> = _generos.asStateFlow()

    private val _generoSeleccionado = MutableStateFlow<String?>(null)
    val generoSeleccionado: StateFlow<String?> = _generoSeleccionado.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    init {
        cargarPeliculas()
    }

    fun cargarPeliculas() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val resultado = RetrofitClient.apiService.getPeliculas()
                _peliculas.value = resultado
                _peliculasFiltradas.value = resultado
                extraerGeneros(resultado)
            } catch (e: Exception) {
                _error.value = "Error al cargar películas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun buscarPeliculas(titulo: String) {
        _searchQuery.value = titulo
        if (titulo.isBlank()) {
            _peliculasFiltradas.value = _peliculas.value
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val resultado = RetrofitClient.apiService.buscarPeliculas(titulo)
                _peliculasFiltradas.value = resultado
            } catch (e: Exception) {
                _error.value = "Error en la búsqueda: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun filtrarPorGenero(genero: String) {
        if (_generoSeleccionado.value == genero) {
            limpiarFiltro()
            return
        }
        _generoSeleccionado.value = genero
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val resultado = RetrofitClient.apiService.getPeliculasPorGenero(genero)
                _peliculasFiltradas.value = resultado
            } catch (e: Exception) {
                _error.value = "Error al filtrar: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun limpiarFiltro() {
        _generoSeleccionado.value = null
        _peliculasFiltradas.value = _peliculas.value
    }

    fun toggleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            _searchQuery.value = ""
            _peliculasFiltradas.value = _peliculas.value
        }
    }

    private fun extraerGeneros(peliculas: List<Pelicula>) {
        val todosGeneros = peliculas.flatMap { it.generos }.distinct().sorted()
        _generos.value = todosGeneros
    }

    fun seleccionarPelicula(id: String) {
        val pelicula = _peliculas.value.find { it.id == id }
        _peliculaSeleccionada.value = pelicula
        
        // Si no está en la lista actual, podríamos hacer fetch desde la API
        if (pelicula == null) {
            viewModelScope.launch {
                _isLoading.value = true
                try {
                    val p = RetrofitClient.apiService.getPeliculaById(id)
                    _peliculaSeleccionada.value = p
                } catch (e: Exception) {
                    _error.value = "Error al cargar el detalle: ${e.message}"
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun limpiarSeleccion() {
        _peliculaSeleccionada.value = null
    }
}
