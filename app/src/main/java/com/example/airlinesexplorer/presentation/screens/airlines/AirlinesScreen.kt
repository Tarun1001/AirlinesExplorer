package com.example.airlinesexplorer.presentation.screens.airlines

// AirlinesScreen.kt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.airlinesexplorer.presentation.components.AirlineCard
import com.example.airlinesexplorer.presentation.components.ErrorState
import com.example.airlinesexplorer.presentation.components.LoadingIndicator


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirlinesScreen(
    onAirlineClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AirlinesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = viewModel::onSearchQueryChange,
            placeholder = { Text("Search airlines by name or country") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true
        )

        // Content
        when {
            uiState.isLoading -> {
                LoadingIndicator(message = "Loading airlines...")
            }

            uiState.error != null -> {
                ErrorState(
                    message = uiState.error!!,
                    onRetryClick = viewModel::onRetry
                )
            }

            uiState.airlines.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text(
                        text = if (searchQuery.isNotEmpty()) "No airlines found" else "No airlines available",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(uiState.airlines) { airline ->
                        AirlineCard(
                            airline = airline,
                            onAirlineClick = onAirlineClick,
                            onFavoriteClick = viewModel::onFavoriteClick
                        )
                    }
                }
            }
        }
    }
}