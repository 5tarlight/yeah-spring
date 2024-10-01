package io.yeahx4.webserver.controller

import io.yeahx4.webserver.RequestHeader
import java.io.BufferedReader
import java.io.DataOutputStream

interface RequestController {
    fun response(
        req: BufferedReader,
        res: DataOutputStream,
        header: RequestHeader,
        body: Map<String, String>
    ): Int
}
