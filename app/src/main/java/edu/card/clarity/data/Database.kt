package edu.card.clarity.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import edu.card.clarity.data.creditCard.CreditCardDao
import edu.card.clarity.data.creditCard.CreditCardInfoEntity
import edu.card.clarity.data.creditCard.PredefinedCreditCardIdEntity
import edu.card.clarity.data.creditCard.pointBack.CreditCardIdPointSystemIdPairEntity
import edu.card.clarity.data.creditCard.pointBack.PointBackCardPointSystemAssociationDao
import edu.card.clarity.data.pointSystem.PointSystemDao
import edu.card.clarity.data.pointSystem.PointSystemEntity
import edu.card.clarity.data.purchase.PlaceTypeToPurchaseTypeMappingDao
import edu.card.clarity.data.purchase.PlaceTypeToPurchaseTypeMappingEntity
import edu.card.clarity.data.purchase.PurchaseDao
import edu.card.clarity.data.purchase.PurchaseEntity
import edu.card.clarity.data.purchaseReward.PurchaseRewardDao
import edu.card.clarity.data.purchaseReward.PurchaseRewardEntity

@Database(
    entities = [
        PointSystemEntity::class,
        CreditCardInfoEntity::class,
        PurchaseRewardEntity::class,
        CreditCardIdPointSystemIdPairEntity::class,
        PurchaseEntity::class,
        PlaceTypeToPurchaseTypeMappingEntity::class
        PredefinedCreditCardIdEntity::class
    ],
    version = 4,
    autoMigrations = [
        AutoMigration(1, 2),
        AutoMigration(2, 3),
        AutoMigration(3, 4)
    ],
    exportSchema = true
)
abstract class Database : RoomDatabase() {
    abstract fun pointSystem(): PointSystemDao
    abstract fun pointSystemAssociation(): PointBackCardPointSystemAssociationDao

    abstract fun creditCard(): CreditCardDao
    abstract fun purchaseReward(): PurchaseRewardDao

    abstract fun purchase(): PurchaseDao
    abstract fun placeTypeToPurchaseTypeMapping(): PlaceTypeToPurchaseTypeMappingDao
}