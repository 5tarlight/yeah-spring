package io.yeahx4.webserver

import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.net.Socket
import java.nio.file.Files

class RequestHandler(private val connection: Socket) : Thread() {
    private val log = LoggerFactory.getLogger(RequestHandler::class.java);

    override fun run() {
        log.info("New client connect! Connected IP : ${connection.inetAddress}, Port : ${connection.port}")

        try {
            connection.inputStream.use { input ->
                connection.outputStream.use { output ->
                    val dos = DataOutputStream(output)
                    val br = BufferedReader(InputStreamReader(DataInputStream(input)))
                    val header = RequestHeader(br)

                    log.info("${header.method} ${header.path}")

                    val body = readFile(header.path)

                    if (body == null) {
                        response404(dos)
                        return
                    }

                    response200Header(dos, body.size)
                    responseBody(dos, body)
                }
            }
        } catch (e: IOException) {
            log.error(e.message)
        } finally {
            try {
                connection.close()
            } catch (e: IOException) {
                log.error("Failed to close socket: ${e.message}")
            }
        }
    }

    private fun readFile(path: String): ByteArray? {
        val file = File("./webapp$path")
        if (!file.exists() || !file.isFile || !file.canRead()) {
            return null
        }

        return Files.readAllBytes(file.toPath())
    }

    private fun response404(dos: DataOutputStream) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n")
            dos.writeBytes("Content-Type: text/html;charset=utf-8 \r\n")
            dos.writeBytes("\r\n")
            dos.writeBytes("<html><body><h1>Not Found</h1></body></html>\r\n")
            dos.flush()
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
