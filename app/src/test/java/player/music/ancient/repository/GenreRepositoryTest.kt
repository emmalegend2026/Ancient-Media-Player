package player.music.ancient.repository

import android.content.ContentResolver
import android.database.Cursor
import org.junit.Test
import org.junit.Assert.*
import android.content.Context
import android.provider.MediaStore.Audio.Genres
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.shadows.ShadowContentResolver
import org.robolectric.annotation.Config
import player.music.ancient.App
import java.lang.reflect.Field
import java.lang.reflect.Method
import player.music.ancient.model.Song

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class GenreRepositoryTest {

    @Test
    fun `test SecurityException returns null`() {
        val mockProvider = object : android.content.ContentProvider() {
            override fun onCreate() = true
            override fun query(
                uri: android.net.Uri, projection: Array<out String>?, selection: String?,
                selectionArgs: Array<out String>?, sortOrder: String?
            ): Cursor? = throw SecurityException("Mock SecurityException")
            override fun getType(uri: android.net.Uri): String? = null
            override fun insert(uri: android.net.Uri, values: android.content.ContentValues?): android.net.Uri? = null
            override fun delete(uri: android.net.Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
            override fun update(uri: android.net.Uri, values: android.content.ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
        }

        ShadowContentResolver.registerProviderInternal(Genres.EXTERNAL_CONTENT_URI.authority, mockProvider)

        val context = RuntimeEnvironment.getApplication()
        val app = App()
        val instanceField: Field = App::class.java.getDeclaredField("instance")
        instanceField.isAccessible = true
        instanceField.set(null, app)
        val attachBaseContext: Method = android.content.ContextWrapper::class.java.getDeclaredMethod("attachBaseContext", Context::class.java)
        attachBaseContext.isAccessible = true
        attachBaseContext.invoke(app, context)

        val repo = RealGenreRepository(context.contentResolver, RealSongRepository(context))

        val genres = repo.genres()
        assertTrue(genres.isEmpty())

        val genresQuery = repo.genres("dummy")
        assertTrue(genresQuery.isEmpty())

        val songsResult = repo.songs(100L)
        assertTrue(songsResult.isEmpty())
    }
}
