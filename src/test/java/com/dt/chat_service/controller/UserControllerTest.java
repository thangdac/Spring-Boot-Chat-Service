package com.dt.chat_service.controller;

import com.dt.chat_service.Security.JwtTokenProvider;
import com.dt.chat_service.config.JpaConfig;
import com.dt.chat_service.dto.response.UserResponse;
import com.dt.chat_service.service.CustomUserDetailsService;
import com.dt.chat_service.service.TokenBlacklistService;
import com.dt.chat_service.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = UserController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = JpaConfig.class
                )
        }
)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;
    @MockitoBean
    CustomUserDetailsService customUserDetailsService;
    @MockitoBean
    JwtTokenProvider jwtTokenProvider;
    @MockitoBean
    TokenBlacklistService tokenBlacklistService;

    // ── Không có token ──
    @Test
    void getAllUsers_noToken_returns401() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());
    }

    // ── USER role gọi API ADMIN ──
    @Test
    @WithMockUser(roles = "USER")
    void getAllUsers_userRole_returns403() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden());
    }

    // ── ADMIN role gọi API ADMIN ──
    @Test
    @WithMockUser(authorities = "ADMIN") // dùng authorities vì bạn không có prefix ROLE_
    void getAllUsers_adminRole_returns200() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
    }

    // ── Không token gọi search ──
    @Test
    void getUserByUsername_noToken_returns401() throws Exception {
        mockMvc.perform(get("/api/users/search").param("username", "testuser"))
                .andExpect(status().isUnauthorized());
    }

    // ── Đã auth gọi search ──
    @Test
    @WithMockUser
    void getUserByUsername_authenticated_returns200() throws Exception {
        when(userService.getUserByUsername("testuser"))
                .thenReturn(UserResponse.builder().username("testuser").build());

        mockMvc.perform(get("/api/users/search").param("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.username").value("testuser"));
    }
}
