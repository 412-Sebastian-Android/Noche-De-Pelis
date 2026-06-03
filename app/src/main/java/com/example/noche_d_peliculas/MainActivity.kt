package com.example.noche_d_peliculas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.noche_d_peliculas.ui.components.TopBar
import com.example.noche_d_peliculas.ui.screens.HomeScreen
import com.example.noche_d_peliculas.ui.screens.MovieDetailScreen
import com.example.noche_d_peliculas.ui.theme.NetflixBlack
import com.example.noche_d_peliculas.ui.theme.Noche_D_peliculasTheme
import com.example.noche_d_peliculas.viewmodel.PeliculaViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: PeliculaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Noche_D_peliculasTheme {
                val navController = rememberNavController()
                val searchQuery by viewModel.searchQuery.collectAsState()
                val isSearching by viewModel.isSearching.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = NetflixBlack,
                    topBar = {
                        TopBar(
                            isSearching = isSearching,
                            searchQuery = searchQuery,
                            onSearchClick = { viewModel.toggleSearch() },
                            onSearchQueryChange = { viewModel.buscarPeliculas(it) }
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier
                            .padding(innerPadding)
                            .background(NetflixBlack)
                    ) {
                        composable("home") {
                            HomeScreen(
                                viewModel = viewModel,
                                onMovieClick = { id ->
                                    viewModel.seleccionarPelicula(id)
                                    navController.navigate("detail/$id")
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        composable(
                            route = "detail/{peliculaId}",
                            arguments = listOf(navArgument("peliculaId") { type = NavType.StringType })
                        ) {
                            MovieDetailScreen(
                                viewModel = viewModel,
                                onBackClick = {
                                    viewModel.limpiarSeleccion()
                                    navController.popBackStack()
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}