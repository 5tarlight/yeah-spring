package io.yeahx4.util

import com.google.common.base.Strings

class HttpRequestUtils {
    companion object {
        fun parseQueryString(queryString: String): Map<String, String> {
            return parseValues(queryString, "&")
        }

        fun parseCookies(cookieString: String): Map<String, String> {
            return parseValues(cookieString, ";")
        }

        private fun parseValues(values: String, separator: String): Map<String, String> {
            if (Strings.isNullOrEmpty(values))
                return emptyMap()

            val tokens = values.split(separator)
            return tokens.map { t -> getKeyValue(t, "=") }
                .filter { it != null }
                .associate { it!!.first.trim() to it.second.trim() }
        }

        private fun getKeyValue(keyValue: String, regex: String): Pair<String, String>? {
            if (Strings.isNullOrEmpty(keyValue))
                return null

            val tokens = keyValue.split(regex)
            if (tokens.size < 2)
                return null

            return Pair(
                keyValue.slice(0 until tokens[0].length).trim(),
                keyValue.slice(tokens[0].length + 1 until keyValue.length).trim()
            )
        }

        fun parseHeader(header: String): Pair<String, String> {
            return getKeyValue(header, ":")
                ?: throw IllegalArgumentException("Invalid Header: $header")
        }

        private fun parseJson(json: String): Map<String, String> {
            return json.split(",")
                .map { it.split(":") }
                .associate { it[0].trim() to it[1].trim() }
        }

        fun parseBody(body: String, contentType: String): Map<String, String> {
            return when (contentType) {
                "application/x-www-form-urlencoded" -> parseQueryString(body)
                "application/json" -> parseJson(body)
                else -> emptyMap()
            }
        }
    }
}