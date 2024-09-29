package io.yeahx4.webserver

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class HttpMethodTest {

    @Test
    fun `test valueOf`() {
        assertEquals(HttpMethod.GET, HttpMethod.valueOf("GET"))
        assertEquals(HttpMethod.POST, HttpMethod.valueOf("POST"))
    }

    @Test
    fun `test values`() {
        val methods = HttpMethod.entries.toTypedArray()
        assertTrue(methods.contains(HttpMethod.GET))
        assertTrue(methods.contains(HttpMethod.POST))
    }
}
