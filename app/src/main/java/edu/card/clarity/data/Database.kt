package edu.card.clarity.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import edu.card.clarity.data.creditCard.CreditCardDao
import edu.card.clarity.data.creditCard.CreditCardInfo
import edu.card.clarity.data.creditCard.predefined.PredefinedCreditCardId
import edu.card.clarity.data.creditCard.pointBack.CreditCardIdPointSystemIdPairEntity
import edu.card.clarity.data.creditCard.pointBack.PointBackCardPointSystemAssociationDao
import edu.card.clarity.data.creditCard.predefined.PredefinedCreditCardInfo
import edu.card.clarity.data.creditCard.userAdded.UserAddedCreditCardInfo
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
        CreditCardInfo::class,
        PurchaseRewardEntity::class,
        CreditCardIdPointSystemIdPairEntity::class,
        PurchaseEntity::class,
        PlaceTypeToPurchaseTypeMappingEntity::class,
        PredefinedCreditCardId::class
    ],
    views = [
        UserAddedCreditCardInfo::class,
        PredefinedCreditCardInfo::class
    ],
    version = 7,
    autoMigrations = [
        AutoMigration(1, 2),
        AutoMigration(2, 3),
        AutoMigration(3, 4),
        AutoMigration(4, 5),
        AutoMigration(5, 6),
        AutoMigration(6, 7)
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