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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tennisapp.R
import com.example.tennisapp.utils.AppUtil
import com.example.tennisapp.viewmodel.AuthViewModel

@Composable
fun LoginPage(modifier: Modifier = Modifier,
              navController: NavHostController,
              authViewModel: AuthViewModel = viewModel()
) {
    var email by remember {
        mutableStateOf(value = "")
    }

    var password by remember {
        mutableStateOf(value = "")
    }

    var isLoading by remember {
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
                .height(300.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Welcome back!",
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Serving you greatness, one login at a time.",
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 22.sp,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center,
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text(text = "Email Address")
            },
            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(text = "Password")
            },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()

        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                when {
                    email.isBlank() -> {
                        AppUtil.showToast(context, "Please enter your email")
                        return@Button
                    }
                    password.isBlank() -> {
                        AppUtil.showToast(context, "Please enter a password")
                        return@Button
                    }
                    password.length < 6 -> {
                        AppUtil.showToast(context, "Invalid Password")
                        return@Button
                    }
                }

                isLoading = true
                authViewModel.login(email, password) { success, errorMessage ->
                    if(success) {
                        isLoading = false
                        navController.navigate(route = "home") {
                            popUpTo("auth") {
                                inclusive = true
                            }
                        }
                    } else {
                        isLoading = false
                        AppUtil.showToast(
                            context,
                            errorMessage?:"Something went wrong..."
                        )
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonColors(
                containerColor = Color.Black,
                contentColor = Color.White,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent,
            )
        ) {
            Text(
                text = if(isLoading) "Logging In" else "Log In",
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