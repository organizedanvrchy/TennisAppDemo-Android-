package com.example.tennisapp.pages.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tennisapp.model.playerrankings.PlayerModel
import com.example.tennisapp.ui.theme.lexendBold
import com.example.tennisapp.ui.theme.lexendLight
import com.example.tennisapp.ui.theme.lexendSemiBold
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase

@Composable
fun HeaderView(
    players: List<PlayerModel>,        // List of all players for search/autocomplete
    onOpenPlayer: (Int) -> Unit        // Callback to navigate to player screen
) {
    var name by remember { mutableStateOf("") } // Current user’s first name

    // ---------------------------
    // Fetch user name from Firebase
    // ---------------------------
    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            Firebase.firestore.collection("users").document(uid).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Take first name only
                        val fullName = task.result.getString("name")
                        name = fullName?.split(" ")?.firstOrNull() ?: ""
                    } else {
                        name = "Guest" // fallback for errors
                    }
                }
        } else {
            name = "Guest" // Guest user
        }
    }

    // ---------------------------
    // Search state
    // ---------------------------
    var showSearch by remember { mutableStateOf(false) } // Controls overlay visibility
    var query by remember { mutableStateOf("") }         // Current search query

    // ---------------------------
    // Filtered players for search results
    // ---------------------------
    val matches by remember(query, players) {
        mutableStateOf(
            players
                .distinctBy { it.id } // Avoid duplicates
                .filter {
                    val q = query.trim()
                    if (q.isBlank()) false
                    else it.name.startsWith(q, ignoreCase = true) || it.name.contains(q, ignoreCase = true)
                }
                .sortedBy { it.name }
        )
    }

    Box(Modifier.fillMaxWidth()) {
        // ---------------------------
        // Header row: Welcome + Search Icon
        // ---------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Welcome text
            Column {
                Text("Welcome Back", style = TextStyle(fontSize = 16.sp, fontFamily = lexendLight))
                Text(name, style = TextStyle(fontSize = 22.sp, fontFamily = lexendBold))
            }

            // Search button
            IconButton(
                onClick = { showSearch = true },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.Black,
                    containerColor = Color.LightGray,
                    disabledContentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
        }

        // ---------------------------
        // Overlay search UI
        // ---------------------------
        if (showSearch) {
            // Dim background; tap to close
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.35f))
                    .clickable { showSearch = false }
            )

            // Search card at top
            Surface(
                tonalElevation = 4.dp,
                shadowElevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
            ) {
                Column(Modifier.fillMaxWidth()) {
                    // Search text field row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                        )
                        Spacer(Modifier.width(8.dp))
                        TextField(
                            value = query,
                            onValueChange = { query = it },
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 40.dp),
                            placeholder = { Text(
                                text = "Search players…",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = lexendSemiBold
                                )
                            ) },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent
                            )
                        )
                        Spacer(Modifier.width(4.dp))
                        IconButton(onClick = { showSearch = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

                    // ---------------------------
                    // Type-ahead results
                    // ---------------------------
                    when {
                        query.isBlank() -> {
                            Text(
                                "Start typing a name",
                                modifier = Modifier.padding(12.dp),
                                style = TextStyle(fontSize = 12.sp, fontFamily = lexendLight),
                                color = Color.Gray
                            )
                        }
                        matches.isEmpty() -> {
                            Text(
                                "No matches for \"$query\"",
                                modifier = Modifier.padding(12.dp),
                                style = TextStyle(fontSize = 12.sp, fontFamily = lexendLight),
                                color = Color.Gray
                            )
                        }
                        else -> {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 0.dp, max = 360.dp)
                            ) {
                                items(matches) { player ->
                                    ListItem(
                                        headlineContent = { Text(
                                            text = player.name,
                                            style = TextStyle(fontSize = 16.sp, fontFamily = lexendSemiBold)
                                        ) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                showSearch = false
                                                query = ""
                                                onOpenPlayer(player.id) // Navigate to player
                                            }
                                    )
                                    HorizontalDivider(
                                        Modifier,
                                        DividerDefaults.Thickness,
                                        DividerDefaults.color
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}


// Alt Search Box Animation
//    AnimatedVisibility(
//    visible = showSearch,
//    enter = fadeIn() + slideInVertically { -it }, // slide from top
//    exit = fadeOut() + slideOutVertically { -it } // slide up on close
//    ) {
//        val alpha by animateFloatAsState(if (showSearch) 0.35f else 0f)
//
//        // Dim background (tap to close)
//        if (alpha > 0f) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.Black.copy(alpha = alpha))
//                    .clickable { showSearch = false }
//            )
//        }
//
//        // Search card pinned to top
//        Surface(
//            tonalElevation = 4.dp,
//            shadowElevation = 8.dp,
//            shape = RoundedCornerShape(16.dp),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp)
//                .align(Alignment.TopCenter)
//                .statusBarsPadding()
//        ) {
//            Column(Modifier.fillMaxWidth()) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 12.dp, vertical = 8.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Search,
//                        contentDescription = null,
//                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
//                    )
//                    Spacer(Modifier.width(8.dp))
//                    TextField(
//                        value = query,
//                        onValueChange = { query = it },
//                        modifier = Modifier
//                            .weight(1f)
//                            .heightIn(min = 40.dp),
//                        placeholder = { Text("Search players…") },
//                        singleLine = true,
//                        colors = TextFieldDefaults.colors(
//                            focusedContainerColor = Color.Transparent,
//                            unfocusedContainerColor = Color.Transparent,
//                            disabledContainerColor = Color.Transparent
//                        )
//                    )
//                    Spacer(Modifier.width(4.dp))
//                    IconButton(onClick = { showSearch = false }) {
//                        Icon(Icons.Default.Close, contentDescription = "Close")
//                    }
//                }
//                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
//
//                // Type-ahead results
//                if (query.isBlank()) {
//                    Text(
//                        "Start typing a name",
//                        modifier = Modifier.padding(12.dp),
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = Color.Gray
//                    )
//                } else if (matches.isEmpty()) {
//                    Text(
//                        "No matches for \"$query\"",
//                        modifier = Modifier.padding(12.dp),
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = Color.Gray
//                    )
//                } else {
//                    LazyColumn(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .heightIn(min = 0.dp, max = 360.dp)
//                    ) {
//                        items(matches) { player ->
//                            ListItem(
//                                headlineContent = { Text(player.name) },
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .clickable {
//                                        showSearch = false
//                                        query = ""
//                                        onOpenPlayer(player.id) // navigate
//                                    }
//                            )
//                            HorizontalDivider(
//                                Modifier,
//                                DividerDefaults.Thickness,
//                                DividerDefaults.color
//                            )
//                        }
//                    }
//                }
//
//                Spacer(Modifier.height(8.dp))
//            }
//        }
//    }