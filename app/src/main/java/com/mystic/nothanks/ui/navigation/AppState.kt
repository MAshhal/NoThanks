package com.mystic.nothanks.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberAppState(
    windowSizeClass: WindowSizeClass,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController()
) : AppState {
    return remember(windowSizeClass, coroutineScope, navController) {
        AppState(navController, coroutineScope, windowSizeClass)
    }
}

class AppState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
    val windowSizeClass: WindowSizeClass
) {

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when(currentDestination?.route) {
            "home" -> TopLevelDestination.HOME
            "brands" -> TopLevelDestination.BRANDS
            else -> null
        }

    val shouldShowBottomBar : Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean
        get() = !shouldShowBottomBar

    /**
     * Map of top level destination to be used in the TopBar, BottomBar or NavRail. The key is the
     * route.
     */
    val topLevelDestinations : List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(
        destination: TopLevelDestination
    ) {
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }

            // Avoid multiple copies of the same destination when
            // re-selecting the same item
            launchSingleTop = true
            // Restore state when re-selecting a previously selected item
            restoreState = true
        }

        // navigate here
        when(destination) {
            TopLevelDestination.HOME -> navController.navigate("home", topLevelNavOptions)
            TopLevelDestination.BRANDS -> navController.navigate("brands", topLevelNavOptions)
        }
    }
}