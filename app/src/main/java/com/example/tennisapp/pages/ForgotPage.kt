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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tennisapp.R
import com.example.tennisapp.ui.theme.YellowGreenTennis
import com.example.tennisapp.utils.AppUtil
import com.example.tennisapp.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    var securityQuestion by remember { mutableStateOf<String?>(null) }
    var answer by remember { mutableStateOf("") }

    var canResetPassword by remember { mutableStateOf(false) }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.forgotpassword),
            contentDescription = "Forgot Password Banner",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = when {
                securityQuestion == null -> "Enter your e-mail to retrieve your security question"
                !canResetPassword -> "Answer your security question to reset your password"
                else -> "Enter your new password"
            },
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(40.dp))

        when {
            // Phase 1: Email input
            securityQuestion == null -> {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address", color = Color.DarkGray) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(60.dp))

                Button(
                    onClick = {
                        if (email.isBlank()) {
                            AppUtil.showToast(context, "Please enter your e-mail")
                            return@Button
                        }
                        isLoading = true
                        authViewModel.getSecurityQuestion(email) { question ->
                            isLoading = false
                            if (!question.isNullOrBlank()) {
                                securityQuestion = question
                            } else {
                                AppUtil.showToast(context, "No user found with that email")
                            }
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonColors(
                        containerColor = Color.Black,
                        contentColor = YellowGreenTennis,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent
                    )
                ) {
                    Text(
                        text = if (isLoading) "Looking for User" else "Submit",
                        fontSize = 22.sp
                    )
                }
            }

            // Phase 2: Security question + answer
            !canResetPassword -> {
                Text(
                    text = securityQuestion ?: "",
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    label = { Text("Your Answer", color = Color.DarkGray) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {
                        if (answer.isBlank()) {
                            AppUtil.showToast(context, "Please enter your answer")
                            return@Button
                        }
                        isLoading = true
                        authViewModel.checkSecurityQuestionAnswer(email, answer) { isCorrect, errorMessage ->
                            isLoading = false
                            if (isCorrect) {
                                authViewModel.resetPassword(email) { success, error ->
                                    isLoading = false
                                    if (success) {
                                        AppUtil.showToast(context, "An e-mail has been sent to reset your password")
                                        navController.navigate("auth")
                                    } else {
                                        AppUtil.showToast(context, error ?: "Failed to update password")
                                    }
                                }
                            } else {
                                AppUtil.showToast(context, errorMessage ?: "Something went wrong...")
                            }
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonColors(
                        containerColor = Color.Black,
                        contentColor = YellowGreenTennis,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent
                    )
                ) {
                    Text(
                        text = if (isLoading) "Checking..." else "Verify Answer",
                        fontSize = 22.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))

        FloatingActionButton(
            onClick = { navController.navigate("auth") },
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

