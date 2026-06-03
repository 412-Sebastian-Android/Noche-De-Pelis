package com.example.noche_d_peliculas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noche_d_peliculas.ui.theme.NetflixLightGray
import com.example.noche_d_peliculas.ui.theme.NetflixMediumGray
import com.example.noche_d_peliculas.ui.theme.NetflixRed
import com.example.noche_d_peliculas.ui.theme.NetflixWhite

@Composable
fun CategoryChips(
    generos: List<String>,
    generoSeleccionado: String?,
    onGeneroClick: (String) -> Unit,
    onTodosClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // "Todas" chip
        item {
            val isSelected = generoSeleccionado == null
            CategoryChip(
                label = "Todas",
                isSelected = isSelected,
                onClick = onTodosClick
            )
        }
        // Genre chips
        items(generos) { genero ->
            val isSelected = generoSeleccionado == genero
            CategoryChip(
                label = genero,
                isSelected = isSelected,
                onClick = { onGeneroClick(genero) }
            )
        }
    }
}

@Composable
private fun CategoryChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    Box(
        modifier = Modifier
            .clip(shape)
            .then(
                if (isSelected) {
                    Modifier.background(NetflixRed, shape)
                } else {
                    Modifier.border(1.dp, NetflixMediumGray, shape)
                }
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) NetflixWhite else NetflixLightGray,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
