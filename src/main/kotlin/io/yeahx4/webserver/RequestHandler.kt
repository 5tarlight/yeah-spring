package io.yeahx4.webserver

import io.yeahx4.util.HttpRequestUtils
import io.yeahx4.util.HttpResponseUtils
import io.yeahx4.util.IoUtils
import io.yeahx4.webserver.controller.user.SignInController
import io.yeahx4.webserver.controller.user.SignUpController
import io.yeahx4.webserver.controller.user.UserListController
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
    private val postRoutes = mapOf(
        "/user/create" to SignUpController(),
        "/user/login" to SignInController()
    )
    private val getRoutes = mapOf(
        "/user/list" to UserListController()
    )

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
                        } else if (getRoutes.containsKey(header.path)) {
                            val code = getRoutes[header.path]!!.response(br, dos, header, emptyMap())
                            logReq(header.method, code, header.path)
                            return
                        }
                    } else if (header.method == HttpMethod.POST) {
                        val length = header.headers["Content-Length"]?.toInt() ?: 0
                        val contentType = header.headers["Content-Type"] ?: "text/plain"
                        val body = HttpRequestUtils.parseBody(IoUtils.readData(br, length), contentType)

                        if (postRoutes.containsKey(header.path)) {
                            val code = postRoutes[header.path]?.response(br, dos, header, body)
                            logReq(header.method, code!!, header.path)
                            return
                        }
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
