package com.example.tennisapp.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tennisapp.R
import com.example.tennisapp.utils.AppUtil
import com.example.tennisapp.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupPage(modifier: Modifier = Modifier,
               navController: NavHostController,
               authViewModel: AuthViewModel = viewModel()
) {
    val securityQuestions = listOf(
        "What was your childhood nickname?",
        "What is the name of your first pet?",
        "What city were you born in?",
        "What is wrong with you?"
    )

    var email by remember {
        mutableStateOf(value = "")
    }

    var name by remember {
        mutableStateOf(value = "")
    }

    var password by remember {
        mutableStateOf(value = "")
    }

    var selectedQuestion by remember {
        mutableStateOf(value = "")
    }

    var answer by remember {
        mutableStateOf(value = "")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Image(
            painter = painterResource(id = R.drawable.loginbannerimg),
            contentDescription = "Signup Banner",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Stats, scores, stories — it starts with sign-up.",
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
            },
            label = {
                Text(
                    text = "First Name | Last Name",
                    color = Color.DarkGray
                )
            },
            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text(
                    text = "Email Address",
                    color = Color.DarkGray
                )
            },
            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(
                    text = "Password",
                    color = Color.DarkGray
                )
            },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()

        )

        Spacer(modifier = Modifier.height(15.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedQuestion,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Security Question") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                securityQuestions.forEach { question ->
                    DropdownMenuItem(
                        text = { Text(question) },
                        onClick = {
                            selectedQuestion = question
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = answer,
            onValueChange = { answer = it },
            label = { Text("Your Answer", color = Color.DarkGray) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {
                // Input validation before starting signup
                when {
                    name.isBlank() -> {
                        AppUtil.showToast(context, "Please enter your name")
                        return@Button
                    }
                    email.isBlank() -> {
                        AppUtil.showToast(context, "Please enter your email")
                        return@Button
                    }
                    password.isBlank() -> {
                        AppUtil.showToast(context, "Please enter a password")
                        return@Button
                    }
                    selectedQuestion.isBlank() -> {
                        AppUtil.showToast(context, "Please select a security question")
                        return@Button
                    }
                    answer.isBlank() -> {
                        AppUtil.showToast(context, "Please answer the security question")
                        return@Button
                    }


                    //Replaced this snippet with a more thorough check in AuthViewModel.kt
                    /*
                    password.length < 6 -> {
                        AppUtil.showToast(context, "Password must be at least 6 characters")
                        return@Button
                    }
                     */
                }

                isLoading = true
                authViewModel.signup(email, name, password, selectedQuestion, answer ) { success, errorMessage ->
                    if (success) {
                        isLoading = false
                        navController.navigate(route = "login") {
                            popUpTo("auth") {
                                inclusive = true
                            }
                        }
                    } else {
                        isLoading = false
                        AppUtil.showToast(
                            context,
                            errorMessage ?: "Something went wrong..."
                        )
                    }
                }
            },
            enabled = !isLoading, // Disables button during background load
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonColors(
                containerColor = Color.Black,
                contentColor = Color.White,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent,
            )
        ) {
            Text(
                text = if(isLoading) "Creating Account" else "Sign Up",
                fontSize = 22.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        FloatingActionButton(
            onClick = {
                navController.navigate(route = "auth")
            },
            shape = CircleShape,
            containerColor = Color.White,
            contentColor = Color.Black,
            content = {
                Icon(
                    painter = painterResource(R.drawable.returnhome),
                    contentDescription = "return"
                )
            },
            modifier = Modifier.offset(150.dp, 0.dp)
        )
    }
}