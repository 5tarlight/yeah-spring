package io.yeahx4.webserver

import io.yeahx4.util.HttpRequestUtils
import java.io.BufferedReader

class RequestHeader {
    val method: HttpMethod
    val path: String
    val version: String
    val headers: Map<String, String>

    constructor(bufferedReader: BufferedReader) {
        val requestLine = bufferedReader.readLine().split(" ")
        method = HttpMethod.valueOf(requestLine[0])
        path = requestLine[1]
        version = requestLine[2]

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
