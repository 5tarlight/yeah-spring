package io.yeahx4.web

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileInputStream

class HttpRequestTest {
    private val testDir = "src/test/resources"

    @Test
    fun request_GET() {
        val fis = FileInputStream(File("$testDir/Http_GET.txt"))
        val req = HttpRequest(fis)

        assertEquals(HttpMethod.GET, req.method)
        assertEquals("/user/create", req.path)
        assertEquals("HTTP/1.1", req.version)
        assertEquals("keep-alive", req.headers["Connection"])
        assertEquals("javajigi", req.params["userId"])
    }

    @Test
    fun request_POST() {
        val input = FileInputStream(File("$testDir/Http_POST.txt"))
        val req = HttpRequest(input)

        assertEquals(HttpMethod.POST, req.method)
        assertEquals("/user/create", req.path)
        assertEquals("HTTP/1.1", req.version)
        assertEquals("keep-alive", req.headers["Connection"])
        assertEquals("javajigi", req.body["userId"])
        assertEquals("password", req.body["password"])
    }
}
