package edu.card.clarity.presentation.myReceiptsScreen

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class ParcelableCreditCardInfo(
    val id: UUID? = null,
    val name: String
): Parcelable