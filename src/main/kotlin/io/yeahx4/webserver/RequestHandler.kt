package io.yeahx4.webserver

import io.yeahx4.model.User
import io.yeahx4.util.HttpRequestUtils
import io.yeahx4.util.IoUtils
import io.yeahx4.webserver.controller.UserController
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
    private val userController = UserController()

    override fun run() {
        try {
            connection.inputStream.use { input ->
                connection.outputStream.use { output ->
                    val dos = DataOutputStream(output)
                    val br = BufferedReader(InputStreamReader(DataInputStream(input)))
                    val header = RequestHeader(br)

                    if (header.method == HttpMethod.GET) {
                        val body = readFile(header.path)

                        if (body != null) {
                            logReq(header.method, 200, header.path)
                            response200Header(dos, body.size)
                            responseBody(dos, body)
                            return
                        }

                        if (header.path == "/user/create") {
                            val user = User.fromParams(header.params)

                            if (user == null) {
                                logReq(header.method, 400, header.path)
                                response400(dos)
                                return
                            }

                            val (statusCode, statusMessage) = userController.signUp(user)
                            logReq(header.method, statusCode, header.path)
                            responseElse(dos, statusCode, statusMessage)
                            return
                        }
                    } else if (header.method == HttpMethod.POST) {
                        val length = header.headers["Content-Length"]?.toInt() ?: 0
                        val contentType = header.headers["Content-Type"] ?: "text/plain"
                        val body = HttpRequestUtils.parseBody(IoUtils.readData(br, length), contentType)

                        val user = User.fromParams(body)

                        if (user == null) {
                            logReq(header.method, 400, header.path)
                            response400(dos)
                            return
                        }

                        val (statusCode, statusMessage) = userController.signUp(user)
                        logReq(header.method, statusCode, header.path)
                        responseElse(dos, statusCode, statusMessage)
                        return
                    }

                    logReq(header.method, 404, header.path)
                    response404(dos)
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

    private fun logReq(method: HttpMethod, code: Int, path: String) {
        log.info("$method\t$code\t$path")
    }

    private fun readFile(path: String): ByteArray? {
        val file = File("./webapp$path")
        if (!file.exists() || !file.isFile || !file.canRead()) {
            return null
        }

        return Files.readAllBytes(file.toPath())
    }

    private fun responseElse(dos: DataOutputStream, code: Int, message: String) {
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

    private fun response400(dos: DataOutputStream) {
        try {
            dos.writeBytes("HTTP/1.1 400 Bad Request \r\n")
            dos.writeBytes("Content-Type: text/html;charset=utf-8 \r\n")
            dos.writeBytes("\r\n")
            dos.writeBytes("<html><body><h1>400 Bad Request</h1></body></html>\r\n")
            dos.flush()
        } catch (e: IOException) {
            log.error(e.message)
        }
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
