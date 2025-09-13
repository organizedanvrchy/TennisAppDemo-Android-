package com.example.tennisapp.pages.screens.components.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.tennisapp.ui.theme.lexend
import com.example.tennisapp.ui.theme.lexendSemiBold
import com.example.tennisapp.utils.AppUtil
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun ProfileView() {
    // Get current Firebase user
    val currUser = FirebaseAuth.getInstance().currentUser

    // ---------------------------
    // State variables for user info
    // ---------------------------
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var userImage by remember { mutableStateOf("") }

    // State to control dropdown menu expansion
    var expanded by remember { mutableStateOf(false) }

    // ---------------------------
    // Load user data from Firestore
    // ---------------------------
    LaunchedEffect(Unit) {
        // Placeholder image from Firestore
        Firebase.firestore
            .collection("data")
            .document("placeholder")
            .get().addOnCompleteListener { imgTask ->
                userImage = imgTask.result.get("imageUrl").toString()
            }

        // Current user's details (name, email)
        Firebase.firestore
            .collection("users")
            .document(currUser?.uid!!)
            .get().addOnCompleteListener { detailTask ->
                name = detailTask.result.get("name").toString().split(" ")[0] // first name only
                email = detailTask.result.get("email").toString().split(" ")[0] // first part only
            }
    }

    // ---------------------------
    // Main profile layout
    // ---------------------------
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Card with profile picture, name, and email
        ProfileInfoView(name, email, userImage)

        Spacer(modifier = Modifier.height(10.dp))

        // Dropdown for account options
        Box(contentAlignment = Alignment.Center) {
            TextButton(onClick = { expanded = true }) {
                Text(
                    text = "Account Options",
                    fontFamily = lexend,
                    color = Color.LightGray
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                ProfileOptionsView(name) // Account options like reset/delete
            }
        }
    }
}

@Composable
fun ProfileInfoView(name: String, email: String, userImage: String) {
    // Card displaying user's profile info
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ---------------------------
            // Profile Image
            // ---------------------------
            AsyncImage(
                model = userImage,
                contentDescription = "profile",
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(200.dp)
                    .border(2.dp, Color.Black, CircleShape)
                    .padding(20.dp)
            )

            // ---------------------------
            // Name and Email
            // ---------------------------
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Labels
                Column {
                    Text(text = "Name:", fontFamily = lexendSemiBold)
                    Text(text = "Email:", fontFamily = lexendSemiBold)
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Values
                Column {
                    Text(text = name, fontFamily = lexend, modifier = Modifier.padding(horizontal = 8.dp))
                    Text(
                        text = if (email == "null") "No Email" else email,
                        fontFamily = lexend,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileOptionsView(name: String) {
    val context = LocalContext.current

    // State for dialog and password input
    var showDialog by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }

    Column {
        // ---------------------------
        // Reset Password Button
        // ---------------------------
        TextButton(onClick = {
            if(name != "Guest") {
                showDialog = true // Open password dialog
            } else {
                AppUtil.showToast(context = context, message = "Guest users cannot reset password")
            }
        }) {
            Text(text = "Reset Password")
        }

        Spacer(modifier = Modifier.width(10.dp))

        // ---------------------------
        // Delete Account Button
        // ---------------------------
        TextButton(onClick = {
            if(name != "Guest") {
                /*TODO*/ // Delete logic to implement
            } else {
                AppUtil.showToast(context = context, message = "Guest users cannot delete account")
            }
        }) {
            Text(text = "Delete Account")
        }
    }

    // ---------------------------
    // Password dialog for reset
    // ---------------------------
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Enter your password") },
            text = {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation()
                )
            },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    // TODO: handle password submission
                }) {
                    Text("Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
