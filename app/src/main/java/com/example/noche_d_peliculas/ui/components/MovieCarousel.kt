package com.example.noche_d_peliculas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.noche_d_peliculas.model.Pelicula
import com.example.noche_d_peliculas.ui.theme.NetflixBlack
import com.example.noche_d_peliculas.ui.theme.NetflixLightGray
import com.example.noche_d_peliculas.ui.theme.NetflixMediumGray
import com.example.noche_d_peliculas.ui.theme.NetflixRed
import com.example.noche_d_peliculas.ui.theme.NetflixWhite
import com.example.noche_d_peliculas.ui.theme.StarYellow
import kotlinx.coroutines.delay

@Composable
fun MovieCarousel(
    peliculas: List<Pelicula>,
    onMovieClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (peliculas.isEmpty()) return

    val carouselItems = peliculas.take(5)
    val pagerState = rememberPagerState(pageCount = { carouselItems.size })

    // Auto-scroll effect
    LaunchedEffect(pagerState) {
        while (true) {
            delay(5000)
            val nextPage = (pagerState.currentPage + 1) % carouselItems.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val pelicula = carouselItems[page]
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onMovieClick(pelicula.id) }
                ) {
                    // Poster image
                    AsyncImage(
                        model = pelicula.portadaUrl,
                        contentDescription = pelicula.titulo,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Bottom gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        NetflixBlack.copy(alpha = 0.3f),
                                        NetflixBlack.copy(alpha = 0.95f)
                                    ),
                                    startY = 100f,
                                    endY = Float.POSITIVE_INFINITY
                                )
                            )
                    )

                    // Movie info overlay
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(horizontal = 20.dp, vertical = 20.dp)
                    ) {
                        Text(
                            text = pelicula.titulo,
                            color = NetflixWhite,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "${pelicula.anioLanzamiento}",
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
                            Text(
                                text = pelicula.clasificacionRtc,
                                color = NetflixWhite,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .background(
                                        NetflixMediumGray.copy(alpha = 0.8f),
                                        RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = pelicula.generos.joinToString(" • "),
                            color = NetflixLightGray,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        // Page indicator dots
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(carouselItems.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (pagerState.currentPage == index) 10.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (pagerState.currentPage == index) NetflixRed
                            else NetflixMediumGray
                        )
                )
            }
        }
    }
}
