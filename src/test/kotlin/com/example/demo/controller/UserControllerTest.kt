package com.example.demo.controller

import com.example.demo.model.User
import com.example.demo.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
// import org.junit.jupiter.api.Assertions.assertEquals // Not strictly needed if using jsonPath assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @AfterEach
    fun tearDown() {
        userRepository.deleteAll()
    }

    @Test
    fun `testCreateUserAndGetUserById`() {
        val newUser = User(username = "testuser", email = "test@example.com")

        val resultActions = mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(newUser)))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.username").value(newUser.username))
            .andExpect(jsonPath("$.email").value(newUser.email))
            .andExpect(jsonPath("$.id").exists())

        val responseString = resultActions.andReturn().response.contentAsString
        val createdUser = objectMapper.readValue(responseString, User::class.java)
        val userId = createdUser.id!! // id should not be null as it's generated and checked by jsonPath("$.id").exists()

        mockMvc.perform(get("/api/users/{id}", userId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.username").value(newUser.username))
            .andExpect(jsonPath("$.email").value(newUser.email))
    }

    @Test
    fun `testGetAllUsers`() {
        val user1 = User(username = "userone", email = "user1@example.com")
        val user2 = User(username = "usertwo", email = "user2@example.com")
        userRepository.saveAll(listOf(user1, user2))

        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size()").value(2))
            // Order is not guaranteed by findAll, so check for presence or sort if specific order is needed
            // For simplicity, assuming order or checking for specific elements:
            .andExpect(jsonPath("$[?(@.username == 'userone')]").exists())
            .andExpect(jsonPath("$[?(@.username == 'usertwo')]").exists())
    }
    
    @Test
    fun `testGetUserById_NotFound`() {
        val nonExistentId = 9999L // Use a Long type for ID
        mockMvc.perform(get("/api/users/{id}", nonExistentId))
            .andExpect(status().isNotFound)
    }
}
