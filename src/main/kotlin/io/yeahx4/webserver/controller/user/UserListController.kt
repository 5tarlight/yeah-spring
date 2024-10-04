package io.yeahx4.webserver.controller.user

import io.yeahx4.db.Database
import io.yeahx4.util.HttpRequestUtils
import io.yeahx4.util.ResponseBuilder
import io.yeahx4.webserver.RequestHeader
import io.yeahx4.webserver.controller.RequestController
import java.io.BufferedReader
import java.io.DataOutputStream

class UserListController : RequestController {
    override fun response(
        req: BufferedReader,
        res: DataOutputStream,
        header: RequestHeader,
        body: Map<String, String>
    ): Int {
        val cookie = header.headers["Cookie"]

        val notLoggedInRes = ResponseBuilder(res, 302, "Found")
            .header("Location", "/login.html")

        if (cookie == null) {
            notLoggedInRes.build()
            return 302
        }

        val cookies = HttpRequestUtils.parseCookies(cookie)

        if (cookies["logined"] != "true") {
            notLoggedInRes.build()
            return 302
        }

        ResponseBuilder(res, 200, "OK")
            .header("Content-Type", "text/html")
            .body(buildHTML().toByteArray())
            .build()

        return 200
    }

    private fun buildHTML(): String {
        val users = Database.findAll()

        val body = StringBuilder("<!DOCTYPE html>")
            .append("<html>")
            .append("<head>")
            .append("<title>Users</title>")
            .append("</head>")
            .append("<body>")
            .append("<h1>Users</h1>")
            .append("<ul>")

        users.forEach { user ->
            body.append("<li>${user.userId} - ${user.name}</li>")
        }

        body.append("</ul>")
            .append("</body>")
            .append("</html>")

        return body.toString()
    }
}