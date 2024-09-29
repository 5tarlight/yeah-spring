package io.yeahx4.model

data class User(
    val userId: String,
    val password: String,
    val name: String,
    val email: String
) {
    override fun toString(): String {
        return "User(userId='$userId', password='$password', name='$name', email='$email')"
    }

    companion object {
        fun fromParams(params: Map<String, String>): User? {
            val userId = params["userId"] ?: return null
            val password = params["password"] ?: return null
            val name = params["name"] ?: return null
            val email = params["email"] ?: return null

            return User(userId, password, name, email)
        }
    }
}
