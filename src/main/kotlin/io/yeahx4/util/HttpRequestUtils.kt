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
                .associate { it!!.first to it.second }
        }

        private fun getKeyValue(keyValue: String, regex: String): Pair<String, String>? {
            if (Strings.isNullOrEmpty(keyValue))
                return null

            val tokens = keyValue.split(regex)
            if (tokens.size != 2)
                return null

            return Pair(tokens[0].trim(), tokens[1].trim())
        }

        fun parseHeader(header: String): Pair<String, String> {
            return getKeyValue(header, ":")
                ?: throw IllegalArgumentException("Invalid Header: $header")
        }
    }
}