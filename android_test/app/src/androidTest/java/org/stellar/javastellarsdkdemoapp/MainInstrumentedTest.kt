package org.stellar.javastellarsdkdemoapp

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

private const val ONE_MINUTE = 1000L * 60
private const val PACKAGE = "org.stellar.javastellarsdkdemoapp"

/**
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainInstrumentedTest {
    @Test
    fun testSDK() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        // open app
        device.pressHome()
        val launcherPackage: String = device.launcherPackageName
        assertNotNull(launcherPackage)
        device.wait(
            Until.hasObject(By.pkg(launcherPackage).depth(0)),
            ONE_MINUTE
        )
        val context = InstrumentationRegistry.getInstrumentation().context
        val intent =
            context.packageManager.getLaunchIntentForPackage(PACKAGE)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        // wait for app to appear
        device.wait(
            Until.hasObject(By.pkg(PACKAGE).depth(0)),
            ONE_MINUTE
        )

        // get text
        val textNoTestResult = device.wait(
            Until.findObject(By.text("Not Run")),
            ONE_MINUTE
        )
        assertNotNull(textNoTestResult)

        // get button
        val button = device.wait(
            Until.findObject(By.text("Run Test")),
            ONE_MINUTE
        )
        assertNotNull(button)

        // click button and wait text to appear
        button.click()

        assertTrue(device.wait(Until.hasObject(By.text("SUCCESS")), ONE_MINUTE * 5))
    }
}