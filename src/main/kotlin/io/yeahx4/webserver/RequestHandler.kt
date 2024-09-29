package io.yeahx4.webserver

import io.yeahx4.model.User
import io.yeahx4.util.HttpRequestUtils
import io.yeahx4.util.HttpResponseUtils
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
                            HttpResponseUtils.responseFile(dos, header.path, body)
                            return
                        }

                        if (header.path == "/user/create") {
                            val user = User.fromParams(header.params)
                            responseUserCreate(dos, user, header)
                            return
                        }
                    } else if (header.method == HttpMethod.POST) {
                        val length = header.headers["Content-Length"]?.toInt() ?: 0
                        val contentType = header.headers["Content-Type"] ?: "text/plain"
                        val body = HttpRequestUtils.parseBody(IoUtils.readData(br, length), contentType)

                        val user = User.fromParams(body)
                        responseUserCreate(dos, user, header)
                        return
                    }

                    logReq(header.method, 404, header.path)
                    HttpResponseUtils.responseCode(dos, 404, "Not Found")
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

    private fun responseUserCreate(dos: DataOutputStream, user: User?, header: RequestHeader) {
        if (user == null) {
            logReq(header.method, 400, header.path)
            HttpResponseUtils.responseCode(dos, 400, "Bad Request")
            return
        }

        val (statusCode, statusMessage) = userController.signUp(user)
        logReq(header.method, statusCode, header.path)
        if (statusCode == 201) {
            HttpResponseUtils.redirect(dos, "/index.html")
        } else {
            HttpResponseUtils.responseCode(dos, statusCode, statusMessage)
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
}
