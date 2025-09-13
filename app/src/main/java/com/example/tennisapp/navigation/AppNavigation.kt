package com.example.tennisapp.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tennisapp.data.TennisRepository
import com.example.tennisapp.model.NavItem
import com.example.tennisapp.pages.AuthPage
import com.example.tennisapp.pages.ForgotPage
import com.example.tennisapp.pages.HomePage
import com.example.tennisapp.pages.LoginPage
import com.example.tennisapp.pages.SignupPage
import com.example.tennisapp.pages.screens.LiveTournamentScreen
import com.example.tennisapp.pages.screens.ProfileScreen
import com.example.tennisapp.pages.screens.NewsScreen
import com.example.tennisapp.pages.screens.PlayerScreen
import com.example.tennisapp.pages.screens.TournamentScreen
import com.example.tennisapp.pages.screens.components.HeaderView
import com.example.tennisapp.ui.theme.lexend
import com.example.tennisapp.viewmodel.AuthViewModel
import com.example.tennisapp.viewmodel.TennisViewModel
import com.example.tennisapp.viewmodel.TennisViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

// Main Composable that sets up navigation, top/bottom bars, and screens
@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    // AuthViewModel using default factory
    authViewModel: AuthViewModel = viewModel(),
    // TennisViewModel using a custom factory with TennisRepository
    tennisViewModel: TennisViewModel = viewModel(
        factory = TennisViewModelFactory(TennisRepository())
    )
) {
    // Create NavController for navigation
    val navController = rememberNavController()
    // Store navController globally for external navigation usage
    GlobalNavigation.navController = navController

    // Determine if user is logged in using Firebase Auth
    val isLoggedIn = Firebase.auth.currentUser != null
    val firstScreen = if (isLoggedIn) "home" else "auth"

    // Define bottom navigation items
    val navItems = listOf(
        NavItem("Home", Icons.Default.Home, "home"),
        NavItem("News", Icons.Default.DateRange, "news"),
        NavItem("Profile", Icons.Default.Face, "profile"),
        NavItem("Logout", Icons.Default.Lock, "logout")
    )

    // Scaffold for top bar, bottom bar, and main content
    Scaffold(
        topBar = {
            // Observe current back stack entry to conditionally show top bar
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            // Only show header on main screens (hide on auth/login/signup/forgot)
            if (currentRoute !in listOf("auth", "login", "signup", "forgot")) {
                HeaderView(
                    // Use live rankings from tennisViewModel
                    // Uncomment below line for mock players if needed
                    // players = (tennisViewModel.rankings.value.ifEmpty { MockPlayers.rankings }).map { it.player },
                    players = tennisViewModel.rankings.value.map { it.player },
                    // Navigate to player details on click
                    onOpenPlayer = { playerId ->
                        navController.navigate("players/$playerId")
                    }
                )
            }
        },
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            // Show bottom navigation bar only on main screens
            if (currentRoute !in listOf("auth", "login", "signup", "forgot")) {
                NavigationBar {
                    navItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route || navBackStackEntry?.destination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                if (item.route == "logout") {
                                    // Handle logout for guest or authenticated users
                                    val currentUser = Firebase.auth.currentUser
                                    if (currentUser?.email == null) {
                                        authViewModel.signOutGuest()
                                    } else {
                                        authViewModel.logout()
                                    }
                                    // Navigate to auth screen and clear back stack
                                    navController.navigate("auth") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                } else {
                                    // Navigate to selected route with singleTop and state restoration
                                    navController.navigate(item.route) {
                                        popUpTo(item.route) { inclusive = true } // reset Home
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(
                                item.label,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = lexend
                                )
                            ) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // Main navigation host with all app routes
        NavHost(
            navController = navController,
            startDestination = firstScreen,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Auth routes
            composable("auth") { AuthPage(modifier, navController) }
            composable("login") { LoginPage(modifier, navController) }
            composable("signup") { SignupPage(modifier, navController) }
            composable("forgot") { ForgotPage(modifier, navController) }

            // Main app routes
            composable("home") { HomePage(modifier, navController, authViewModel, tennisViewModel) }
            composable("news") { NewsScreen(modifier) }
            composable("profile") { ProfileScreen(modifier) }
            composable("live") { LiveTournamentScreen(
                modifier,
                tennisViewModel = tennisViewModel,
            ) }
            // Tournament details route with argument
            composable("tournament/{tournamentId}"){ backStackEntry ->
                val tournamentId = backStackEntry.arguments?.getString("tournamentId")?.toIntOrNull()
                TournamentScreen(
                    tournamentId = tournamentId,
                    tennisViewModel = tennisViewModel,
                    modifier = modifier.fillMaxSize()
                )
            }

            // Player details route with argument
            composable("players/{playerId}") { backStackEntry ->
                val playerId = backStackEntry.arguments?.getString("playerId")?.toIntOrNull()
                PlayerScreen(
                    playerId = playerId,
                    tennisViewModel = tennisViewModel,
                    modifier = modifier.fillMaxSize()
                )
            }
        }
    }
}

// Global object to store NavController for app-wide navigation usage
object GlobalNavigation {
    @SuppressLint("StaticFieldLeak")
    lateinit var navController: NavHostController
}
