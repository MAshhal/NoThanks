package com.mystic.nothanks.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mystic.nothanks.ui.screens.brands.BrandsScreen
import com.mystic.nothanks.ui.screens.home.HomeScreen

@Composable
fun AppNavHost(
    appState: AppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    startDestination: String = HOME_ROUTE
) {
    val navController = appState.navController
    
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(HOME_ROUTE) {
            HomeScreen(onShowSnackbar = onShowSnackbar)
        }

        composable(BRANDS_ROUTE) {
            BrandsScreen(onShowSnackbar = onShowSnackbar)
        }

    }
    
}

fun NavHostController.safeNavigate(route: String) = navigate(route)

val NavBackStackEntry.isLifecycleResumed
    get() = lifecycle.currentState == Lifecycle.State.RESUMED

const val HOME_ROUTE = "home"
const val BRANDS_ROUTE = "brands"