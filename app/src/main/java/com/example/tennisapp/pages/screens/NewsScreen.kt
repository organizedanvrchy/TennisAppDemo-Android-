package com.example.tennisapp.pages.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tennisapp.ui.theme.lexend
import com.example.tennisapp.ui.theme.lexendLight
import com.example.tennisapp.ui.theme.lexendSemiBold
import com.example.tennisapp.viewmodel.NewsViewModel

@Composable
fun NewsScreen(
    modifier: Modifier = Modifier,
    vm: NewsViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by vm.uiState

    LaunchedEffect(Unit) { vm.load() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp) // flush under header
    ) {
        // Header + refresh
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Tennis News",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        fontFamily = lexendSemiBold
                    )
                )
                Text(
                    text = "Latest from multiple sources",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontFamily = lexendLight,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                    )
                )
            }
            IconButton(onClick = { vm.refresh() }, enabled = !state.loading) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh")
            }
        }

        Divider()

        when {
            state.loading && state.articles.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            state.error != null && state.articles.isEmpty() -> {
                ErrorBlock(message = state.error ?: "Something went wrong") { vm.refresh() }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.articles, key = { it.id }) { a ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val i = Intent(Intent.ACTION_VIEW, Uri.parse(a.link))
                                    context.startActivity(i)
                                },
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(Modifier.padding(14.dp)) {
                                Text(
                                    text = a.title,
                                    style = TextStyle(
                                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                        fontFamily = lexendSemiBold
                                        ),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(Modifier.height(6.dp))
                                val meta = buildString {
                                    append(a.source)
                                    a.publishedDisplay?.let { append(" • ").append(it) }
                                }
                                Text(
                                    text = meta,
                                    style = TextStyle(
                                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                        fontFamily = lexendLight,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                )
                                a.summary?.takeIf { it.isNotBlank() }?.let { summary ->
                                    Spacer(Modifier.height(10.dp))
                                    Text(
                                        text = summary,
                                        style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize),
                                        maxLines = 3,
                                        overflow = TextOverflow.Ellipsis,
                                        fontFamily = lexend
                                    )
                                }
                                Spacer(Modifier.height(10.dp))
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                    TextButton(
                                        onClick = {
                                            val i = Intent(Intent.ACTION_VIEW, Uri.parse(a.link))
                                            context.startActivity(i)
                                        }
                                    ) { Text(
                                        text = "Open",
                                        fontFamily = lexend
                                    ) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorBlock(message: String, onRetry: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(message, color = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(12.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}
