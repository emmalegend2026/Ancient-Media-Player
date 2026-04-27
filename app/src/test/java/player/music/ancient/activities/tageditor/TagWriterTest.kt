package player.music.ancient.activities.tageditor

import android.content.Context
import android.graphics.Bitmap
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import player.music.ancient.model.AudioTagInfo
import player.music.ancient.model.ArtworkInfo
import player.music.ancient.repository.Repository
import java.io.File
import java.io.IOException
import org.koin.dsl.module
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
@OptIn(ExperimentalCoroutinesApi::class)
class TagWriterTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        startKoin {
            modules(module {
                single<Repository> { mock(Repository::class.java) }
            })
        }
    }

    @After
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun testTagWriterIOException_CompressThrows() {
        runBlocking {
            val context = ApplicationProvider.getApplicationContext<Context>()

            val bitmap = mock(Bitmap::class.java)
            `when`(bitmap.compress(
                org.mockito.Mockito.any(Bitmap.CompressFormat::class.java),
                org.mockito.Mockito.anyInt(),
                org.mockito.Mockito.any(java.io.OutputStream::class.java)
            )).thenThrow(IOException("Mock IOException"))

            val artworkInfo = ArtworkInfo(albumId = 1L, artwork = bitmap)

            // Use a valid fake file path so it doesn't fail on "CannotReadException" early
            val tempDir = File(System.getProperty("java.io.tmpdir"))
            val cacheDir = File(tempDir, "testCache")
            cacheDir.mkdirs()
            val audioFile = File(cacheDir, "invalid.mp3")
            audioFile.createNewFile()

            val info = AudioTagInfo(
                filePaths = listOf(audioFile.absolutePath),
                fieldKeyValueMap = emptyMap(),
                artworkInfo = artworkInfo
            )

            // Act
            // This should not throw an unhandled exception
            TagWriter.writeTagsToFiles(context, info)

            audioFile.delete()
        }
    }
}
