package edu.card.clarity.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import edu.card.clarity.presentation.utils.Destinations

sealed class BottomNavBar (
    val route: String,
    val title: String,
    val icon: ImageVector,
    val desc: String
) {
    data object Home: BottomNavBar(
        route = Destinations.HOME,
        title = "Home",
        icon = Icons.Default.Home,
        desc = "home_screen"
    )
    data object AddCard: BottomNavBar(
        route = Destinations.ADD_CARD,
        title = "Add Card",
        icon = Icons.Default.AddCircle,
        desc = "add_card_screen"
    )
    data object MyReceipts: BottomNavBar(
        route = Destinations.MY_RECEIPTS,
        title = "Receipts",
        icon = Icons.Default.ShoppingCart,
        desc = "receipts_screen"
    )
    data object Purchase: BottomNavBar(
        route = Destinations.PURCHASE,
        title = "Purchase",
        icon = Icons.Default.Search,
        desc = "purchase_screen"
    )
}