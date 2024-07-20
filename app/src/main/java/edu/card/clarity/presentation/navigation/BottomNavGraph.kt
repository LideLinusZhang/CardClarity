package edu.card.clarity.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import edu.card.clarity.enums.RewardType
import edu.card.clarity.presentation.HomeScreen
import edu.card.clarity.presentation.MyReceiptsScreen
import edu.card.clarity.presentation.PurchaseScreen
import edu.card.clarity.presentation.UpcomingPaymentsScreen
import edu.card.clarity.presentation.addBenefitScreen.AddBenefitScreen
import edu.card.clarity.presentation.addCardScreen.AddCardScreen
import edu.card.clarity.presentation.myBenefitsScreen.MyBenefitsScreen
import edu.card.clarity.presentation.myCardScreen.MyCardsScreen

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
            route = "myBenefits/{cardId}/{cardName}/{creditRewardType}",
            arguments = listOf(
                navArgument("cardId") { type = NavType.StringType },
                navArgument("cardName") { type = NavType.StringType },
                navArgument("cardRewardType") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString("cardId")!!
            val cardName = backStackEntry.arguments?.getString("cardName")!!
            val cardRewardType = RewardType.entries[backStackEntry.arguments?.getInt("cardRewardType")!!]

            navController.previousBackStackEntry?.savedStateHandle?.set("cardId", cardId)
            navController.previousBackStackEntry?.savedStateHandle?.set("cardRewardType", cardRewardType)

            MyBenefitsScreen(cardName, cardRewardType, navController)
        }
        composable(
            route = "addBenefit/{cardName}/{cardRewardType}",
            arguments = listOf(
                navArgument("cardName") { type = NavType.StringType },
                navArgument("cardRewardType") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val cardName = backStackEntry.arguments?.getString("cardName")!!
            val cardRewardType = RewardType.entries[backStackEntry.arguments?.getInt("cardRewardType")!!]

            AddBenefitScreen(cardName, cardRewardType)
        }
    }
}
