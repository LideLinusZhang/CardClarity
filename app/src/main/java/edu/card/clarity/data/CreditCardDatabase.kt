package edu.card.clarity.data

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.card.clarity.data.creditCard.cashBack.CashBackCreditCardDao
import edu.card.clarity.data.creditCard.cashBack.CashBackCreditCardEntity
import edu.card.clarity.data.creditCard.pointBack.PointBackCreditCardDao
import edu.card.clarity.data.creditCard.pointBack.PointBackCreditCardEntity
import edu.card.clarity.data.pointSystem.PointSystemDao
import edu.card.clarity.data.pointSystem.PointSystemEntity
import edu.card.clarity.data.purchaseReturn.multiplier.MultiplierPurchaseReturnEntity
import edu.card.clarity.data.purchaseReturn.percentage.PercentagePurchaseReturnEntity

@Database(
    entities = [
        PointSystemEntity::class,
        CashBackCreditCardEntity::class,
        PointBackCreditCardEntity::class,
        MultiplierPurchaseReturnEntity::class,
        PercentagePurchaseReturnEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CreditCardDatabase : RoomDatabase() {
    abstract fun pointSystem(): PointSystemDao

    abstract fun cashBackCreditCard(): CashBackCreditCardDao
    abstract fun pointBackCreditCard(): PointBackCreditCardDao
}