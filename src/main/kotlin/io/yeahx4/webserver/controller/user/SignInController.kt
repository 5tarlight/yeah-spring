package io.yeahx4.webserver.controller.user

import io.yeahx4.util.HttpResponseUtils
import io.yeahx4.util.ResponseBuilder
import io.yeahx4.webserver.RequestHeader
import io.yeahx4.webserver.controller.RequestController
import io.yeahx4.webserver.service.UserService
import java.io.BufferedReader
import java.io.DataOutputStream

class SignInController : RequestController {
    private val userService = UserService()

    override fun response(
        req: BufferedReader,
        res: DataOutputStream,
        header: RequestHeader,
        body: Map<String, String>
    ): Int {
        if (!body.containsKey("userId") || !body.containsKey("password")) {
            HttpResponseUtils.redirect(res, "/user/failed-login.html")
            return 400
        }

        val user = userService.signIn(body["userId"]!!, body["password"]!!)
        if (user == null) {
            HttpResponseUtils.redirect(res, "/user/failed-login.html")

            return 401
        } else {
            ResponseBuilder(res, 302, "Found")
                .header("Location", "/index.html")
                .header("Set-Cookie", "logined=true")
                .build()
            return 302
        }
    }
}