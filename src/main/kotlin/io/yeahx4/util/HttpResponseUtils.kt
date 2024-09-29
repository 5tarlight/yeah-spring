package io.yeahx4.util

import org.slf4j.LoggerFactory
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
    }
}