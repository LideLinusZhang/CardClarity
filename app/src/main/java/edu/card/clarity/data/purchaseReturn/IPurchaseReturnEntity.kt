package edu.card.clarity.data.purchaseReturn

import edu.card.clarity.domain.PurchaseType
import java.util.UUID

interface IPurchaseReturnEntity {
    val creditCardId: UUID
    val purchaseType: PurchaseType
}