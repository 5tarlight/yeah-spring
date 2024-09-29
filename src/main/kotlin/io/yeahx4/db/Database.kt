package io.yeahx4.db

import io.yeahx4.model.User

class Database {
    companion object {
        private val users = mutableMapOf<String, User>()

        fun addUser(user: User) {
            users[user.userId] = user
        }

        fun findUserById(userId: String): User? {
            return users[userId]
        }

        fun findAll(): List<User> {
            return users.values.toList()
        }
    }
}
