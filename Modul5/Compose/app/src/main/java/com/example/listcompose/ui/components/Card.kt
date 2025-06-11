package com.example.listcompose.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.listcompose.R
import com.example.listcompose.data.model.Movie

@Composable
fun MovieCard(
    movie: Movie,
    onDetailClick: () -> Unit,
    onSaveClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Image(
                painter = rememberAsyncImagePainter(
                    model = "https://image.tmdb.org/t/p/w500${movie.posterPath}"
                ),
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleLarge
            )

            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = stringResource(R.string.rating),
                    tint = Color(0xFFFFD700)
                )
                Text(
                    text = " ${movie.voteAverage}/10",
                    modifier = Modifier.padding(start = 4.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                    contentDescription = stringResource(R.string.popularity),
                    tint = Color(0xFF4CAF50)
                )
                Text(
                    text = " ${movie.popularity.toInt()}",
                    modifier = Modifier.padding(start = 4.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(text = movie.releaseDate)
            }

            Text(
                text = movie.overview.take(100) + "...",
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                onClick = onDetailClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(stringResource(R.string.view_details_button))
            }

            if (onSaveClick != null) {
                Button(
                    onClick = onSaveClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(stringResource(R.string.save_to_local_button))
                }
            }
        }
    }
}