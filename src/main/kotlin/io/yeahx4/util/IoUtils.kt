package io.yeahx4.util

import java.io.BufferedReader

class IoUtils {
    companion object {
        /**
         * @param br Request Body 가 시작하는 지점
         * @param length Request Header의 Content-Length 값
         */
        fun readData(br: BufferedReader, length: Int): String {
            val body = CharArray(length)
            br.read(body, 0, length);
            return String(body)
        }
    }
}