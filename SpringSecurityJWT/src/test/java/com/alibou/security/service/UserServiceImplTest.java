package com.alibou.security.service;

import com.alibou.security.config.JwtService;
import com.alibou.security.user.model.Role;
import com.alibou.security.user.model.User;
import com.alibou.security.user.repository.UserRepository;
import com.alibou.security.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository, jwtService);
    }

    @Test
    public void testSave() {
        User user = User.builder()
                .firstname("FistTest")
                .lastname("LastTest")
                .email("test@email.com")
                .password("testPassword")
                .role(Role.OWNER)
                .build();

        userService.save(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testFindAll() {
        userService.findAll();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        long id = 1L;
        User user = User.builder()
                .id(id)
                .build();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        userService.findById(id);
        verify(userRepository, times(1)).findById(id);
        assertEquals(id, user.getId());
    }

    @Test
    public void testFindByEmail() {
        String email = "test@email.com";
        User user = User.builder()
                .email(email)
                .build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        userService.findByEmail(email);
        verify(userRepository, times(1)).findByEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testDeleteById() {
        long id = 1L;
        User user = User.builder()
                .id(id)
                .build();
        userService.deleteById(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    public void testGetEmailFromToken() {
        String email = "test@email.com";
        String authorizationHeader = "'Bearer tokenTest134";
        String token = "tokenTest134";
        User user = User.builder()
                .email(email)
                .build();
//        when(authorizationHeader.replace("Bearer ", "")).thenReturn(token);
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userService.findByEmail(email)).thenReturn(user);
        userService.getEmailFromToken(authorizationHeader);
        verify(userService, times(1)).findByEmail(email);
    }
}
