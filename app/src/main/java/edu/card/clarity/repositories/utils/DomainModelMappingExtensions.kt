package edu.card.clarity.repositories.utils

import edu.card.clarity.data.creditCard.ICreditCardInfo
import edu.card.clarity.data.receipt.Receipt as ReceiptInDB
import edu.card.clarity.data.pointSystem.PointSystem as PointSystemInDB
import edu.card.clarity.data.purchase.Purchase as PurchaseInDB
import edu.card.clarity.data.purchaseReward.PurchaseReward as PurchaseRewardInDB
import edu.card.clarity.domain.PointSystem
import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.PurchaseReward
import edu.card.clarity.domain.Receipt
import edu.card.clarity.domain.creditCard.CreditCardInfo
import java.util.UUID

@JvmName("creditCardInfoEntityToDomainModel")
internal fun List<ICreditCardInfo>.toDomainModel() = map(
    ICreditCardInfo::toDomainModel
)

internal fun ICreditCardInfo.toDomainModel() = CreditCardInfo(
    id = id,
    name = name,
    rewardType = rewardType,
    cardNetworkType = cardNetworkType,
    statementDate = statementDate,
    paymentDueDate = paymentDueDate,
    isReminderEnabled = isReminderEnabled,
)

@JvmName("pointSystemEntityToDomainModel")
internal fun List<PointSystemInDB>.toDomainModel() = map(PointSystemInDB::toDomainModel)

internal fun PointSystemInDB.toDomainModel() = PointSystem(
    id = id,
    name = name,
    pointToCashConversionRate = pointToCashConversionRate
)

@JvmName("purchaseRewardEntityToDomainModel")
internal fun List<PurchaseRewardInDB>.toDomainModel() =
    map(PurchaseRewardInDB::toDomainModel)

internal fun PurchaseRewardInDB.toDomainModel() = PurchaseReward(
    applicablePurchaseType = purchaseType,
    rewardFactor = factor
)

@JvmName("purchaseEntityToDomainModel")
internal fun List<PurchaseInDB>.toDomainModel() =
    map(PurchaseInDB::toDomainModel)

internal fun PurchaseInDB.toDomainModel() = Purchase(
    id = id,
    time = time,
    merchant = merchant,
    type = type,
    total = total,
    rewardAmount = rewardAmount,
    creditCardId = creditCardId
)

@JvmName("receiptEntityToDomainModel")
internal fun List<ReceiptInDB>.toDomainModel() =
    map(ReceiptInDB::toDomainModel)

internal fun ReceiptInDB.toDomainModel() = Receipt(
    id = id,
    date = date,
    totalAmount = totalAmount,
    merchant = merchant,
    selectedCardId = selectedCardId,
    selectedPurchaseType = selectedPurchaseType,
    photoPath = photoPath
)