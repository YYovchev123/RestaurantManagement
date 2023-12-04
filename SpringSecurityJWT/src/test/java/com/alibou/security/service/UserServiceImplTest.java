package com.alibou.security.service;

import com.alibou.security.config.JwtService;
import com.alibou.security.exception.RecordNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository, jwtService);
    }

    @Test
    public void verifySave() {
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
    public void verifyFindAll() {
        userService.findAll();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void verifyFindById() {
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
    public void verifyFindByEmail() {
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
    public void verifyDeleteById() {
        long id = 1L;
        User user = User.builder()
                .id(id)
                .build();
        userService.deleteById(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    public void verifyGetEmailFromToken() {
        String email = "test@email.com";
        String token = "tokenTest134";
        User user = User.builder()
                .email(email)
                .build();
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        userService.getEmailFromToken(token);
        verify(userRepository, times(1)).findByEmail(email);
        assertEquals(user.getEmail(), email);
    }

    @Test
    public void verifyFindByIdThrowsException() {
        String exceptionMessage = "User with id: 1 not found!";
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            userService.findById(1L);
        });
        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    public void verifyFindByEmailThrowsException() {
        String exceptionMessage = "User with email: test@email.com not found!";
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            userService.findByEmail("test@email.com");
        });
        assertEquals(exceptionMessage, exception.getMessage());
    }
}
