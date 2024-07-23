package edu.card.clarity.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.presentation.MyReceiptsScreen
import edu.card.clarity.presentation.addCardScreen.AddCardScreen
import edu.card.clarity.presentation.homeScreen.HomeScreen
import edu.card.clarity.presentation.myCardScreen.MyCardsScreen
import edu.card.clarity.presentation.PurchaseScreen
import edu.card.clarity.presentation.UpcomingPaymentsScreen
import edu.card.clarity.presentation.addBenefitScreen.AddBenefitScreen
import edu.card.clarity.presentation.addCardScreen.AddCardScreen
import edu.card.clarity.presentation.myBenefitsScreen.MyBenefitsScreen
import edu.card.clarity.presentation.myCardScreen.MyCardsScreen
import edu.card.clarity.presentation.purchaseBenefitsScreen.PurchaseOptimalBenefitsScreen
import edu.card.clarity.presentation.utils.ArgumentNames
import edu.card.clarity.presentation.utils.Destinations

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Destinations.HOME
    ) {
        composable(Destinations.HOME) {
            HomeScreen(navController)
        }
        composable(Destinations.ADD_CARD) {
            AddCardScreen(navController)
        }
        composable(Destinations.MY_RECEIPTS) {
            MyReceiptsScreen()
        }
        composable(Destinations.PURCHASE) {
            PurchaseScreen(navController)
        }
        composable(Destinations.MY_CARDS) {
            MyCardsScreen(navController)
        }
        composable(Destinations.UPCOMING_PAYMENTS) {
            UpcomingPaymentsScreen()
        }
        composable(
            route = "${Destinations.MY_BENEFITS}/{${ArgumentNames.CREDIT_CARD_ID}}/{${ArgumentNames.CREDIT_CARD_NAME}}/{${ArgumentNames.CREDIT_CARD_REWARD_TYPE}}",
            arguments = listOf(
                navArgument(ArgumentNames.CREDIT_CARD_ID) { type = NavType.StringType },
                navArgument(ArgumentNames.CREDIT_CARD_NAME) { type = NavType.StringType },
                navArgument(ArgumentNames.CREDIT_CARD_REWARD_TYPE) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString(ArgumentNames.CREDIT_CARD_ID)!!
            val cardName = backStackEntry.arguments?.getString(ArgumentNames.CREDIT_CARD_NAME)!!
            val cardRewardTypeOrdinal =
                backStackEntry.arguments?.getInt(ArgumentNames.CREDIT_CARD_REWARD_TYPE)!!

            navController.previousBackStackEntry?.savedStateHandle?.set(
                ArgumentNames.CREDIT_CARD_ID,
                cardId
            )
            navController.previousBackStackEntry?.savedStateHandle?.set(
                ArgumentNames.CREDIT_CARD_REWARD_TYPE,
                cardRewardTypeOrdinal
            )

            MyBenefitsScreen(cardName, navController)
        }
        composable(
            route = "${Destinations.ADD_BENEFIT}/{${ArgumentNames.CREDIT_CARD_ID}}/{${ArgumentNames.CREDIT_CARD_NAME}}/{${ArgumentNames.CREDIT_CARD_REWARD_TYPE}}",
            arguments = listOf(
                navArgument(ArgumentNames.CREDIT_CARD_ID) { type = NavType.StringType },
                navArgument(ArgumentNames.CREDIT_CARD_NAME) { type = NavType.StringType },
                navArgument(ArgumentNames.CREDIT_CARD_REWARD_TYPE) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString(ArgumentNames.CREDIT_CARD_ID)!!
            val cardName = backStackEntry.arguments?.getString(ArgumentNames.CREDIT_CARD_NAME)!!
            val cardRewardTypeOrdinal =
                backStackEntry.arguments?.getInt(ArgumentNames.CREDIT_CARD_REWARD_TYPE)!!

            navController.previousBackStackEntry?.savedStateHandle?.set(
                ArgumentNames.CREDIT_CARD_ID,
                cardId
            )
            navController.previousBackStackEntry?.savedStateHandle?.set(
                ArgumentNames.CREDIT_CARD_REWARD_TYPE,
                cardRewardTypeOrdinal
            )

            AddBenefitScreen(cardName)
        }
        composable(
            route = "${Destinations.PURCHASE_OPTIMAL_BENEFITS}/{category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryString = backStackEntry.arguments?.getString("category")!!
            val category = PurchaseType.valueOf(categoryString)
            PurchaseOptimalBenefitsScreen(navController, category)
        }
    }
}
