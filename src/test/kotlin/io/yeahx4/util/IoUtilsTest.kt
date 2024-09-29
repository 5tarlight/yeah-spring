package io.yeahx4.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.StringReader

class IoUtilsTest {

    @Test
    fun `test readData with valid input`() {
        val input = "This is a test request body"
        val length = input.length
        val reader = BufferedReader(StringReader(input))

        val result = IoUtils.readData(reader, length)
        assertEquals(input, result)
    }

    @Test
    fun `test readData with empty input`() {
        val input = ""
        val length = input.length
        val reader = BufferedReader(StringReader(input))

        val result = IoUtils.readData(reader, length)
        assertEquals(input, result)
    }

    @Test
    fun `test readData with partial input`() {
        val input = "Partial input"
        val length = 7
        val reader = BufferedReader(StringReader(input))

        val result = IoUtils.readData(reader, length)
        assertEquals("Partial", result)
    }
}
