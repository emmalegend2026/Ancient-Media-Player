package player.music.ancient.extensions

import android.database.CharArrayBuffer
import android.database.Cursor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.lang.IllegalArgumentException

class CursorExtensionsTest {

    // Minimal implementation of a Cursor to test error paths without mocking frameworks
    abstract class MockCursor : Cursor {
        override fun getColumnIndexOrThrow(columnName: String?): Int {
            if (columnName == "valid_column") {
                return 0
            }
            throw IllegalArgumentException("column '$columnName' does not exist")
        }

        override fun getInt(columnIndex: Int): Int {
            if (columnIndex == 0) return 42
            throw IllegalArgumentException()
        }

        override fun getLong(columnIndex: Int): Long {
            if (columnIndex == 0) return 42L
            throw IllegalArgumentException()
        }

        override fun getString(columnIndex: Int): String {
            if (columnIndex == 0) return "value"
            throw IllegalArgumentException()
        }

        override fun copyStringToBuffer(columnIndex: Int, buffer: CharArrayBuffer?) {}
        override fun getType(columnIndex: Int) = 0

        // Stubs for remaining methods
        override fun getCount() = 0
        override fun getPosition() = 0
        override fun move(offset: Int) = false
        override fun moveToPosition(position: Int) = false
        override fun moveToFirst() = false
        override fun moveToLast() = false
        override fun moveToNext() = false
        override fun moveToPrevious() = false
        override fun isFirst() = false
        override fun isLast() = false
        override fun isBeforeFirst() = false
        override fun isAfterLast() = false
        override fun getColumnIndex(columnName: String?) = -1
        override fun getColumnName(columnIndex: Int) = ""
        override fun getColumnNames() = arrayOf<String>()
        override fun getColumnCount() = 0
        override fun getBlob(columnIndex: Int) = ByteArray(0)
        override fun getShort(columnIndex: Int): Short = 0
        override fun getFloat(columnIndex: Int) = 0f
        override fun getDouble(columnIndex: Int) = 0.0
        override fun isNull(columnIndex: Int) = false
        override fun deactivate() {}
        override fun requery() = false
        override fun close() {}
        override fun isClosed() = false
        override fun registerContentObserver(observer: android.database.ContentObserver?) {}
        override fun unregisterContentObserver(observer: android.database.ContentObserver?) {}
        override fun registerDataSetObserver(observer: android.database.DataSetObserver?) {}
        override fun unregisterDataSetObserver(observer: android.database.DataSetObserver?) {}
        override fun setNotificationUri(cr: android.content.ContentResolver?, uri: android.net.Uri?) {}
        override fun getNotificationUri(): android.net.Uri? = null
        override fun getWantsAllOnMoveCalls() = false
        override fun setExtras(extras: android.os.Bundle?) {}
        override fun getExtras() = android.os.Bundle()
        override fun respond(extras: android.os.Bundle?) = android.os.Bundle()
    }

    private val cursor = object : MockCursor() {}

    @Test
    fun `getInt happy path`() {
        assertEquals(42, cursor.getInt("valid_column"))
    }

    @Test
    fun `getInt error path`() {
        val ex = assertThrows(IllegalStateException::class.java) {
            cursor.getInt("invalid_column")
        }
        assertEquals("invalid column invalid_column", ex.message)
        assertEquals(IllegalArgumentException::class.java, ex.cause?.javaClass)
    }

    @Test
    fun `getLong happy path`() {
        assertEquals(42L, cursor.getLong("valid_column"))
    }

    @Test
    fun `getLong error path`() {
        val ex = assertThrows(IllegalStateException::class.java) {
            cursor.getLong("invalid_column")
        }
        assertEquals("invalid column invalid_column", ex.message)
        assertEquals(IllegalArgumentException::class.java, ex.cause?.javaClass)
    }

    @Test
    fun `getString happy path`() {
        assertEquals("value", cursor.getString("valid_column"))
    }

    @Test
    fun `getString error path`() {
        val ex = assertThrows(IllegalStateException::class.java) {
            cursor.getString("invalid_column")
        }
        assertEquals("invalid column invalid_column", ex.message)
        assertEquals(IllegalArgumentException::class.java, ex.cause?.javaClass)
    }

    @Test
    fun `getStringOrNull happy path`() {
        assertEquals("value", cursor.getStringOrNull("valid_column"))
    }

    @Test
    fun `getStringOrNull error path`() {
        val ex = assertThrows(IllegalStateException::class.java) {
            cursor.getStringOrNull("invalid_column")
        }
        assertEquals("invalid column invalid_column", ex.message)
        assertEquals(IllegalArgumentException::class.java, ex.cause?.javaClass)
    }
}
