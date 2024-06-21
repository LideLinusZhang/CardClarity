package edu.card.clarity.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavBar (
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home: BottomNavBar (
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    data object AddCard: BottomNavBar (
        route = "addCard",
        title = "AddCard",
        icon = Icons.Default.AddCircle
    )
    data object MyBenefits: BottomNavBar (
        route = "myBenefits",
        title = "MyBenefits",
        icon = Icons.Default.Star
    )
    data object Purchase: BottomNavBar (
        route = "purchase",
        title = "Purchase",
        icon = Icons.Default.Search
    )
}