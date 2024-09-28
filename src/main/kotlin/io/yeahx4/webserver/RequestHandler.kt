package io.yeahx4.webserver

import org.slf4j.LoggerFactory
import java.io.DataOutputStream
import java.io.IOException
import java.net.Socket

class RequestHandler(private val connection: Socket) : Thread() {
    private val log = LoggerFactory.getLogger(RequestHandler::class.java);

    override fun run() {
        log.info("New client connect! Connected IP : ${connection.inetAddress}, Port : ${connection.port}")

        try {
            connection.inputStream.use { input ->
                connection.outputStream.use { output ->
                    val dos = DataOutputStream(output)
                    val body = "Hello, World!".toByteArray(Charsets.UTF_8)
                    response200Header(dos, body.size)
                    responseBody(dos, body)
                }
            }
        } catch (e: IOException) {
            log.error(e.message)
        }
    }

    private fun response200Header(dos: DataOutputStream, length: Int) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n")
            dos.writeBytes("Content-Type: text/html;charset=utf-8 \r\n")
            dos.writeBytes("Content-Length: $length \r\n")
            dos.writeBytes("\r\n")
        } catch (e: IOException) {
            log.error(e.message)
        }
    }

    private fun responseBody(dos: DataOutputStream, body: ByteArray) {
        try {
            dos.write(body, 0, body.size)
            dos.writeBytes("\r\n")
            dos.flush()
        } catch (e: IOException) {
            log.error(e.message)
        }
    }
}
