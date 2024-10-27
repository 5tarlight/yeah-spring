package io.yeahx4.webserver.service

import io.yeahx4.db.Database
import io.yeahx4.model.User
import org.slf4j.LoggerFactory

class UserService {
    private val log = LoggerFactory.getLogger(UserService::class.java)

    fun signUp(user: User): Pair<Int, String> {
        if (Database.findUserById(user.userId) != null) {
            log.warn("User already exists\tuserId : ${user.userId}")
            return Pair(409, "Conflict")
        }

        Database.addUser(user)
        log.info("User created\tuserId : ${user.userId},\tname : ${user.name}")
        return Pair(201, "Created")
    }

    fun signIn(userId: String, password: String): User? {
        val user = Database.findUserById(userId) ?: return null
        return if (user.password == password) user else null
    }
}
