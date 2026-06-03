package com.example.noche_d_peliculas.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noche_d_peliculas.ui.components.CategoryChips
import com.example.noche_d_peliculas.ui.components.MovieCard
import com.example.noche_d_peliculas.ui.components.MovieCarousel
import com.example.noche_d_peliculas.ui.theme.NetflixBlack
import com.example.noche_d_peliculas.ui.theme.NetflixLightGray
import com.example.noche_d_peliculas.ui.theme.NetflixRed
import com.example.noche_d_peliculas.ui.theme.NetflixWhite
import com.example.noche_d_peliculas.viewmodel.PeliculaViewModel

@Composable
fun HomeScreen(
    viewModel: PeliculaViewModel,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val peliculas by viewModel.peliculas.collectAsState()
    val peliculasFiltradas by viewModel.peliculasFiltradas.collectAsState()
    val generos by viewModel.generos.collectAsState()
    val generoSeleccionado by viewModel.generoSeleccionado.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NetflixBlack)
    ) {
        when {
            isLoading && peliculas.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = NetflixRed, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Cargando películas...", color = NetflixLightGray, fontSize = 14.sp)
                    }
                }
            }
            error != null && peliculas.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "😕", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = error ?: "Error desconocido", color = NetflixLightGray, fontSize = 14.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 32.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        TextButton(onClick = { viewModel.cargarPeliculas() }) {
                            Text("Reintentar", color = NetflixRed)
                        }
                    }
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item(span = { GridItemSpan(2) }) {
                        MovieCarousel(
                            peliculas = peliculas,
                            onMovieClick = onMovieClick,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item(span = { GridItemSpan(2) }) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    item(span = { GridItemSpan(2) }) {
                        CategoryChips(
                            generos = generos,
                            generoSeleccionado = generoSeleccionado,
                            onGeneroClick = { viewModel.filtrarPorGenero(it) },
                            onTodosClick = { viewModel.limpiarFiltro() }
                        )
                    }

                    item(span = { GridItemSpan(2) }) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "▎", color = NetflixRed, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Text(text = generoSeleccionado ?: "Destacadas", color = NetflixWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp))
                            Text(text = "  (${peliculasFiltradas.size})", color = NetflixLightGray, fontSize = 14.sp)
                        }
                    }

                    items(items = peliculasFiltradas, key = { it.id.ifEmpty { it.titulo } }) { pelicula ->
                        MovieCard(
                            pelicula = pelicula,
                            onClick = { onMovieClick(pelicula.id) },
                            modifier = Modifier.padding(
                                start = if (peliculasFiltradas.indexOf(pelicula) % 2 == 0) 16.dp else 0.dp,
                                end = if (peliculasFiltradas.indexOf(pelicula) % 2 != 0) 16.dp else 0.dp
                            )
                        )
                    }
                }
            }
        }
    }
}
