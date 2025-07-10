package com.example.airlinesexplorer.presentation.screens.details

// AirlineDetailsScreen.kt

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.airlinesexplorer.domain.Airline
import com.example.airlinesexplorer.presentation.components.ErrorState
import com.example.airlinesexplorer.presentation.components.LoadingIndicator


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirlineDetailsScreen(
    airlineId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AirlineDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(airlineId) {
        viewModel.loadAirlineDetails(airlineId)
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Airline Details") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                uiState.airline?.let { airline ->
                    IconButton(onClick = { viewModel.onFavoriteClick(airline.id) }) {
                        Icon(
                            imageVector = if (airline.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (airline.isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (airline.isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        )

        // Content
        when {
            uiState.isLoading -> {
                LoadingIndicator(message = "Loading airline details...")
            }

            uiState.error != null -> {
                ErrorState(
                    message = uiState.error!!,
                    onRetryClick = { viewModel.onRetry(airlineId) }
                )
            }

            uiState.airline != null -> {
                AirlineDetailsContent(
                    airline = uiState.airline!!,
                    onWebsiteClick = { url ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
private fun AirlineDetailsContent(
    airline: Airline,
    onWebsiteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Airline Logo and Name
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(airline.logoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "${airline.name} logo",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = airline.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = airline.country,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Airline Details
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Details",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Headquarters
                DetailItem(
                    icon = Icons.Default.LocationOn,
                    label = "Headquarters",
                    value = airline.headquarters
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Fleet Size
                DetailItem(
                    icon = Icons.Default.KeyboardArrowUp,
                    label = "Fleet Size",
                    value = "${airline.fleetSize} aircraft"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Website
                DetailItem(
                    icon = Icons.Default.Build,
                    label = "Website",
                    value = airline.website,
                    isClickable = true,
                    onClick = { onWebsiteClick(airline.website) }
                )
            }
        }
    }
}

@Composable
private fun DetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    isClickable: Boolean = false,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .let { if (isClickable && onClick != null) it.clickable { onClick() } else it },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isClickable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}