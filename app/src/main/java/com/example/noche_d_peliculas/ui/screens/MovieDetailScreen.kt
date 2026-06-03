package com.example.noche_d_peliculas.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.example.noche_d_peliculas.model.Pelicula
import com.example.noche_d_peliculas.ui.theme.NetflixBlack
import com.example.noche_d_peliculas.ui.theme.NetflixLightGray
import com.example.noche_d_peliculas.ui.theme.NetflixMediumGray
import com.example.noche_d_peliculas.ui.theme.NetflixRed
import com.example.noche_d_peliculas.ui.theme.NetflixWhite
import com.example.noche_d_peliculas.ui.theme.StarYellow
import com.example.noche_d_peliculas.viewmodel.PeliculaViewModel

@Composable
fun MovieDetailScreen(
    viewModel: PeliculaViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pelicula by viewModel.peliculaSeleccionada.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NetflixBlack)
    ) {
        when {
            isLoading && pelicula == null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = NetflixRed)
                }
            }
            error != null && pelicula == null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = error ?: "Error", color = NetflixWhite)
                }
            }
            pelicula != null -> {
                MovieDetailContent(
                    pelicula = pelicula!!,
                    onBackClick = onBackClick
                )
            }
        }
    }
}

@Composable
fun MovieDetailContent(
    pelicula: Pelicula,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    // Initialize ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(pelicula.trailerYoutubeUrl))
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    // Release ExoPlayer when the composable leaves the composition
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Back Button & Title Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Regresar",
                    tint = NetflixWhite
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = pelicula.titulo,
                color = NetflixWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 34.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Metadata: Year, Classification, Duration, Rating
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "${pelicula.anioLanzamiento}",
                color = NetflixLightGray,
                fontSize = 14.sp
            )
            Text(
                text = pelicula.clasificacionRtc,
                color = NetflixWhite,
                fontSize = 12.sp,
                modifier = Modifier
                    .background(NetflixMediumGray, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
            Text(
                text = "${pelicula.duracionMinutos} min",
                color = NetflixLightGray,
                fontSize = 14.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = StarYellow,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${pelicula.calificacionPromedio}",
                    color = StarYellow,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Video Player (ExoPlayer)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black)
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = true
                        setShowNextButton(false)
                        setShowPreviousButton(false)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Poster and Synopsis
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // Poster
            AsyncImage(
                model = pelicula.portadaUrl,
                contentDescription = "Portada",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(100.dp)
                    .aspectRatio(0.67f)
                    .clip(RoundedCornerShape(8.dp))
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Synopsis & Director
            Column {
                Text(
                    text = pelicula.sinopsis,
                    color = NetflixWhite,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Director: ${pelicula.director}",
                    color = NetflixLightGray,
                    fontSize = 13.sp
                )
                if (pelicula.elenco.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Elenco: ${pelicula.elenco.joinToString { it.nombre }}",
                        color = NetflixLightGray,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Categories (Bottom)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            pelicula.generos.forEach { genero ->
                Text(
                    text = genero,
                    color = NetflixWhite,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .background(NetflixMediumGray, RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}
