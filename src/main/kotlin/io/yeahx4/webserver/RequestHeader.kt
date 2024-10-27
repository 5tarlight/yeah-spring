package io.yeahx4.webserver

import io.yeahx4.util.HttpRequestUtils
import io.yeahx4.web.HttpMethod
import java.io.BufferedReader

class RequestHeader {
    val method: HttpMethod
    val path: String
    val version: String
    val headers: Map<String, String>
    val params: Map<String, String>

    constructor(bufferedReader: BufferedReader) {
        val requestLine = bufferedReader.readLine().split(" ")
        method = HttpMethod.valueOf(requestLine[0])
        version = requestLine[2]

        var rawPath = requestLine[1]
        if (rawPath.contains("?")) {
            val pathAndQuery = rawPath.split("\\?".toRegex())
            path = pathAndQuery[0]
            params = HttpRequestUtils.parseQueryString(pathAndQuery[1])
        } else {
            path = rawPath
            params = emptyMap()
        }

        headers = mutableMapOf()
        var line: String? = bufferedReader.readLine()
        while (line != null && line != "") {
            HttpRequestUtils.parseHeader(line).let { (key, value) ->
                headers[key] = value
            }

            line = bufferedReader.readLine()
        }
    }
}
