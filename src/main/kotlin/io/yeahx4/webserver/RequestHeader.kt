package io.yeahx4.webserver

data class RequestHeader(
    val method: String,
    val path: String,
    val version: String,
    val headers: Map<String, String>
) {

}
