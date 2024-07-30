package edu.card.baselineprofile.utils

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector

fun allowPermissionsIfNeeded(permission: String) {
    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    if (device.findObject(UiSelector().textContains(permission)).exists()) {
        val allowBtn = device.findObject(UiSelector().textMatches("(?i)allow|ok"))

        if (allowBtn.exists()) {
            allowBtn.click()
        }
    }
}