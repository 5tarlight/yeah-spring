package io.yeahx4.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class HttpRequestUtilsTest {

    @Test
    fun `test parseQueryString`() {
        val queryString = "name=JohnDoe&age=30&city=NewYork"
        val expected = mapOf("name" to "JohnDoe", "age" to "30", "city" to "NewYork")
        val result = HttpRequestUtils.parseQueryString(queryString)
        assertEquals(expected, result)
    }

    @Test
    fun `test parseCookies`() {
        val cookieString = "sessionId=abc123; userId=JohnDoe"
        val expected = mapOf("sessionId" to "abc123", "userId" to "JohnDoe")
        val result = HttpRequestUtils.parseCookies(cookieString)
        assertEquals(expected, result)
    }

    @Test
    fun `test parseValues with empty string`() {
        val result = HttpRequestUtils.parseQueryString("")
        assertTrue(result.isEmpty())
    }

    @Test
    fun `test parseHeader with valid input`() {
        val header = "Content-Type: application/json"
        val expected = Pair("Content-Type", "application/json")
        val result = HttpRequestUtils.parseHeader(header)
        assertEquals(expected, result)
    }

    @Test
    fun `test parseHeader with invalid input`() {
        val header = "Content-Type application/json"
        assertThrows(IllegalArgumentException::class.java) {
            HttpRequestUtils.parseHeader(header)
        }
    }
}
