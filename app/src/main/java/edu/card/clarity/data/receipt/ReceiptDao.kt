package edu.card.clarity.data.receipt

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ReceiptDao {
    @Insert
    suspend fun insertReceipt(receipt: Receipt)

    @Query("SELECT * FROM receipts")
    suspend fun getAllReceipts(): List<Receipt>
}
