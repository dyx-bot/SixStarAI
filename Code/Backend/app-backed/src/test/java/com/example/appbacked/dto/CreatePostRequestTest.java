package com.example.appbacked.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreatePostRequestTest {

    private CreatePostRequest createPostRequest;

    @BeforeEach
    void setUp() {
        createPostRequest = new CreatePostRequest();
    }

    @Test
    void testGetSetUserId() {
        // Given
        Long userId = 1L;

        // When
        createPostRequest.setUserId(userId);

        // Then
        assertEquals(userId, createPostRequest.getUserId());
    }

    @Test
    void testGetSetUserId_NullValue() {
        // When
        createPostRequest.setUserId(null);

        // Then
        assertNull(createPostRequest.getUserId());
    }

    @Test
    void testGetSetTitle() {
        // Given
        String title = "Test Title";

        // When
        createPostRequest.setTitle(title);

        // Then
        assertEquals(title, createPostRequest.getTitle());
    }

    @Test
    void testGetSetTitle_EmptyValue() {
        // Given
        String title = "";

        // When
        createPostRequest.setTitle(title);

        // Then
        assertEquals(title, createPostRequest.getTitle());
    }

    @Test
    void testGetSetTitle_NullValue() {
        // When
        createPostRequest.setTitle(null);

        // Then
        assertNull(createPostRequest.getTitle());
    }

    @Test
    void testGetSetContent() {
        // Given
        String content = "Test Content";

        // When
        createPostRequest.setContent(content);

        // Then
        assertEquals(content, createPostRequest.getContent());
    }

    @Test
    void testGetSetContent_EmptyValue() {
        // Given
        String content = "";

        // When
        createPostRequest.setContent(content);

        // Then
        assertEquals(content, createPostRequest.getContent());
    }

    @Test
    void testGetSetContent_NullValue() {
        // When
        createPostRequest.setContent(null);

        // Then
        assertNull(createPostRequest.getContent());
    }

    @Test
    void testGetSetIsHideCollege() {
        // Given
        Boolean isHideCollege = true;

        // When
        createPostRequest.setIsHideCollege(isHideCollege);

        // Then
        assertEquals(isHideCollege, createPostRequest.getIsHideCollege());
    }

    @Test
    void testGetSetIsHideCollege_FalseValue() {
        // Given
        Boolean isHideCollege = false;

        // When
        createPostRequest.setIsHideCollege(isHideCollege);

        // Then
        assertEquals(isHideCollege, createPostRequest.getIsHideCollege());
    }

    @Test
    void testGetSetIsHideCollege_NullValue() {
        // When
        createPostRequest.setIsHideCollege(null);

        // Then
        assertNull(createPostRequest.getIsHideCollege());
    }

    @Test
    void testGetSetIsHideGrade() {
        // Given
        Boolean isHideGrade = true;

        // When
        createPostRequest.setIsHideGrade(isHideGrade);

        // Then
        assertEquals(isHideGrade, createPostRequest.getIsHideGrade());
    }

    @Test
    void testGetSetIsHideGrade_FalseValue() {
        // Given
        Boolean isHideGrade = false;

        // When
        createPostRequest.setIsHideGrade(isHideGrade);

        // Then
        assertEquals(isHideGrade, createPostRequest.getIsHideGrade());
    }

    @Test
    void testGetSetIsHideGrade_NullValue() {
        // When
        createPostRequest.setIsHideGrade(null);

        // Then
        assertNull(createPostRequest.getIsHideGrade());
    }

    @Test
    void testGetSetTag() {
        // Given
        String tag = "Test Tag";

        // When
        createPostRequest.setTag(tag);

        // Then
        assertEquals(tag, createPostRequest.getTag());
    }

    @Test
    void testGetSetTag_EmptyValue() {
        // Given
        String tag = "";

        // When
        createPostRequest.setTag(tag);

        // Then
        assertEquals(tag, createPostRequest.getTag());
    }

    @Test
    void testGetSetTag_NullValue() {
        // When
        createPostRequest.setTag(null);

        // Then
        assertNull(createPostRequest.getTag());
    }

    @Test
    void testDefaultConstructor_InitialValues() {
        // Then
        assertNull(createPostRequest.getUserId());
        assertNull(createPostRequest.getTitle());
        assertNull(createPostRequest.getContent());
        assertNull(createPostRequest.getIsHideCollege());
        assertNull(createPostRequest.getIsHideGrade());
        assertNull(createPostRequest.getTag());
    }
}
