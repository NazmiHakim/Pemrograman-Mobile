package com.example.listcompose

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import timber.log.Timber

@Composable
fun ItemListScreen(navController: NavController, viewModel: ItemViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = ItemViewModelFactory(LocalContext.current, "List Loaded"))) {
    val context = LocalContext.current
    val itemList by viewModel.itemList.collectAsState()

    LazyColumn(contentPadding = PaddingValues(8.dp)) {
        items(itemList.size) { index ->
            val item = itemList[index]
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Image(
                        painter = painterResource(id = item.imageResId),
                        contentDescription = item.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = item.title, style = MaterialTheme.typography.titleLarge, color = Color.White)
                    Text(text = item.author, style = MaterialTheme.typography.bodyMedium, color = Color.White)
                    Text(text = item.info1, style = MaterialTheme.typography.bodySmall, color = Color.White)
                    Text(text = item.info2, style = MaterialTheme.typography.bodySmall, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, item.url.toUri())
                            Timber.d("Explicit intent to URL: ${item.url}")
                            context.startActivity(intent)
                        }) {
                            Text(text = stringResource(R.string.manhwa_website))
                        }
                        Button(onClick = {
                            navController.navigate("detail/${item.id}")
                            Timber.d("Navigated to Detail for item id: ${item.id}")
                        }) {
                            Text(text = stringResource(R.string.manhwa_detail))
                        }
                    }
                }
            }
        }
    }
}