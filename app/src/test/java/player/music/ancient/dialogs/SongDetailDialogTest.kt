package player.music.ancient.dialogs

import android.os.Build
import android.widget.TextView
import androidx.fragment.app.testing.launchFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import player.music.ancient.R
import player.music.ancient.model.Song
import java.io.File

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O])
class SongDetailDialogTest {

    @Test
    fun testSongDetailDialogErrorPathFallback() {
        // Create a temporary file that is not a valid audio file.
        // It will cause AudioFileIO.read(songFile) to throw an exception.
        val tempFile = File.createTempFile("test_song", ".txt")
        tempFile.writeText("invalid audio content")

        // Ensure the file is deleted after the test
        tempFile.deleteOnExit()

        // Create a Song object pointing to this invalid file
        val duration = 125000L // 2:05 in milliseconds
        val song = Song(
            id = 1,
            title = "Test Song Title",
            trackNumber = 1,
            year = 2023,
            duration = duration,
            data = tempFile.absolutePath,
            dateModified = System.currentTimeMillis(),
            albumId = 1,
            albumName = "Test Album",
            artistId = 1,
            artistName = "Test Artist",
            composer = "Test Composer",
            albumArtist = "Test Album Artist"
        )

        // Launch the dialog using the fragment scenario
        val scenario = launchFragment<SongDetailDialog>(
            fragmentArgs = SongDetailDialog.create(song).arguments,
            themeResId = R.style.Theme_AncientMusic // using app theme
        )

        scenario.onFragment { fragment ->
            // The dialog has created its view inside onCreateDialog.
            val dialog = fragment.dialog
            assertNotNull("Dialog should not be null", dialog)

            // Look for the fallback UI elements that should be populated
            // since reading the invalid file causes an exception.
            val trackLengthView = dialog?.findViewById<TextView>(R.id.trackLength)
            assertNotNull("Track length view should be found", trackLengthView)

            // The duration text is formatted by MusicUtil.getReadableDurationString(song.duration)
            // For 125000L, it should be 2 mins and 5 seconds -> "02:05"
            // And it is prefixed with the bold title. We just check if the text contains "02:05"
            val text = trackLengthView?.text.toString()
            assertTrue("Track length text should contain duration string 02:05. Actual: $text", text.contains("02:05"))
        }
    }
}
