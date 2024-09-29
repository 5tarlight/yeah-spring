package io.yeahx4.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UserTest {
    @Test
    fun `test user creation`() {
        val user = User("user1", "password123", "John Doe", "john.doe@example.com")
        assertEquals("user1", user.userId)
        assertEquals("password123", user.password)
        assertEquals("John Doe", user.name)
        assertEquals("john.doe@example.com", user.email)
    }

    @Test
    fun `test toString method`() {
        val user = User("user1", "password123", "John Doe", "john.doe@example.com")
        val expectedString = "User(userId='user1', password='password123', name='John Doe', email='john.doe@example.com')"
        assertEquals(expectedString, user.toString())
    }

    @Test
    fun `test equals and hashCode methods`() {
        val user1 = User("user1", "password123", "John Doe", "john.doe@example.com")
        val user2 = User("user1", "password123", "John Doe", "john.doe@example.com")
        assertEquals(user1, user2)
        assertEquals(user1.hashCode(), user2.hashCode())
    }
}
