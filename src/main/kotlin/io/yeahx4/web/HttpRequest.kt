package io.yeahx4.web

import io.yeahx4.util.HttpRequestUtils
import java.io.InputStream

class HttpRequest(input: InputStream) {
    val method: HttpMethod
    val path: String
    val version: String
    val headers: Map<String, String>
    private val rawBody: String
    val body: Map<String, String>
    val params: Map<String, String>

    init {
        val reader = input.bufferedReader()
        val request = reader.readLine()
        val parts = request.split(" ")

        method = HttpMethod.valueOf(parts[0])
        path = parts[1].split("?")[0]
        version = parts[2]
        headers = mutableMapOf()

        var line: String?
        do {
            line = reader.readLine()
            if (line != null && line.isNotEmpty()) {
                val headerParts = line.split(": ")
                headers[headerParts[0]] = headerParts[1]
            }
        } while (line != null && line.isNotEmpty())

        if (method == HttpMethod.POST) {
            val contentLength = headers["Content-Length"]?.toInt() ?: 0
            rawBody = reader.readText().take(contentLength)
            body = HttpRequestUtils.parseBody(rawBody, headers["Content-Type"] ?: "")
        } else {
            rawBody = ""
            body = emptyMap()
        }

        val rawPath = parts[1]
        val queryIndex = rawPath.indexOf("?")
        if (queryIndex != -1) {
            params = HttpRequestUtils.parseQueryString(rawPath.substring(queryIndex + 1))
        } else {
            params = emptyMap()
        }
    }
}
