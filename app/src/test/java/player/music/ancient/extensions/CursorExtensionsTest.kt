package player.music.ancient.extensions

import android.database.Cursor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import org.mockito.Mockito.*

class CursorExtensionsTest {

    @Test
    fun getInt_throwsIllegalStateException_whenColumnInvalid() {
        val cursor = mock(Cursor::class.java)
        val columnName = "invalid_column"
        val exception = IllegalArgumentException("Column not found")

        `when`(cursor.getColumnIndexOrThrow(columnName)).thenThrow(exception)

        val thrown = assertThrows(IllegalStateException::class.java) {
            cursor.getInt(columnName)
        }
        assertEquals("invalid column $columnName", thrown.message)
        assertEquals(exception, thrown.cause)
    }

    @Test
    fun getLong_throwsIllegalStateException_whenColumnInvalid() {
        val cursor = mock(Cursor::class.java)
        val columnName = "invalid_column"
        val exception = IllegalArgumentException("Column not found")

        `when`(cursor.getColumnIndexOrThrow(columnName)).thenThrow(exception)

        val thrown = assertThrows(IllegalStateException::class.java) {
            cursor.getLong(columnName)
        }
        assertEquals("invalid column $columnName", thrown.message)
        assertEquals(exception, thrown.cause)
    }

    @Test
    fun getString_throwsIllegalStateException_whenColumnInvalid() {
        val cursor = mock(Cursor::class.java)
        val columnName = "invalid_column"
        val exception = IllegalArgumentException("Column not found")

        `when`(cursor.getColumnIndexOrThrow(columnName)).thenThrow(exception)

        val thrown = assertThrows(IllegalStateException::class.java) {
            cursor.getString(columnName)
        }
        assertEquals("invalid column $columnName", thrown.message)
        assertEquals(exception, thrown.cause)
    }

    @Test
    fun getStringOrNull_throwsIllegalStateException_whenColumnInvalid() {
        val cursor = mock(Cursor::class.java)
        val columnName = "invalid_column"
        val exception = IllegalArgumentException("Column not found")

        `when`(cursor.getColumnIndexOrThrow(columnName)).thenThrow(exception)

        val thrown = assertThrows(IllegalStateException::class.java) {
            cursor.getStringOrNull(columnName)
        }
        assertEquals("invalid column $columnName", thrown.message)
        assertEquals(exception, thrown.cause)
    }
}
