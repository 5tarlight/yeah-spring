package io.yeahx4.webserver

import org.slf4j.LoggerFactory
import java.net.ServerSocket
import java.net.Socket

class WebServer {
    companion object {
        private val log = LoggerFactory.getLogger(WebServer::class.java)
        private val DEFAULT_PORT = 8080

        fun start(args: Array<String>) {
            val port = if (args.isNotEmpty()) args[0].toInt() else DEFAULT_PORT

            log.warn("Hello?")
            ServerSocket(port).use { listenSocket ->
                log.info("WebServer started at $port port")

                var connection: Socket?
                while (true) {
                    connection = listenSocket.accept()
                    RequestHandler(connection).start()
                }
            }
        }
    }
}