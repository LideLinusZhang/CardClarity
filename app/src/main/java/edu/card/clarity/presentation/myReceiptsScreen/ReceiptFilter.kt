package edu.card.clarity.presentation.myReceiptsScreen

import android.os.Parcelable
import edu.card.clarity.enums.PurchaseType
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class ReceiptFilter(
    val filteredCreditCardId: UUID? = null,
    val filteredPurchaseType: PurchaseType? = null
) : Parcelable