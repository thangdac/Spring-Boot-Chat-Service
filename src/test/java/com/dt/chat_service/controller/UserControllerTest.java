package com.dt.chat_service.controller;

import com.dt.chat_service.dto.request.UserUpdateRequest;
import com.dt.chat_service.dto.response.UserResponse;
import com.dt.chat_service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = UserController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
        },
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {
                                com.dt.chat_service.Security.JwtAuthFilter.class
                                // nếu có SecurityConfig thì thêm luôn:
                                // com.dt.chat_service.Security.SecurityConfig.class
                        }
                )
        }
)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserService userService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Autowired
    private ObjectMapper objectMapper;

    // 🔹 Test GET /api/users
    @Test
    void getAllUsers_shouldReturnList() throws Exception {
        UserResponse user = UserResponse.builder()
                .username("thang")
                .build();

        Mockito.when(userService.getAllUsers())
                .thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0].username").value("thang"));
    }

    @Test
    void getAllUsers_emptyList() throws Exception {
        Mockito.when(userService.getAllUsers())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isEmpty());
    }

    // 🔹 Test GET /api/users/search
    @Test
    void getUserByUsername_shouldReturnUser() throws Exception {
        UserResponse user = UserResponse.builder()
                .username("thang")
                .build();

        Mockito.when(userService.getUserByUsername("thang"))
                .thenReturn(user);

        mockMvc.perform(get("/api/users/search")
                        .param("username", "thang"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.username").value("thang"));
    }

    @Test
    void getUserByUsername_notFound() throws Exception {
        Mockito.when(userService.getUserByUsername("abc"))
                .thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(get("/api/users/search")
                        .param("username", "abc"))
                .andExpect(status().isInternalServerError());
    }

    // 🔹 Test GET /api/users/{id}
    @Test
    void getUserById_shouldReturnUser() throws Exception {
        UUID id = UUID.randomUUID();

        UserResponse user = UserResponse.builder()
                .id(id)
                .username("thang")
                .build();

        Mockito.when(userService.getUserById(id))
                .thenReturn(user);

        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.username").value("thang"));
    }

    // 🔹 Test PUT /api/users/{id}
    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        UUID id = UUID.randomUUID();

        UserUpdateRequest request = new UserUpdateRequest();
        request.setUsername("newName");
        request.setEmail("test@gmail.com");

        UserResponse response = UserResponse.builder()
                .id(id)
                .username("newName")
                .build();

        Mockito.when(userService.updateUser(Mockito.eq(id), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.username").value("newName"));
    }

    @Test
    void getUserById_notFound() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.when(userService.getUserById(id))
                .thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isInternalServerError());
    }

    // 🔹 Test DELETE /api/users/{id}
    @Test
    void deleteUser_shouldReturnSuccess() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.doNothing().when(userService).deleteUser(id);

        mockMvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User deleted successfully"));
    }
}