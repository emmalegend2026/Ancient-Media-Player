package player.music.ancient.repository

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class GenreRepositoryTest {

    @Mock
    private lateinit var mockContentResolver: ContentResolver

    @Mock
    private lateinit var mockSongRepository: RealSongRepository

    private lateinit var genreRepository: RealGenreRepository

    private fun <T> anyObject(): T {
        return Mockito.any()
    }

    private fun <T> isNullObject(): T {
        return Mockito.isNull()
    }

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        genreRepository = RealGenreRepository(mockContentResolver, mockSongRepository)

        val field = player.music.ancient.App::class.java.getDeclaredField("instance")
        field.isAccessible = true
        val appInstance = mock(player.music.ancient.App::class.java)

        `when`(appInstance.applicationContext).thenReturn(appInstance)
        val mockPrefs = mock(android.content.SharedPreferences::class.java)
        `when`(appInstance.getSharedPreferences(anyString(), anyInt())).thenReturn(mockPrefs)

        field.set(null, appInstance)
    }

    @Test
    fun testGenres_SecurityException() {
        `when`(
            mockContentResolver.query(
                anyObject<Uri>(),
                anyObject<Array<String>>(),
                isNullObject<String>(),
                isNullObject<Array<String>>(),
                anyString()
            )
        ).thenThrow(SecurityException::class.java)

        val genres = genreRepository.genres()
        assertEquals(0, genres.size)
    }

    @Test
    fun testGenresWithQuery_SecurityException() {
        `when`(
            mockContentResolver.query(
                anyObject<Uri>(),
                anyObject<Array<String>>(),
                anyString(),
                anyObject<Array<String>>(),
                anyString()
            )
        ).thenThrow(SecurityException::class.java)

        val genres = genreRepository.genres("test_query")
        assertEquals(0, genres.size)
    }

    @Test
    fun testSongs_SecurityException() {
        mockStatic(android.provider.MediaStore.Audio.Genres.Members::class.java).use { mockedMembers ->
            val mockedUri = mock(Uri::class.java)
            mockedMembers.`when`<Uri> {
                android.provider.MediaStore.Audio.Genres.Members.getContentUri(anyString(), anyLong())
            }.thenReturn(mockedUri)

            `when`(
                mockContentResolver.query(
                    anyObject<Uri>(),
                    anyObject<Array<String>>(),
                    anyString(),
                    isNullObject<Array<String>>(),
                    anyString()
                )
            ).thenThrow(SecurityException::class.java)

            `when`(mockSongRepository.songs(isNullObject<Cursor>())).thenReturn(emptyList())

            val songs = genreRepository.songs(1L)
            assertEquals(0, songs.size)
        }
    }
}
