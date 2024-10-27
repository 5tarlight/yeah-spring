package io.yeahx4.web

import java.io.InputStream

class HttpRequest(input: InputStream) {
    val method: HttpMethod
    val path: String
    val version: String
    val headers: Map<String, String>
    private val rawBody: String
    val params: Map<String, String>

    init {
        val request = input.bufferedReader().readLine()
        val parts = request.split(" ")

        method = HttpMethod.valueOf(parts[0])
        path = parts[1].split("?")[0]
        version = parts[2]
        headers = mutableMapOf()
        params = mutableMapOf()

        var line: String?
        do {
            line = input.bufferedReader().readLine()
            if (line != null && line.isNotEmpty()) {
                val headerParts = line.split(": ")
                headers[headerParts[0]] = headerParts[1]
            }
        } while (line != null && line.isNotEmpty())

        if (method == HttpMethod.POST) {
            val contentLength = headers["Content-Length"]?.toInt() ?: 0
            rawBody = input.bufferedReader().readText().take(contentLength)
        } else {
            rawBody = ""
        }

        val queryIndex = path.indexOf("?")
        if (queryIndex != -1) {
            val queryParams = path.substring(queryIndex + 1).split("&")
            for (param in queryParams) {
                val paramParts = param.split("=")
                params[paramParts[0]] = paramParts[1]
            }
        }
    }
}
