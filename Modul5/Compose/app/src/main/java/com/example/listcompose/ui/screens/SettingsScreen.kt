package com.example.listcompose.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.listcompose.R
import com.example.listcompose.viewmodel.MovieViewModel

@Composable
fun SettingsScreen(
    viewModel: MovieViewModel,
    onBackClick: () -> Unit
) {
    val currentUsername by remember { derivedStateOf { viewModel.getUsername() } }
    var usernameInput by remember { mutableStateOf(currentUsername) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.settings_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = usernameInput,
            onValueChange = { usernameInput = it },
            label = { Text(stringResource(R.string.username_label)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.updateUsername(usernameInput)
                onBackClick()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_username_button))
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.back_button))
        }
    }
}