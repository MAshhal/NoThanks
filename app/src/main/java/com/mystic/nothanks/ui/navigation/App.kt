package com.mystic.nothanks.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.mystic.nothanks.util.copy

@Composable
fun App(
    appState: AppState,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    App(
        appState = appState,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    )

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun App(
    appState: AppState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier
            .semantics {
                /* For being able to reference composable from tests and benchmarks*/
                testTagsAsResourceId = true
            },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            val destination = appState.currentTopLevelDestination
            val isTopLevelDestination = destination != null
            AnimatedVisibility(
                visible = isTopLevelDestination,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                AppBottomBar(
                    destinations = appState.topLevelDestinations,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination
                )
            }
        }
    ) { innerPadding ->
        println(innerPadding)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding.copy(LocalLayoutDirection.current, top = 0.dp)),
        ) {
            AppNavHost(
                appState = appState,
                onShowSnackbar = { message, action ->
                    snackbarHostState.showSnackbar(
                        message = message,
                        actionLabel = action,
                        duration = SnackbarDuration.Short
                    ) == SnackbarResult.ActionPerformed
                }
            )
        }
    }
}

@Composable
fun AppBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
    ) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)

            val icon: @Composable () -> Unit = {
                val contentDescription = stringResource(id = destination.iconContentDescId)
                if (selected) {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = contentDescription
                    )
                } else {
                    Icon(
                        imageVector = destination.unselectedIcon,
                        contentDescription = contentDescription
                    )
                }
            }

            NavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = icon,
                label = { Text(text = stringResource(id = destination.titleTextId)) },
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false