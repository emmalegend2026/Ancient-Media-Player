package player.music.ancient.extensions

import android.database.Cursor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy

class CursorExtensionsTest {

    // Create a mock Cursor that throws an exception when any method is called (specifically getColumnIndexOrThrow).
    private fun createErrorMockCursor(): Cursor {
        val handler = InvocationHandler { _, method, _ ->
            if (method.name == "getColumnIndexOrThrow") {
                throw IllegalArgumentException("Mock exception for column")
            }
            throw UnsupportedOperationException("Not implemented in mock")
        }
        return Proxy.newProxyInstance(
            Cursor::class.java.classLoader,
            arrayOf(Cursor::class.java),
            handler
        ) as Cursor
    }

    @Test
    fun `getInt throws IllegalStateException on error`() {
        val cursor = createErrorMockCursor()
        val columnName = "test_int_column"

        val exception = assertThrows(IllegalStateException::class.java) {
            cursor.getInt(columnName)
        }

        assertEquals("invalid column $columnName", exception.message)
    }

    @Test
    fun `getLong throws IllegalStateException on error`() {
        val cursor = createErrorMockCursor()
        val columnName = "test_long_column"

        val exception = assertThrows(IllegalStateException::class.java) {
            cursor.getLong(columnName)
        }

        assertEquals("invalid column $columnName", exception.message)
    }

    @Test
    fun `getString throws IllegalStateException on error`() {
        val cursor = createErrorMockCursor()
        val columnName = "test_string_column"

        val exception = assertThrows(IllegalStateException::class.java) {
            cursor.getString(columnName)
        }

        assertEquals("invalid column $columnName", exception.message)
    }

    @Test
    fun `getStringOrNull throws IllegalStateException on error`() {
        val cursor = createErrorMockCursor()
        val columnName = "test_string_null_column"

        val exception = assertThrows(IllegalStateException::class.java) {
            cursor.getStringOrNull(columnName)
        }

        assertEquals("invalid column $columnName", exception.message)
    }
}
