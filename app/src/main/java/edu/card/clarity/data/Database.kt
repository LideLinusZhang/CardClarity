package edu.card.clarity.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import edu.card.clarity.data.alarmItem.AlarmItem
import edu.card.clarity.data.alarmItem.AlarmItemDao
import edu.card.clarity.data.creditCard.CreditCardDao
import edu.card.clarity.data.creditCard.CreditCardInfo
import edu.card.clarity.data.creditCard.pointBack.CreditCardIdPointSystemIdPair
import edu.card.clarity.data.creditCard.pointBack.PointBackCardPointSystemAssociationDao
import edu.card.clarity.data.creditCard.predefined.PredefinedCreditCardId
import edu.card.clarity.data.creditCard.predefined.PredefinedCreditCardInfo
import edu.card.clarity.data.creditCard.userAdded.UserAddedCreditCardInfo
import edu.card.clarity.data.migration.DeleteUnnecessaryTableAutoMigrationSpec
import edu.card.clarity.data.pointSystem.PointSystem
import edu.card.clarity.data.pointSystem.PointSystemDao
import edu.card.clarity.data.purchase.PlaceTypeToPurchaseTypeMapping
import edu.card.clarity.data.purchase.PlaceTypeToPurchaseTypeMappingDao
import edu.card.clarity.data.purchase.Purchase
import edu.card.clarity.data.purchase.PurchaseDao
import edu.card.clarity.data.purchase.receipt.Receipt
import edu.card.clarity.data.purchase.receipt.ReceiptDao
import edu.card.clarity.data.purchaseReward.PurchaseReward
import edu.card.clarity.data.purchaseReward.PurchaseRewardDao

@Database(
    entities = [
        PointSystem::class,
        CreditCardInfo::class,
        PurchaseReward::class,
        CreditCardIdPointSystemIdPair::class,
        Purchase::class,
        Receipt::class,
        PlaceTypeToPurchaseTypeMapping::class,
        PredefinedCreditCardId::class,
        AlarmItem::class
    ],
    views = [
        UserAddedCreditCardInfo::class,
        PredefinedCreditCardInfo::class
    ],
    version = 10,
    autoMigrations = [
        AutoMigration(1, 2),
        AutoMigration(2, 3),
        AutoMigration(3, 4),
        AutoMigration(4, 5),
        AutoMigration(5, 6),
        AutoMigration(6, 7),
        AutoMigration(7, 8),
        AutoMigration(8, 9),
        AutoMigration(9, 10, DeleteUnnecessaryTableAutoMigrationSpec::class)
    ],
    exportSchema = true
)
abstract class Database : RoomDatabase() {
    abstract fun pointSystem(): PointSystemDao
    abstract fun pointSystemAssociation(): PointBackCardPointSystemAssociationDao

    abstract fun creditCard(): CreditCardDao
    abstract fun alarmItem(): AlarmItemDao
    abstract fun purchaseReward(): PurchaseRewardDao

    abstract fun purchase(): PurchaseDao
    abstract fun receipt(): ReceiptDao
    abstract fun placeTypeToPurchaseTypeMapping(): PlaceTypeToPurchaseTypeMappingDao
}