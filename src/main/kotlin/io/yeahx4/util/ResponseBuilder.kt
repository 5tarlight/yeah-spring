package io.yeahx4.util

import java.io.DataOutputStream

class ResponseBuilder {
    private val res: DataOutputStream
    private val statusCode: Int
    private val statusMessage: String
    private var body: ByteArray? = null
    private val headers = mutableMapOf<String, String>()

    constructor(res: DataOutputStream, statusCode: Int, statusMessage: String) {
        this.res = res
        this.statusCode = statusCode
        this.statusMessage = statusMessage
    }

    fun body(body: ByteArray): ResponseBuilder {
        this.body = body
        return this
    }

    fun header(header: String, value: String): ResponseBuilder {
        headers[header] = value
        return this
    }

    fun build() {
        res.writeBytes("HTTP/1.1 $statusCode $statusMessage\r\n")
        headers.forEach { (header, value) ->
            res.writeBytes("$header: $value\r\n")
        }
        res.writeBytes("\r\n")
        body?.let {
            res.write(it, 0, it.size)
        }
        res.flush()
    }
}
