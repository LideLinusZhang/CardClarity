package edu.card.baselineprofile.journeys

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until

fun MacrobenchmarkScope.goToAddCustomCardScreenJourney() {
    // go to add card screen
    val addCardScreen =
        device.wait(Until.findObject(By.desc("add_card_screen")), 10_000)
    addCardScreen?.click()

    // go to add custom card screen
    val addCustomCardScreen =
        device.wait(Until.findObject(By.desc("add_custom_card_tab")), 10_000)

    addCustomCardScreen?.click()
}