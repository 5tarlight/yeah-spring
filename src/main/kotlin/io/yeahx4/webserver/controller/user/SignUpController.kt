package io.yeahx4.webserver.controller.user

import io.yeahx4.model.User
import io.yeahx4.util.HttpResponseUtils
import io.yeahx4.webserver.RequestHeader
import io.yeahx4.webserver.controller.RequestController
import io.yeahx4.webserver.service.UserService
import java.io.BufferedReader
import java.io.DataOutputStream

class SignUpController : RequestController {
    private val userService = UserService()

    override fun response(
        req: BufferedReader,
        res: DataOutputStream,
        header: RequestHeader,
        body: Map<String, String>
    ) : Int {
        val user = User.fromParams(body)

        if (user == null) {
            HttpResponseUtils.responseCode(res, 400, "Bad Request")
            return 400
        }

        val (statusCode, statusMessage) = userService.signUp(user)
        if (statusCode == 201) {
            HttpResponseUtils.redirect(res, "/index.html")
        } else {
            HttpResponseUtils.responseCode(res, statusCode, statusMessage)
        }

        return statusCode
    }
}