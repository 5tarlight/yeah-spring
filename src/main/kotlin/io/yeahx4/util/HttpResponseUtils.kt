package io.yeahx4.util

import io.yeahx4.webserver.RequestHeader
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException

class HttpResponseUtils {
    companion object {
        private val log = LoggerFactory.getLogger(HttpResponseUtils::class.java)

        fun responseCode(dos: DataOutputStream, code: Int, message: String) {
            try {
                dos.writeBytes("HTTP/1.1 $code $message \r\n")
                dos.writeBytes("Content-Type: text/html;charset=utf-8 \r\n")
                dos.writeBytes("\r\n")
                dos.writeBytes("<html><body><h1>$code $message</h1></body></html>\r\n")
                dos.flush()
            } catch (e: IOException) {
                log.error(e.message)
            }
        }

        fun responseFile(dos: DataOutputStream, path: String, body: ByteArray) {
            try {
                dos.writeBytes("HTTP/1.1 200 OK \r\n")
                dos.writeBytes("Content-Type: ${HttpRequestUtils.getContentType(path)};charset=utf-8 \r\n")
                dos.writeBytes("Content-Length: ${body.size} \r\n")
                dos.writeBytes("\r\n")
                dos.write(body, 0, body.size)
                dos.writeBytes("\r\n")
                dos.flush()
            } catch (e: IOException) {
                log.error(e.message)
            }
        }

        fun redirect(dos: DataOutputStream, location: String) {
            try {
                dos.writeBytes("HTTP/1.1 302 Found \r\n")
                dos.writeBytes("Location: $location \r\n")
                dos.writeBytes("\r\n")
                dos.flush()
            } catch (e: IOException) {
                log.error(e.message)
            }
        }
    }

    fun readBodyRaw(br: BufferedReader, header: RequestHeader): String {
        val length = header.headers["Content-Length"]?.toInt() ?: 0
        return IoUtils.readData(br, length)
    }

    fun readBody(br: BufferedReader, header: RequestHeader): Map<String, String> {
        val length = header.headers["Content-Length"]?.toInt() ?: 0
        val contentType = header.headers["Content-Type"] ?: "text/plain"
        return HttpRequestUtils.parseBody(readBodyRaw(br, header), contentType)
    }
}