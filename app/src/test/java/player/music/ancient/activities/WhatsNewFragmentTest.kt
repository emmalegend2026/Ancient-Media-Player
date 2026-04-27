package player.music.ancient.activities

import android.content.Context
import android.content.pm.PackageManager
import org.junit.Test
import org.mockito.Mockito.*

class WhatsNewFragmentTest {

    @Test
    fun testSetChangelogRead_PackageManagerThrowsNameNotFoundException() {
        val mockContext = mock(Context::class.java)
        val mockPackageManager = mock(PackageManager::class.java)

        `when`(mockContext.packageManager).thenReturn(mockPackageManager)
        `when`(mockContext.packageName).thenReturn("com.example.app")

        // Throw NameNotFoundException when getPackageInfo is called
        `when`(mockPackageManager.getPackageInfo("com.example.app", 0))
            .thenThrow(PackageManager.NameNotFoundException::class.java)

        val method = WhatsNewFragment.Companion::class.java.getDeclaredMethod("setChangelogRead", Context::class.java)
        method.isAccessible = true

        // Call the private method setChangelogRead
        method.invoke(WhatsNewFragment.Companion, mockContext)

        // The method should swallow the exception and not throw it further
        // We verify that getPackageInfo was called
        verify(mockPackageManager).getPackageInfo("com.example.app", 0)
    }
}
