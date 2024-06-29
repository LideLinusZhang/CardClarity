package edu.card.clarity.repositories

import edu.card.clarity.data.creditCard.CreditCardInfoEntity
import edu.card.clarity.data.pointSystem.PointSystemEntity
import edu.card.clarity.data.purchaseReward.PurchaseRewardEntity
import edu.card.clarity.domain.PointSystem
import edu.card.clarity.domain.PurchaseReward
import edu.card.clarity.domain.creditCard.CreditCardInfo

@JvmName("creditCardInfoEntityToDomainModel")
internal fun List<CreditCardInfoEntity>.toDomainModel() = map(
    CreditCardInfoEntity::toDomainModel
)

internal fun CreditCardInfoEntity.toDomainModel() = CreditCardInfo(
    name = name,
    rewardType = rewardType,
    cardNetworkType = cardNetworkType,
    statementDate = statementDate,
    paymentDueDate = paymentDueDate
)

@JvmName("pointSystemEntityToDomainModel")
internal fun List<PointSystemEntity>.toDomainModel() = map(PointSystemEntity::toDomainModel)

internal fun PointSystemEntity.toDomainModel() = PointSystem(
    id = id,
    name = name,
    pointToCashConversionRate = pointToCashConversionRate
)

@JvmName("purchaseRewardEntityToDomainModel")
internal fun List<PurchaseRewardEntity>.toDomainModel() =
    map(PurchaseRewardEntity::toDomainModel)

internal fun PurchaseRewardEntity.toDomainModel() = PurchaseReward(
    applicablePurchaseType = purchaseType,
    rewardFactor = factor
)
