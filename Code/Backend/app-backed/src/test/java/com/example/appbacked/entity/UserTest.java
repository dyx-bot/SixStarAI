package com.example.appbacked.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testGetSetId() {
        // Given
        Long id = 1L;

        // When
        user.setId(id);

        // Then
        assertEquals(id, user.getId());
    }

    @Test
    void testGetSetPassword() {
        // Given
        String password = "testPassword";

        // When
        user.setPassword(password);

        // Then
        assertEquals(password, user.getPassword());
    }

    @Test
    void testGetSetPassword_NullValue() {
        // When
        user.setPassword(null);

        // Then
        assertNull(user.getPassword());
    }

    @Test
    void testGetSetUsername() {
        // Given
        String username = "testUser";

        // When
        user.setUsername(username);

        // Then
        assertEquals(username, user.getUsername());
    }

    @Test
    void testGetSetUsername_NullValue() {
        // When
        user.setUsername(null);

        // Then
        assertNull(user.getUsername());
    }

    @Test
    void testGetSetAvatar() {
        // Given
        String avatar = "avatar.png";

        // When
        user.setAvatar(avatar);

        // Then
        assertEquals(avatar, user.getAvatar());
    }

    @Test
    void testGetSetAvatar_NullValue() {
        // When
        user.setAvatar(null);

        // Then
        assertNull(user.getAvatar());
    }

    @Test
    void testGetSetGrade() {
        // Given
        String grade = "Freshman";

        // When
        user.setGrade(grade);

        // Then
        assertEquals(grade, user.getGrade());
    }

    @Test
    void testGetSetGrade_NullValue() {
        // When
        user.setGrade(null);

        // Then
        assertNull(user.getGrade());
    }

    @Test
    void testGetSetCollege() {
        // Given
        String college = "Computer Science";

        // When
        user.setCollege(college);

        // Then
        assertEquals(college, user.getCollege());
    }

    @Test
    void testGetSetCollege_NullValue() {
        // When
        user.setCollege(null);

        // Then
        assertNull(user.getCollege());
    }

    @Test
    void testGetSetRole() {
        // Given
        String role = "admin";

        // When
        user.setRole(role);

        // Then
        assertEquals(role, user.getRole());
    }

    @Test
    void testGetSetRole_NullValue() {
        // When
        user.setRole(null);

        // Then
        assertNull(user.getRole());
    }

    @Test
    void testNoArgsConstructor() {
        // When
        User userEntity = new User();

        // Then
        assertNotNull(userEntity);
        assertNull(userEntity.getId());
        assertNull(userEntity.getPassword());
        assertNull(userEntity.getUsername());
        assertNull(userEntity.getAvatar());
        assertNull(userEntity.getGrade());
        assertNull(userEntity.getCollege());
        assertNull(userEntity.getRole());
    }

    @Test
    void testAllArgsConstructor() {
        // Given
        Long id = 1L;
        String password = "testPassword";
        String username = "testUser";
        String avatar = "avatar.png";
        String grade = "Freshman";
        String college = "Computer Science";

        // When
        User userEntity = new User(id, password, username, avatar, grade, college);

        // Then
        assertNotNull(userEntity);
        assertEquals(id, userEntity.getId());
        assertEquals(password, userEntity.getPassword());
        assertEquals(username, userEntity.getUsername());
        assertEquals(avatar, userEntity.getAvatar());
        assertEquals(grade, userEntity.getGrade());
        assertEquals(college, userEntity.getCollege());
        assertNull(userEntity.getRole()); // role not set in constructor
    }

    @Test
    void testToString() {
        // Given
        user.setId(1L);
        user.setUsername("testUser");
        user.setRole("admin");

        // When
        String toStringResult = user.toString();

        // Then
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("User{"));
        assertTrue(toStringResult.contains("id=1"));
        assertTrue(toStringResult.contains("username='testUser'"));
        assertTrue(toStringResult.contains("role='admin'"));
    }

    @Test
    void testEquals_SameObject() {
        // When & Then
        assertEquals(user, user);
    }

    @Test
    void testEquals_EqualObjects() {
        // Given
        User other = new User();
        other.setId(1L);
        user.setId(1L);

        // When & Then
        assertEquals(user, other);
    }

    @Test
    void testEquals_DifferentObjects() {
        // Given
        User other = new User();
        other.setId(1L);
        user.setId(2L);

        // When & Then
        assertNotEquals(user, other);
    }

    @Test
    void testEquals_NullObject() {
        // When & Then
        assertNotEquals(null, user);
    }

    @Test
    void testEquals_DifferentClass() {
        // Given
        String other = "other";

        // When & Then
        assertNotEquals(user, other);
    }

    @Test
    void testHashCode_SameObject() {
        // When
        int hashCode1 = user.hashCode();
        int hashCode2 = user.hashCode();

        // Then
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCode_EqualObjects() {
        // Given
        User other = new User();
        other.setId(1L);
        user.setId(1L);

        // When
        int hashCode1 = user.hashCode();
        int hashCode2 = other.hashCode();

        // Then
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testAllFieldsTogether() {
        // Given
        Long id = 1L;
        String password = "testPassword";
        String username = "testUser";
        String avatar = "avatar.png";
        String grade = "Senior";
        String college = "Engineering";
        String role = "user";

        // When
        user.setId(id);
        user.setPassword(password);
        user.setUsername(username);
        user.setAvatar(avatar);
        user.setGrade(grade);
        user.setCollege(college);
        user.setRole(role);

        // Then
        assertEquals(id, user.getId());
        assertEquals(password, user.getPassword());
        assertEquals(username, user.getUsername());
        assertEquals(avatar, user.getAvatar());
        assertEquals(grade, user.getGrade());
        assertEquals(college, user.getCollege());
        assertEquals(role, user.getRole());
    }
}
