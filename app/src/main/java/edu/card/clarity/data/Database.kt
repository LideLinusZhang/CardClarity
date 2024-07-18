package edu.card.clarity.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import edu.card.clarity.data.creditCard.CreditCardDao
import edu.card.clarity.data.creditCard.CreditCardInfoEntity
import edu.card.clarity.data.creditCard.PredefinedCreditCardIdEntity
import edu.card.clarity.data.creditCard.pointBack.CreditCardIdPointSystemIdPairEntity
import edu.card.clarity.data.creditCard.pointBack.PointBackCardPointSystemAssociationDao
import edu.card.clarity.data.pointSystem.PointSystemDao
import edu.card.clarity.data.pointSystem.PointSystemEntity
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
        PredefinedCreditCardIdEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun pointSystem(): PointSystemDao
    abstract fun pointSystemAssociation(): PointBackCardPointSystemAssociationDao

    abstract fun creditCard(): CreditCardDao
    abstract fun purchaseReward(): PurchaseRewardDao

    abstract fun purchase(): PurchaseDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE creditCardInfo ADD COLUMN isReminderEnabled INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}