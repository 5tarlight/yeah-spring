package io.yeahx4.util

import org.slf4j.LoggerFactory
import java.io.DataOutputStream
import java.io.IOException

class HttpResponseUtils {
    companion object {
        private val log = LoggerFactory.getLogger(HttpResponseUtils::class.java)

        fun responseCode(dos: DataOutputStream, code: Int, message: String) {
            try {
                ResponseBuilder(dos, code, message)
                    .header("Content-Type", "text/html;charset=utf-8")
                    .body("<html><body><h1>$code $message</h1></body></html>".toByteArray())
                    .build()
            } catch (e: IOException) {
                log.error(e.message)
            }
        }

        fun responseFile(dos: DataOutputStream, path: String, body: ByteArray) {
            try {
                ResponseBuilder(dos, 200, "OK")
                    .header("Content-Type", HttpRequestUtils.getContentType(path))
                    .header("Content-Length", body.size.toString())
                    .body(body)
                    .build()
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
}