package com.example.controller;

import com.example.config.SecurityConfig;
import com.example.dto.PackageResponse;
import com.example.dto.PageResponse;
import com.example.entity.Package;
import com.example.security.UserDetailsServiceCustom;
import com.example.service.PackageService;
import com.example.utils.JwtFilter;
import com.example.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PackageController.class)
@Import({SecurityConfig.class, JwtFilter.class})
class PackageControllerTest {
    @Autowired private MockMvc mvc;
    @MockitoBean private PackageService packages;
    @MockitoBean private UserDetailsServiceCustom users;
    @MockitoBean private JwtUtil jwt;

    @BeforeEach
    void setUp() {
        when(jwt.extractUsername(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwt.validateToken(anyString(), any())).thenReturn(true);
        for (String role : List.of("ADMIN", "RECRUIT", "USER")) {
            when(users.loadUserByUsername(role)).thenReturn(
                    new User(role, "password", List.of(new SimpleGrantedAuthority("ROLE_" + role))));
        }
        Package item = new Package();
        item.setId(1);
        item.setName("Basic");
        item.setPrice(new BigDecimal("100000"));
        item.setCredits(10);
        PackageResponse response = new PackageResponse(item);
        when(packages.getAll(any(Pageable.class))).thenReturn(new PageResponse<>(List.of(response), 0, 10, 1, 1, true));
        when(packages.getById(1)).thenReturn(response);
        when(packages.create(any())).thenReturn(response);
        when(packages.update(eq(1), any())).thenReturn(response);
    }

    @Test
    void recruiterAndAdminCanReadButOtherUsersCannot() throws Exception {
        mvc.perform(get("/api/v1/packages").header("Authorization", "Bearer RECRUIT"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.content[0].name").value("Basic"));
        mvc.perform(get("/api/v1/packages/1").header("Authorization", "Bearer ADMIN"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1));
        mvc.perform(get("/api/v1/packages").header("Authorization", "Bearer USER"))
                .andExpect(status().isForbidden());
        mvc.perform(get("/api/v1/packages")).andExpect(status().is4xxClientError());
    }

    @Test
    void onlyAdminCanCreateUpdateAndDelete() throws Exception {
        String body = "{\"name\":\"Basic\",\"price\":100000,\"credits\":10}";
        mvc.perform(post("/api/v1/packages").header("Authorization", "Bearer ADMIN")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1));
        mvc.perform(put("/api/v1/packages/1").header("Authorization", "Bearer ADMIN")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());
        mvc.perform(delete("/api/v1/packages/1").header("Authorization", "Bearer ADMIN"))
                .andExpect(status().isNoContent());
        mvc.perform(post("/api/v1/packages").header("Authorization", "Bearer RECRUIT")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isForbidden());
        mvc.perform(put("/api/v1/packages/1").header("Authorization", "Bearer RECRUIT")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isForbidden());
        mvc.perform(delete("/api/v1/packages/1").header("Authorization", "Bearer RECRUIT"))
                .andExpect(status().isForbidden());
    }

    @Test
    void rejectsInvalidInput() throws Exception {
        mvc.perform(post("/api/v1/packages").header("Authorization", "Bearer ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\", \"price\":-10, \"credits\":-1}"))
                .andExpect(status().isBadRequest());
        verify(packages, never()).create(any());
    }
}
