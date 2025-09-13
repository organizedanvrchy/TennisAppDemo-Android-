package com.example.tennisapp.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tennisapp.ui.theme.YellowGreenTennis
import com.example.tennisapp.R
import com.example.tennisapp.ui.theme.lexendBold
import com.example.tennisapp.ui.theme.lexendLight
import com.example.tennisapp.ui.theme.lexendSemiBold
import com.example.tennisapp.utils.AppUtil
import com.example.tennisapp.viewmodel.AuthViewModel

@Composable
fun AuthPage(modifier: Modifier = Modifier,
             navController: NavHostController,
             authViewModel: AuthViewModel = viewModel()
) {
    var isLoading by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Image(
            painter = painterResource(id = R.drawable.authbanner),
            contentDescription = "Login Banner",
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        )

        Text(
            text = "Top spin into today’s tennis world.",
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = lexendSemiBold,
                textAlign = TextAlign.Center,
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Sign in or Sign up and stay in the game!",
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = lexendLight,
                textAlign = TextAlign.Center,
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = {
                navController.navigate(route = "login")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonColors(
                containerColor = YellowGreenTennis,
                contentColor = Color.Black,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            ),
            border = BorderStroke(
                width = 1.dp,
                color = Color.Transparent
            )
        ) {
            Text(
                text = "Login",
                style = TextStyle(
                    fontSize = 26.sp,
                    fontFamily = lexendBold
                ),
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = {
                navController.navigate(route = "signup")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonColors(
                containerColor = Color.Black,
                contentColor = YellowGreenTennis,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            ),
            border = BorderStroke(
                width = 1.dp,
                color = Color.Transparent
            )
        ) {
            Text(
                text = "Sign Up",
                style = TextStyle(
                    fontSize = 26.sp,
                    fontFamily = lexendBold
                ),
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        TextButton(
            onClick = {
                isLoading = true
                authViewModel.signInAsGuest { success, errorMessage ->
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
            colors = ButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            ),
            content = {
                Text(
                    text = "Sign-In as Guest",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = lexendLight
                    )
                )
            },
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            TextButton(
                onClick = {
                    isLoading = true
                    authViewModel.signInAsGuest { success, errorMessage ->
                        if(success) {
                            isLoading = false
                            navController.navigate(route = "forgot") {
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
                colors = ButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.LightGray,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.Transparent
                ),
                content = {
                    Text(
                        text = "Forgot Password",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = lexendLight
                        )
                    )
                },
            )

            TextButton(
                onClick = {
                    AppUtil.showToast(context, "Feature not yet available")
                },
                enabled = !isLoading,
                colors = ButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.LightGray,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.Transparent
                ),
                content = {
                    Text(
                        text = "Contact Us",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = lexendLight
                        )
                    )
                },
            )
        }
    }
}