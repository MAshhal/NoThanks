package com.mystic.nothanks.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.mystic.nothanks.R
import com.mystic.nothanks.ui.theme.icons.AppIcons

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconContentDescId: Int,
    val titleTextId: Int
) {
    HOME(
        selectedIcon = AppIcons.Home,
        unselectedIcon = AppIcons.HomeBorder,
        iconContentDescId = R.string.home,
        titleTextId = R.string.home
    ),

    BRANDS(
        selectedIcon = AppIcons.List,
        unselectedIcon = AppIcons.ListBorder,
        iconContentDescId = R.string.brands,
        titleTextId = R.string.brands
    )

}