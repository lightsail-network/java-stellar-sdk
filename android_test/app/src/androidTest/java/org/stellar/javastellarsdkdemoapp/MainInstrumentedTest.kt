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

private const val DEFAULT_TIMEOUT = 5000L
private const val PACKAGE = "org.stellar.javastellarsdkdemoapp"

/**
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainInstrumentedTest {
    @Test
    fun demoTest() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        // open app
        device.pressHome()
        val launcherPackage: String = device.launcherPackageName
        assertNotNull(launcherPackage)
        device.wait(
            Until.hasObject(By.pkg(launcherPackage).depth(0)),
            DEFAULT_TIMEOUT
        )
        val context = InstrumentationRegistry.getInstrumentation().context
        val intent =
            context.packageManager.getLaunchIntentForPackage(PACKAGE)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        // wait for app to appear
        device.wait(
            Until.hasObject(By.pkg(PACKAGE).depth(0)),
            DEFAULT_TIMEOUT
        )

        // get text
        val textNoNetworkInfo = device.wait(
            Until.findObject(By.text("No network info")),
            DEFAULT_TIMEOUT
        )
        assertNotNull(textNoNetworkInfo)

        // get button
        val button = device.wait(
            Until.findObject(By.text("Get Network")),
            DEFAULT_TIMEOUT
        )
        assertNotNull(button)

        // click button and wait screen change
        button.clickAndWait(Until.newWindow(), DEFAULT_TIMEOUT)

        // get text
        val expectedNetwork = "public"
        val textNetwork = device.wait(
            Until.findObject(By.text(expectedNetwork)),
            DEFAULT_TIMEOUT
        )
        assertNotNull(textNetwork)

        // exit app
        device.pressHome()
    }
}