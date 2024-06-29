package edu.card.clarity.dependencyInjection

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.card.clarity.data.CreditCardDatabase
import edu.card.clarity.data.creditCard.CreditCardDao
import edu.card.clarity.data.creditCard.pointBack.PointBackCardPointSystemAssociationDao
import edu.card.clarity.data.pointSystem.PointSystemDao
import edu.card.clarity.data.purchaseReward.PurchaseRewardDao
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
    fun providePurchaseRewardDao(database: CreditCardDatabase): PurchaseRewardDao =
        database.purchaseReward()

    @Provides
    fun provideCreditCardDao(database: CreditCardDatabase): CreditCardDao =
        database.creditCard()

    @Provides
    fun providePointBackCardPointSystemAssociationDao(
        database: CreditCardDatabase
    ): PointBackCardPointSystemAssociationDao = database.pointSystemAssociation()
}