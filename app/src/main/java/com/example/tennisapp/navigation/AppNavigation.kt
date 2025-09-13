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

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(),
    tennisViewModel: TennisViewModel = viewModel(
        factory = TennisViewModelFactory(TennisRepository())
    )
) {
    val navController = rememberNavController()
    GlobalNavigation.navController = navController

    val isLoggedIn = Firebase.auth.currentUser != null
    val firstScreen = if (isLoggedIn) "home" else "auth"

    // Define bottom navigation items
    val navItems = listOf(
        NavItem("Home", Icons.Default.Home, "home"),
        NavItem("News", Icons.Default.DateRange, "news"),
        NavItem("Profile", Icons.Default.Face, "profile"),
        NavItem("Logout", Icons.Default.Lock, "logout")
    )

    Scaffold(
        topBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            // Only show header on main screens (not auth/login/signup)
            if (currentRoute !in listOf("auth", "login", "signup", "forgot")) {
                HeaderView(
                    // UNCOMMENT BELOW FOR MOCK PLAYERS
                    // players = (tennisViewModel.rankings.value.ifEmpty { MockPlayers.rankings }).map { it.player },
                    players = tennisViewModel.rankings.value.map { it.player },
                    onOpenPlayer = { playerId ->
                        navController.navigate("players/$playerId")
                    }
                )
            }
        },
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            // Show bottom bar only on main screens
            if (currentRoute !in listOf("auth", "login", "signup", "forgot")) {
                NavigationBar {
                    navItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route || navBackStackEntry?.destination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                if (item.route == "logout") {
                                    val currentUser = Firebase.auth.currentUser
                                    if (currentUser?.email == null) {
                                        authViewModel.signOutGuest()
                                    } else {
                                        authViewModel.logout()
                                    }
                                    navController.navigate("auth") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                } else {
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
            composable("tournament/{tournamentId}"){ backStackEntry ->
                val tournamentId = backStackEntry.arguments?.getString("tournamentId")?.toIntOrNull()
                TournamentScreen(
                    tournamentId = tournamentId,
                    tennisViewModel = tennisViewModel,
                    modifier = modifier.fillMaxSize()
                )
            }

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

object GlobalNavigation {
    @SuppressLint("StaticFieldLeak")
    lateinit var navController: NavHostController
}
