package edu.card.clarity.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import edu.card.clarity.presentation.addCardScreen.AddCardScreen
import edu.card.clarity.presentation.HomeScreen
import edu.card.clarity.presentation.MyBenefitsScreen
import edu.card.clarity.presentation.myCardScreen.MyCardsScreen
import edu.card.clarity.presentation.PurchaseScreen
import edu.card.clarity.presentation.UpcomingPaymentsScreen
import edu.card.clarity.presentation.myReceiptsScreen.MyReceiptsScreen
import java.util.UUID

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavBar.Home.route
    ) {
        composable(BottomNavBar.Home.route) {
            HomeScreen(navController)
        }
        composable(route = BottomNavBar.AddCard.route) {
            AddCardScreen(navController)
        }
        composable(route = BottomNavBar.MyReceipts.route) {
            MyReceiptsScreen(navController)
        }
        composable(route = BottomNavBar.Purchase.route) {
            PurchaseScreen()
        }
        composable("myCards") {
            MyCardsScreen()
        }
        composable("upcomingPayments") {
            UpcomingPaymentsScreen()
        }

    }
}