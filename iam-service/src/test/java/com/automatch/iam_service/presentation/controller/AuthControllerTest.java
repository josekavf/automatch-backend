package com.automatch.iam_service.presentation.controller;

import com.automatch.iam_service.application.dto.request.LoginRequest;
import com.automatch.iam_service.application.dto.request.RegisterUserRequest;
import com.automatch.iam_service.application.dto.response.AuthResponse;
import com.automatch.iam_service.application.usecase.AuthenticateUserUseCase;
import com.automatch.iam_service.application.usecase.RegisterUserUseCase;
import com.automatch.iam_service.domain.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegisterUserUseCase registerUserUseCase;

    @MockBean
    private AuthenticateUserUseCase authenticateUserUseCase;

    @Test
    void register_ShouldReturnCreated() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setRole(com.automatch.iam_service.domain.model.Role.CLIENT);

        when(registerUserUseCase.execute(any())).thenReturn(User.builder().email("test@example.com").build());

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void login_ShouldReturnOk() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(authenticateUserUseCase.execute(any())).thenReturn(AuthResponse.builder().token("token").build());

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }
}
