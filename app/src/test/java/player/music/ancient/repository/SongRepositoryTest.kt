package player.music.ancient.repository

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import org.junit.Assert.assertNull
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import io.mockk.mockkObject
import io.mockk.every
import io.mockk.unmockkAll
import io.mockk.mockk
import org.junit.After
import player.music.ancient.App
import player.music.ancient.util.PreferenceUtil

class SongRepositoryTest {

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testMakeSongCursorSecurityException() {
        // We can set the App singleton using reflection directly to mockApp
        // This avoids trying to mockk the exact getContext() call if it still throws NPE during proxy eval
        val mockApp = mockk<App>(relaxed = true)

        try {
            val instanceField = App.Companion::class.java.getDeclaredField("instance")
            instanceField.isAccessible = true
            instanceField.set(App.Companion, mockApp)
        } catch (e: Exception) {
            try {
                val instanceField = App::class.java.getDeclaredField("instance")
                instanceField.isAccessible = true
                instanceField.set(null, mockApp)
            } catch (e2: Exception) {}
        }

        mockkObject(PreferenceUtil)
        every { PreferenceUtil.songSortOrder } returns "mockSortOrder"
        every { PreferenceUtil.isWhiteList } returns false

        val mockContext = mockk<Context>(relaxed = true)
        val mockContentResolver = mockk<ContentResolver>(relaxed = true)

        every { mockContext.contentResolver } returns mockContentResolver

        every {
            mockContentResolver.query(
                any(),
                any(),
                any<String>(),
                any(),
                any<String>()
            )
        } throws SecurityException("Mock SecurityException")

        val repository = RealSongRepository(mockContext)

        val cursor = repository.makeSongCursor(null, null, "sortOrder", true)
        assertNull(cursor)
    }
}
