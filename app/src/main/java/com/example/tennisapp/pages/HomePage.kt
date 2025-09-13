package com.example.tennisapp.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tennisapp.data.TennisRepository
import com.example.tennisapp.lifecyclemanager.AppLifecycleObserver
import com.example.tennisapp.pages.screens.HomeScreen
import com.example.tennisapp.viewmodel.AuthViewModel
import com.example.tennisapp.viewmodel.TennisViewModel
import com.example.tennisapp.viewmodel.TennisViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(),
    tennisViewModel: TennisViewModel = viewModel(
        factory = TennisViewModelFactory(TennisRepository())
    )
) {
    val authViewModel = authViewModel

    val observer = remember {
        AppLifecycleObserver()
    }

    LaunchedEffect(Unit) {
        ProcessLifecycleOwner.get().lifecycle.addObserver(observer)
    }

    // This LaunchedEffect will re-run whenever isInBackground changes
    LaunchedEffect(observer.isInBackground.value) {
        if (observer.isInBackground.value) {
            val currentUser = Firebase.auth.currentUser
            if (currentUser?.email == null) {
                authViewModel.signOutGuest()
                navController.navigate("auth") {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            }
        }
    }

    HomeScreen(
        modifier,
        tennisViewModel
    )
}