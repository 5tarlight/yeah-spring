package io.yeahx4.db

import io.yeahx4.model.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DatabaseTest {

    @BeforeEach
    fun setUp() {
        // Clear the database before each test
        Database.clear()
    }

    @Test
    fun `test addUser`() {
        val user = User("user1", "password123", "John Doe", "john.doe@example.com")
        Database.addUser(user)
        val retrievedUser = Database.findUserById("user1")
        assertNotNull(retrievedUser)
        assertEquals(user, retrievedUser)
    }

    @Test
    fun `test findUserById`() {
        val user = User("user2", "password456", "Jane Doe", "jane.doe@example.com")
        Database.addUser(user)
        val retrievedUser = Database.findUserById("user2")
        assertNotNull(retrievedUser)
        assertEquals(user, retrievedUser)
    }

    @Test
    fun `test findAll`() {
        val user1 = User("user1", "password123", "John Doe", "john.doe@example.com")
        val user2 = User("user2", "password456", "Jane Doe", "jane.doe@example.com")
        Database.addUser(user1)
        Database.addUser(user2)
        val allUsers = Database.findAll()
        assertEquals(2, allUsers.size)
        assertTrue(allUsers.contains(user1))
        assertTrue(allUsers.contains(user2))
    }
}