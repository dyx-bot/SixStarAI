package com.example.appbacked.service;

import com.example.appbacked.entity.User;
import com.example.appbacked.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"); // SHA256 of "password123"
        testUser.setUsername("testUser");
        testUser.setAvatar("testAvatar");
        testUser.setGrade("2021");
        testUser.setCollege("Computer Science");
        testUser.setRole("USER");
    }

    @Test
    void loginWithValidCredentialsShouldReturnTrue() {
        User inputUser = new User();
        inputUser.setId(1L);
        inputUser.setPassword("password123");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        boolean result = userService.login(inputUser);

        assertTrue(result);
        assertEquals("testUser", inputUser.getUsername());
        assertEquals("testAvatar", inputUser.getAvatar());
        assertEquals("2021", inputUser.getGrade());
        assertEquals("Computer Science", inputUser.getCollege());
        assertEquals("USER", inputUser.getRole());
        verify(userRepository).findById(1L);
    }

    @Test
    void loginWithWrongPasswordShouldReturnFalse() {
        User inputUser = new User();
        inputUser.setId(1L);
        inputUser.setPassword("wrongPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        boolean result = userService.login(inputUser);

        assertFalse(result);
        verify(userRepository).findById(1L);
    }

    @Test
    void loginWithNonExistentUserShouldReturnFalse() {
        User inputUser = new User();
        inputUser.setId(999L);
        inputUser.setPassword("password123");

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        boolean result = userService.login(inputUser);

        assertFalse(result);
        verify(userRepository).findById(999L);
    }

    @Test
    void loginWithNullUserShouldReturnFalse() {
        boolean result = userService.login(null);
        assertFalse(result);
    }

    @Test
    void loginWithNullIdShouldReturnFalse() {
        User inputUser = new User();
        inputUser.setPassword("password123");

        boolean result = userService.login(inputUser);
        assertFalse(result);
    }

    @Test
    void loginWithNullPasswordShouldReturnFalse() {
        User inputUser = new User();
        inputUser.setId(1L);

        boolean result = userService.login(inputUser);
        assertFalse(result);
    }

    @Test
    void loginWithEmptyPasswordShouldReturnFalse() {
        User inputUser = new User();
        inputUser.setId(1L);
        inputUser.setPassword("");

        boolean result = userService.login(inputUser);
        assertFalse(result);
    }

    @Test
    void loginShouldSetDefaultUsernameAndAvatarWhenNotPresent() {
        User inputUser = new User();
        inputUser.setId(1L);
        inputUser.setPassword("password123");

        User dbUser = new User();
        dbUser.setId(1L);
        dbUser.setPassword("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");
        dbUser.setUsername(null);
        dbUser.setAvatar(null);
        dbUser.setGrade("2021");
        dbUser.setCollege("Computer Science");
        dbUser.setRole("USER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(dbUser));
        when(userRepository.save(any(User.class))).thenReturn(dbUser);

        boolean result = userService.login(inputUser);

        assertTrue(result);
        assertEquals("用户1", inputUser.getUsername());
        assertNotNull(inputUser.getAvatar());
        assertTrue(inputUser.getAvatar().contains("dicebear"));
        assertEquals("2021", inputUser.getGrade());
        assertEquals("Computer Science", inputUser.getCollege());
        assertEquals("USER", inputUser.getRole());
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUserWithValidDataShouldReturnUpdatedUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.updateUser(1L, "newUsername", "newAvatar");

        assertNotNull(result);
        assertEquals("newUsername", result.getUsername());
        assertEquals("newAvatar", result.getAvatar());
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUserWithNonExistentUserShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(1L, "newUsername", "newAvatar");
        });
        verify(userRepository).findById(1L);
    }

    @Test
    void updateUserWithInvalidUsernameLengthShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(1L, "a", "newAvatar");
        });
        verify(userRepository).findById(1L);
    }

    @Test
    void updateUserWithSensitiveWordShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(1L, "adminUser", "newAvatar");
        });
        verify(userRepository).findById(1L);
    }

    @Test
    void updateUserWithTooLongAvatarUrlShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        String longAvatarUrl = "http://example.com/" + "a".repeat(500);
        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(1L, "newUsername", longAvatarUrl);
        });
        verify(userRepository).findById(1L);
    }

    @Test
    void updateUserWithNoFieldsToUpdateShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(1L, null, null);
        });
        verify(userRepository).findById(1L);
    }

    @Test
    void updateNameWithValidDataShouldReturnTrue() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        boolean result = userService.updateName(1L, "newName");

        assertTrue(result);
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateNameWithNonExistentUserShouldReturnFalse() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = userService.updateName(1L, "newName");

        assertFalse(result);
        verify(userRepository).findById(1L);
    }

    @Test
    void updateAvatarShouldProcessAndSaveAvatar() {
        String base64Avatar = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8/5+hHgAHggJ/PchI7wAAAABJRU5ErkJggg==";

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        assertDoesNotThrow(() -> {
            User result = userService.updateAvatar(1L, base64Avatar);
            assertNotNull(result);
        });

        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void isAdminWithAdminUserShouldReturnTrue() {
        User adminUser = new User();
        adminUser.setId(1L);
        adminUser.setRole("ADMIN");

        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));

        boolean result = userService.isAdmin(1L);

        assertTrue(result);
        verify(userRepository).findById(1L);
    }

    @Test
    void isAdminWithRegularUserShouldReturnFalse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        boolean result = userService.isAdmin(1L);

        assertFalse(result);
        verify(userRepository).findById(1L);
    }

    @Test
    void isAdminWithNonExistentUserShouldReturnFalse() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = userService.isAdmin(1L);

        assertFalse(result);
        verify(userRepository).findById(1L);
    }

    @Test
    void getAllUsersShouldReturnUserList() {
        List<User> users = new ArrayList<>();
        users.add(testUser);
        users.add(new User());

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void deleteUserShouldCallRepositoryDelete() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void updateUserRoleShouldUpdateUserRole() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        userService.updateUserRole(1L, "ADMIN");

        assertEquals("ADMIN", testUser.getRole());
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUserRoleWithNonExistentUserShouldNotSave() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        userService.updateUserRole(1L, "ADMIN");

        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any());
    }
}
