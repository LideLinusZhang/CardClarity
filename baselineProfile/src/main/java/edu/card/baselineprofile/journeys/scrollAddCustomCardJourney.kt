package edu.card.baselineprofile.journeys

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction

fun MacrobenchmarkScope.scrollAddCustomCardJourney() {
    val customCardDetails = device.findObject(By.desc("custom_card_info_form"))

    customCardDetails.setGestureMargin(device.displayWidth / 5)
    customCardDetails.fling(Direction.DOWN)
    customCardDetails.fling(Direction.UP)

    device.waitForIdle()
}