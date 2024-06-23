package edu.card.clarity.data.purchaseReturn.multiplier

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import edu.card.clarity.domain.PurchaseType
import java.util.UUID

@Dao
interface MultiplierPurchaseReturnDao {
    /**
     * Insert or update a purchase return in the database.
     * If a purchase return already exists, replace it.
     *
     * @param multiplierPurchaseReturn the purchase return to be inserted or updated.
     */
    @Upsert
    suspend fun upsert(multiplierPurchaseReturn: MultiplierPurchaseReturnEntity)

    /**
     * Insert or update purchase returns in the database.
     * If a purchase return already exists, replace it.
     *
     * @param multiplierPurchaseReturns the purchase returns to be inserted or updated.
     */
    @Upsert
    suspend fun upsertAll(multiplierPurchaseReturns: List<MultiplierPurchaseReturnEntity>)

    /**
     * Update the multiplier of a purchase return.
     *
     * @param creditCardId id of the credit card.
     * @param purchaseType purchase type.
     * @param multiplier multiplier to be updated.
     */
    @Query(
        "UPDATE multiplierPurchaseReturn " +
                "SET multiplier = :multiplier " +
                "WHERE creditCardId = :creditCardId AND purchaseType = :purchaseType"
    )
    suspend fun updateMultiplier(creditCardId: UUID, purchaseType: PurchaseType, multiplier: Float)

    /**
     * Delete a purchase return from a credit card.
     *
     * @param creditCardId id of the credit card.
     * @param purchaseType purchase type.
     * @return the number of the purchase return deleted. This should always be 1.
     */
    @Query(
        "DELETE FROM multiplierPurchaseReturn " +
                "WHERE creditCardId = :creditCardId AND purchaseType = :purchaseType"
    )
    suspend fun delete(creditCardId: UUID, purchaseType: PurchaseType): Int
}