package com.alibou.security.service;

import com.alibou.security.auth.AuthenticationRequest;
import com.alibou.security.auth.AuthenticationResponse;
import com.alibou.security.auth.AuthenticationService;
import com.alibou.security.auth.RegisterRequest;
import com.alibou.security.config.JwtService;
import com.alibou.security.user.model.Role;
import com.alibou.security.user.model.User;
import com.alibou.security.user.service.UserService;
import com.alibou.security.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    private AuthenticationService authenticationService;
    @Mock
    private UserService userService;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        authenticationService = new AuthenticationService(userService, passwordEncoder, jwtService, authenticationManager);
    }

    @Test
    public void testRegister() {
        // Given
        RegisterRequest request = new RegisterRequest("FirstTest", "LastTest", "text@email.com", "password", Role.OWNER);
        User expectedUser = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password("encodedPassword")
                .role(request.getRole())
                .build();

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userService.save(any(User.class))).thenReturn(expectedUser);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        // When
        AuthenticationResponse response = authenticationService.register(request);

        // Then
        assertNotNull(response);
        assertEquals(expectedUser.getFirstname(), response.getFirstName());
        assertEquals(expectedUser.getLastname(), response.getLastName());
        assertEquals("jwtToken", response.getToken());

        // Verify that userService.save and jwtService.generateToken were called once
        verify(userService, times(1)).save(any(User.class));
        verify(jwtService, times(1)).generateToken(any(User.class));
    }

    @Test
    public void testAuthenticate() {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .email("text@email.com")
                .password("password")
                .build();

        User user = User.builder()
                .firstname("FistTest")
                .lastname("LastTest")
                .email(authenticationRequest.getEmail())
                .password(authenticationRequest.getPassword())
                .role(Role.OWNER)
                .build();

        when(userService.findByEmail("text@email.com")).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);

        verify(authenticationManager, times(1)).authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword()
        ));

        assertNotNull(response);
        assertEquals(user.getFirstname(), response.getFirstName());
        assertEquals(user.getLastname(), response.getLastName());
        assertEquals("jwtToken", response.getToken());
    }
}

