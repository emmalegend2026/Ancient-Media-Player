package player.music.ancient.repository

import android.content.Context
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import player.music.ancient.network.LastFMService
import player.music.ancient.network.Result

class RealRepositoryTest {

    @Mock
    private lateinit var context: Context
    @Mock
    private lateinit var lastFMService: LastFMService
    @Mock
    private lateinit var songRepository: SongRepository
    @Mock
    private lateinit var albumRepository: AlbumRepository
    @Mock
    private lateinit var artistRepository: ArtistRepository
    @Mock
    private lateinit var genreRepository: GenreRepository
    @Mock
    private lateinit var lastAddedRepository: LastAddedRepository
    @Mock
    private lateinit var playlistRepository: PlaylistRepository
    @Mock
    private lateinit var searchRepository: RealSearchRepository
    @Mock
    private lateinit var topPlayedRepository: TopPlayedRepository
    @Mock
    private lateinit var roomRepository: RoomRepository
    @Mock
    private lateinit var localDataRepository: LocalDataRepository

    private lateinit var repository: RealRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = RealRepository(
            context,
            lastFMService,
            songRepository,
            albumRepository,
            artistRepository,
            genreRepository,
            lastAddedRepository,
            playlistRepository,
            searchRepository,
            topPlayedRepository,
            roomRepository,
            localDataRepository
        )
    }

    @Test
    fun `albumInfo error path returns Error`() = runTest {
        val artist = "TestArtist"
        val album = "TestAlbum"
        val exception = RuntimeException("Network error")

        `when`(lastFMService.albumInfo(artist, album)).thenAnswer { throw exception }

        val result = repository.albumInfo(artist, album)

        assertTrue(result is Result.Error)
        val errorResult = result as Result.Error
        assertTrue(errorResult.error === exception)
    }
}
