package edu.card.clarity.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import edu.card.clarity.presentation.addCardScreen.AddCardScreen
import edu.card.clarity.presentation.HomeScreen
import edu.card.clarity.presentation.MyReceiptsScreen
import edu.card.clarity.presentation.myCardScreen.MyCardsScreen
import edu.card.clarity.presentation.PurchaseScreen
import edu.card.clarity.presentation.UpcomingPaymentsScreen
import edu.card.clarity.presentation.myBenefitsScreen.AddBenefitScreen
import edu.card.clarity.presentation.myBenefitsScreen.MyBenefitsScreen
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
            MyReceiptsScreen()
        }
        composable(route = BottomNavBar.Purchase.route) {
            PurchaseScreen()
        }
        composable("myCards") {
            MyCardsScreen(navController)
        }
        composable("upcomingPayments") {
            UpcomingPaymentsScreen()
        }
        composable(
            route = "myBenefits/{cardName}/{cardId}",
            arguments = listOf(
                navArgument("cardName") { type = NavType.StringType },
                navArgument("cardId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val cardName = backStackEntry.arguments?.getString("cardName") ?: ""
            val cardIdString = backStackEntry.arguments?.getString("cardId") ?: ""
            val cardId = cardIdString.takeIf { it.isNotEmpty() }?.let { UUID.fromString(it) }
            MyBenefitsScreen(cardName = cardName, cardId = cardId, navController)
        }
        composable(
            route = "addBenefit/{cardName}/{rewardType}/{cardId}",
            arguments = listOf(
                navArgument("cardName") { type = NavType.StringType },
                navArgument("rewardType") { type = NavType.StringType },
                navArgument("cardId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val cardName = backStackEntry.arguments?.getString("cardName") ?: ""
            val rewardType = backStackEntry.arguments?.getString("rewardType") ?: ""
            val cardIdString = backStackEntry.arguments?.getString("cardId") ?: ""
            val cardId = cardIdString.takeIf { it.isNotEmpty() }?.let { UUID.fromString(it) }
            AddBenefitScreen(cardName = cardName, rewardType = rewardType, cardId = cardId)
        }
    }
}
