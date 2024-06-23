package edu.card.clarity.data.purchaseReturn.percentage

import androidx.room.Query
import androidx.room.Upsert
import edu.card.clarity.domain.PurchaseType
import java.util.UUID

interface PercentagePurchaseReturnDao {
    /**
     * Insert or update a purchase return in the database.
     * If a purchase return already exists, replace it.
     *
     * @param percentagePurchaseReturn the purchase return to be inserted or updated.
     */
    @Upsert
    suspend fun upsert(percentagePurchaseReturn: PercentagePurchaseReturnEntity)

    /**
     * Insert or update purchase returns in the database.
     * If a purchase return already exists, replace it.
     *
     * @param percentagePurchaseReturns the purchase returns to be inserted or updated.
     */
    @Upsert
    suspend fun upsertAll(percentagePurchaseReturns: List<PercentagePurchaseReturnEntity>)

    /**
     * Update the percentage of a purchase return.
     *
     * @param creditCardId id of the credit card.
     * @param purchaseType purchase type.
     * @param percentage percentage to be updated.
     */
    @Query(
        "UPDATE percentagePurchaseReturn " +
                "SET percentage = :percentage " +
                "WHERE creditCardId = :creditCardId AND purchaseType = :purchaseType"
    )
    suspend fun updateMultiplier(creditCardId: UUID, purchaseType: PurchaseType, percentage: Float)

    /**
     * Delete a purchase return from a credit card.
     *
     * @param creditCardId id of the credit card.
     * @param purchaseType purchase type.
     * @return the number of the purchase return deleted. This should always be 1.
     */
    @Query(
        "DELETE FROM percentagePurchaseReturn " +
                "WHERE creditCardId = :creditCardId AND purchaseType = :purchaseType"
    )
    suspend fun delete(creditCardId: UUID, purchaseType: PurchaseType): Int
}