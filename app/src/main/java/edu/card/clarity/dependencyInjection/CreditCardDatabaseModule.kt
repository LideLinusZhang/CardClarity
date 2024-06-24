package edu.card.clarity.dependencyInjection

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.card.clarity.data.CreditCardDatabase
import edu.card.clarity.data.creditCard.cashBack.CashBackCreditCardDao
import edu.card.clarity.data.creditCard.pointBack.PointBackCreditCardDao
import edu.card.clarity.data.pointSystem.PointSystemDao
import edu.card.clarity.data.purchaseReturn.multiplier.MultiplierPurchaseReturnDao
import edu.card.clarity.data.purchaseReturn.percentage.PercentagePurchaseReturnDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CreditCardDatabaseModule {
    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): CreditCardDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CreditCardDatabase::class.java,
            "credit_card_db"
        ).build()
    }

    @Provides
    fun providePointSystemDao(database: CreditCardDatabase): PointSystemDao = database.pointSystem()

    @Provides
    fun providePercentagePurchaseReturnDao(database: CreditCardDatabase): PercentagePurchaseReturnDao =
        database.percentagePurchaseReturn()

    @Provides
    fun provideMultiplierPurchaseReturnDao(database: CreditCardDatabase): MultiplierPurchaseReturnDao =
        database.multiplierPurchaseReturn()

    @Provides
    fun provideCashBackCreditCardDao(database: CreditCardDatabase): CashBackCreditCardDao =
        database.cashBackCreditCard()

    @Provides
    fun providePointBackCreditCardDao(database: CreditCardDatabase): PointBackCreditCardDao =
        database.pointBackCreditCard()
}