package player.music.ancient.activities

import android.content.Context
import android.content.res.AssetManager
import android.os.Build
import android.view.ContextThemeWrapper
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.Shadows.shadowOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import java.io.IOException

class TestWhatsNewFragment : WhatsNewFragment() {
    override fun getContext(): Context? {
        val realContext = super.getContext()
        if (realContext == null) return null

        val mockAssetManager = mock<AssetManager> {
            on { open("ancient-changelog.html") } doThrow IOException("Simulated asset load failure")
        }

        val mockContext = object : ContextThemeWrapper(realContext, com.google.android.material.R.style.Theme_MaterialComponents_DayNight) {
            override fun getAssets(): AssetManager {
                return mockAssetManager
            }
        }

        return mockContext
    }
}

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class WhatsNewFragmentTest {

    @Test
    fun testWebViewErrorPath() {
        val scenario = launchFragmentInContainer<TestWhatsNewFragment>(themeResId = com.google.android.material.R.style.Theme_MaterialComponents_DayNight)
        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.onFragment { fragment ->
            val webView = fragment.binding.webView
            val shadowWebView = shadowOf(webView)

            val loadDataWithBaseURL = shadowWebView.lastLoadDataWithBaseURL
            val loadData = shadowWebView.lastLoadData

            var isUnableToLoad = false
            if (loadDataWithBaseURL != null) {
                isUnableToLoad = loadDataWithBaseURL.data.contains("Unable to load")
            } else if (loadData != null) {
                isUnableToLoad = loadData.data.contains("Unable to load")
            }

            assertTrue("Expected loadData to contain Unable to load", isUnableToLoad)
        }
    }
}
