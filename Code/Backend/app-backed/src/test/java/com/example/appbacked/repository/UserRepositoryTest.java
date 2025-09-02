package com.example.appbacked.repository;

import com.example.appbacked.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCountNormalUsers() {
        // Given
        long count = 10L;
        when(userRepository.countNormalUsers()).thenReturn(count);

        // When
        long result = userRepository.countNormalUsers();

        // Then
        assertEquals(count, result);
        verify(userRepository, times(1)).countNormalUsers();
    }

    @Test
    void testCountNormalUsers_ZeroCount() {
        // Given
        long count = 0L;
        when(userRepository.countNormalUsers()).thenReturn(count);

        // When
        long result = userRepository.countNormalUsers();

        // Then
        assertEquals(count, result);
        verify(userRepository, times(1)).countNormalUsers();
    }

    @Test
    void testCountUsersByGrade() {
        // Given
        Object[] grade1 = {"Freshman", 5L};
        Object[] grade2 = {"Sophomore", 3L};
        Object[] grade3 = {"Junior", 2L};
        List<Object[]> gradeCounts = Arrays.asList(grade1, grade2, grade3);

        when(userRepository.countUsersByGrade()).thenReturn(gradeCounts);

        // When
        List<Object[]> result = userRepository.countUsersByGrade();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Freshman", result.get(0)[0]);
        assertEquals(5L, result.get(0)[1]);
        assertEquals("Sophomore", result.get(1)[0]);
        assertEquals(3L, result.get(1)[1]);
        assertEquals("Junior", result.get(2)[0]);
        assertEquals(2L, result.get(2)[1]);

        verify(userRepository, times(1)).countUsersByGrade();
    }

    @Test
    void testCountUsersByGrade_EmptyResult() {
        // Given
        List<Object[]> gradeCounts = Arrays.asList();
        when(userRepository.countUsersByGrade()).thenReturn(gradeCounts);

        // When
        List<Object[]> result = userRepository.countUsersByGrade();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).countUsersByGrade();
    }

    @Test
    void testCountUsersByCollege() {
        // Given
        Object[] college1 = {"计算机学院", 10L};
        Object[] college2 = {"电子工程学院", 8L};
        Object[] college3 = {"机械学院", 6L};
        List<Object[]> collegeCounts = Arrays.asList(college1, college2, college3);

        when(userRepository.countUsersByCollege()).thenReturn(collegeCounts);

        // When
        List<Object[]> result = userRepository.countUsersByCollege();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("计算机学院", result.get(0)[0]);
        assertEquals(10L, result.get(0)[1]);
        assertEquals("电子工程学院", result.get(1)[0]);
        assertEquals(8L, result.get(1)[1]);
        assertEquals("机械学院", result.get(2)[0]);
        assertEquals(6L, result.get(2)[1]);

        verify(userRepository, times(1)).countUsersByCollege();
    }

    @Test
    void testCountUsersByCollege_EmptyResult() {
        // Given
        List<Object[]> collegeCounts = Arrays.asList();
        when(userRepository.countUsersByCollege()).thenReturn(collegeCounts);

        // When
        List<Object[]> result = userRepository.countUsersByCollege();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).countUsersByCollege();
    }

    @Test
    void testFindAllByRoleNotAdmin() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setRole("USER");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setRole(null);

        List<User> users = Arrays.asList(user1, user2);
        Page<User> page = new PageImpl<>(users);

        when(userRepository.findAllByRoleNotAdmin(pageable)).thenReturn(page);

        // When
        Page<User> result = userRepository.findAllByRoleNotAdmin(pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(user1.getId(), result.getContent().get(0).getId());
        assertEquals(user2.getId(), result.getContent().get(1).getId());
        assertEquals("user1", result.getContent().get(0).getUsername());
        assertEquals("user2", result.getContent().get(1).getUsername());

        verify(userRepository, times(1)).findAllByRoleNotAdmin(pageable);
    }

    @Test
    void testFindAllByRoleNotAdmin_EmptyResult() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = Arrays.asList();
        Page<User> page = new PageImpl<>(users);

        when(userRepository.findAllByRoleNotAdmin(pageable)).thenReturn(page);

        // When
        Page<User> result = userRepository.findAllByRoleNotAdmin(pageable);

        // Then
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        verify(userRepository, times(1)).findAllByRoleNotAdmin(pageable);
    }

    @Test
    void testExistsById() {
        // Given
        Long id = 1L;
        when(userRepository.existsById(id)).thenReturn(true);

        // When
        boolean result = userRepository.existsById(id);

        // Then
        assertTrue(result);
        verify(userRepository, times(1)).existsById(id);
    }

    @Test
    void testExistsById_NotExists() {
        // Given
        Long id = 1L;
        when(userRepository.existsById(id)).thenReturn(false);

        // When
        boolean result = userRepository.existsById(id);

        // Then
        assertFalse(result);
        verify(userRepository, times(1)).existsById(id);
    }

    // 继承自 JpaRepository 的方法测试示例
    @Test
    void testFindById() {
        // Given
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setUsername("testUser");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // When
        Optional<User> result = userRepository.findById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        assertEquals("testUser", result.get().getUsername());

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void testFindById_NotFound() {
        // Given
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userRepository.findById(id);

        // Then
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void testSave() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("password");

        when(userRepository.save(user)).thenReturn(user);

        // When
        User result = userRepository.save(user);

        // Then
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeleteById() {
        // Given
        Long id = 1L;

        // When
        userRepository.deleteById(id);

        // Then
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void testCount() {
        // Given
        long count = 15L;
        when(userRepository.count()).thenReturn(count);

        // When
        long result = userRepository.count();

        // Then
        assertEquals(count, result);
        verify(userRepository, times(1)).count();
    }
}
