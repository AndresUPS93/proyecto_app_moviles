package com.example.dictionaryapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationItem(
    val route: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val title: String
) {
    data object Home: BottomNavigationItem(
        route = "home",
        unselectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
        title = "Home"
    )

    data object Search: BottomNavigationItem(
        route = "search",
        unselectedIcon = Icons.Outlined.Search,
        selectedIcon = Icons.Filled.Search,
        title = "Search"
    )

    data object History: BottomNavigationItem(
        route = "history",
        unselectedIcon = Icons.Outlined.Bookmark,
        selectedIcon = Icons.Filled.Bookmark,
        title = "History"
    )

}