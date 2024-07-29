package edu.card.clarity.repositories.utils

import edu.card.clarity.data.creditCard.ICreditCardInfo
import edu.card.clarity.domain.PointSystem
import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.PurchaseReward
import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.data.pointSystem.PointSystem as PointSystemInDB
import edu.card.clarity.data.purchase.Purchase as PurchaseInDB
import edu.card.clarity.data.purchaseReward.PurchaseReward as PurchaseRewardInDB

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