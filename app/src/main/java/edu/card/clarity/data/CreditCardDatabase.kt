package edu.card.clarity.data

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.card.clarity.data.creditCard.CreditCardDao
import edu.card.clarity.data.creditCard.CreditCardInfoEntity
import edu.card.clarity.data.creditCard.pointBack.PointBackCardPointSystemAssociationDao
import edu.card.clarity.data.pointSystem.PointSystemDao
import edu.card.clarity.data.pointSystem.PointSystemEntity
import edu.card.clarity.data.purchaseReward.PurchaseRewardDao
import edu.card.clarity.data.purchaseReward.PurchaseRewardEntity

@Database(
    entities = [
        PointSystemEntity::class,
        CreditCardInfoEntity::class,
        PurchaseRewardEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class CreditCardDatabase : RoomDatabase() {
    abstract fun pointSystem(): PointSystemDao
    abstract fun purchaseReward(): PurchaseRewardDao

    abstract fun creditCard(): CreditCardDao
    abstract fun pointSystemAssociation(): PointBackCardPointSystemAssociationDao
}