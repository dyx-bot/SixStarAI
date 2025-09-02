package com.example.appbacked.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    private Post post;

    @BeforeEach
    void setUp() {
        post = new Post();
    }

    @Test
    void testGetSetId() {
        // Given
        Long id = 1L;

        // When
        post.setId(id);

        // Then
        assertEquals(id, post.getId());
    }

    @Test
    void testGetSetUserId() {
        // Given
        Long userId = 100L;

        // When
        post.setUserId(userId);

        // Then
        assertEquals(userId, post.getUserId());
    }

    @Test
    void testGetSetTitle() {
        // Given
        String title = "Test Title";

        // When
        post.setTitle(title);

        // Then
        assertEquals(title, post.getTitle());
    }

    @Test
    void testGetSetTitle_NullValue() {
        // When
        post.setTitle(null);

        // Then
        assertNull(post.getTitle());
    }

    @Test
    void testGetSetContent() {
        // Given
        String content = "Test Content";

        // When
        post.setContent(content);

        // Then
        assertEquals(content, post.getContent());
    }

    @Test
    void testGetSetContent_NullValue() {
        // When
        post.setContent(null);

        // Then
        assertNull(post.getContent());
    }

    @Test
    void testGetSetIsHideCollege() {
        // Given
        Boolean isHideCollege = true;

        // When
        post.setIsHideCollege(isHideCollege);

        // Then
        assertEquals(isHideCollege, post.getIsHideCollege());
    }

    @Test
    void testGetSetIsHideCollege_FalseValue() {
        // Given
        Boolean isHideCollege = false;

        // When
        post.setIsHideCollege(isHideCollege);

        // Then
        assertEquals(isHideCollege, post.getIsHideCollege());
    }

    @Test
    void testGetSetIsHideCollege_NullValue() {
        // When
        post.setIsHideCollege(null);

        // Then
        assertNull(post.getIsHideCollege());
    }

    @Test
    void testGetSetIsHideGrade() {
        // Given
        Boolean isHideGrade = true;

        // When
        post.setIsHideGrade(isHideGrade);

        // Then
        assertEquals(isHideGrade, post.getIsHideGrade());
    }

    @Test
    void testGetSetIsHideGrade_FalseValue() {
        // Given
        Boolean isHideGrade = false;

        // When
        post.setIsHideGrade(isHideGrade);

        // Then
        assertEquals(isHideGrade, post.getIsHideGrade());
    }

    @Test
    void testGetSetIsHideGrade_NullValue() {
        // When
        post.setIsHideGrade(null);

        // Then
        assertNull(post.getIsHideGrade());
    }

    @Test
    void testGetSetTag() {
        // Given
        String tag = "Test Tag";

        // When
        post.setTag(tag);

        // Then
        assertEquals(tag, post.getTag());
    }

    @Test
    void testGetSetTag_NullValue() {
        // When
        post.setTag(null);

        // Then
        assertNull(post.getTag());
    }

    @Test
    void testGetSetGrade() {
        // Given
        String grade = "Freshman";

        // When
        post.setGrade(grade);

        // Then
        assertEquals(grade, post.getGrade());
    }

    @Test
    void testGetSetGrade_NullValue() {
        // When
        post.setGrade(null);

        // Then
        assertNull(post.getGrade());
    }

    @Test
    void testGetSetCollege() {
        // Given
        String college = "Computer Science";

        // When
        post.setCollege(college);

        // Then
        assertEquals(college, post.getCollege());
    }

    @Test
    void testGetSetCollege_NullValue() {
        // When
        post.setCollege(null);

        // Then
        assertNull(post.getCollege());
    }

    @Test
    void testGetSetLikeCount() {
        // Given
        Integer likeCount = 10;

        // When
        post.setLikeCount(likeCount);

        // Then
        assertEquals(likeCount, post.getLikeCount());
    }

    @Test
    void testGetSetLikeCount_ZeroValue() {
        // Given
        Integer likeCount = 0;

        // When
        post.setLikeCount(likeCount);

        // Then
        assertEquals(likeCount, post.getLikeCount());
    }

    @Test
    void testGetSetLikeCount_NullValue() {
        // When
        post.setLikeCount(null);

        // Then
        assertNull(post.getLikeCount());
    }

    @Test
    void testGetSetReplyCount() {
        // Given
        Integer replyCount = 5;

        // When
        post.setReplyCount(replyCount);

        // Then
        assertEquals(replyCount, post.getReplyCount());
    }

    @Test
    void testGetSetReplyCount_ZeroValue() {
        // Given
        Integer replyCount = 0;

        // When
        post.setReplyCount(replyCount);

        // Then
        assertEquals(replyCount, post.getReplyCount());
    }

    @Test
    void testGetSetReplyCount_NullValue() {
        // When
        post.setReplyCount(null);

        // Then
        assertNull(post.getReplyCount());
    }

    @Test
    void testGetSetCreatedAt() {
        // Given
        LocalDateTime createdAt = LocalDateTime.now();

        // When
        post.setCreatedAt(createdAt);

        // Then
        assertEquals(createdAt, post.getCreatedAt());
    }

    @Test
    void testGetSetCreatedAt_NullValue() {
        // When
        post.setCreatedAt(null);

        // Then
        assertNull(post.getCreatedAt());
    }

    @Test
    void testGetSetUpdatedAt() {
        // Given
        LocalDateTime updatedAt = LocalDateTime.now();

        // When
        post.setUpdatedAt(updatedAt);

        // Then
        assertEquals(updatedAt, post.getUpdatedAt());
    }

    @Test
    void testGetSetUpdatedAt_NullValue() {
        // When
        post.setUpdatedAt(null);

        // Then
        assertNull(post.getUpdatedAt());
    }

    @Test
    void testNoArgsConstructor_DefaultValues() {
        // When
        Post postEntity = new Post();

        // Then
        assertNotNull(postEntity);
        assertNull(postEntity.getId());
        assertNull(postEntity.getUserId());
        assertNull(postEntity.getTitle());
        assertNull(postEntity.getContent());
        assertFalse(postEntity.getIsHideCollege()); // 默认值为 false
        assertFalse(postEntity.getIsHideGrade());   // 默认值为 false
        assertNull(postEntity.getTag());
        assertNull(postEntity.getGrade());
        assertNull(postEntity.getCollege());
        assertEquals(0, postEntity.getLikeCount());    // 默认值为 0
        assertEquals(0, postEntity.getReplyCount());   // 默认值为 0
        assertNull(postEntity.getCreatedAt());
        assertNull(postEntity.getUpdatedAt());
    }

    @Test
    void testToString() {
        // Given
        post.setId(1L);
        post.setUserId(100L);
        post.setTitle("Test Title");

        // When
        String toStringResult = post.toString();

        // Then
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("Post"));
        assertTrue(toStringResult.contains("id=1"));
        assertTrue(toStringResult.contains("userId=100"));
        assertTrue(toStringResult.contains("title=Test Title"));
    }

    @Test
    void testEquals_SameObject() {
        // When & Then
        assertEquals(post, post);
    }

    @Test
    void testEquals_EqualObjects() {
        // Given
        Post other = new Post();
        other.setId(1L);
        post.setId(1L);

        // When & Then
        assertEquals(post, other);
    }

    @Test
    void testEquals_DifferentObjects() {
        // Given
        Post other = new Post();
        other.setId(1L);
        post.setId(2L);

        // When & Then
        assertNotEquals(post, other);
    }

    @Test
    void testEquals_NullObject() {
        // When & Then
        assertNotEquals(null, post);
    }

    @Test
    void testEquals_DifferentClass() {
        // Given
        String other = "other";

        // When & Then
        assertNotEquals(post, other);
    }

    @Test
    void testHashCode_SameObject() {
        // When
        int hashCode1 = post.hashCode();
        int hashCode2 = post.hashCode();

        // Then
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCode_EqualObjects() {
        // Given
        Post other = new Post();
        other.setId(1L);
        post.setId(1L);

        // When
        int hashCode1 = post.hashCode();
        int hashCode2 = other.hashCode();

        // Then
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testAllFieldsTogether() {
        // Given
        Long id = 1L;
        Long userId = 100L;
        String title = "Test Title";
        String content = "Test Content";
        Boolean isHideCollege = true;
        Boolean isHideGrade = false;
        String tag = "Test Tag";
        String grade = "Senior";
        String college = "Engineering";
        Integer likeCount = 10;
        Integer replyCount = 5;
        LocalDateTime createdAt = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2023, 1, 2, 12, 0, 0);

        // When
        post.setId(id);
        post.setUserId(userId);
        post.setTitle(title);
        post.setContent(content);
        post.setIsHideCollege(isHideCollege);
        post.setIsHideGrade(isHideGrade);
        post.setTag(tag);
        post.setGrade(grade);
        post.setCollege(college);
        post.setLikeCount(likeCount);
        post.setReplyCount(replyCount);
        post.setCreatedAt(createdAt);
        post.setUpdatedAt(updatedAt);

        // Then
        assertEquals(id, post.getId());
        assertEquals(userId, post.getUserId());
        assertEquals(title, post.getTitle());
        assertEquals(content, post.getContent());
        assertEquals(isHideCollege, post.getIsHideCollege());
        assertEquals(isHideGrade, post.getIsHideGrade());
        assertEquals(tag, post.getTag());
        assertEquals(grade, post.getGrade());
        assertEquals(college, post.getCollege());
        assertEquals(likeCount, post.getLikeCount());
        assertEquals(replyCount, post.getReplyCount());
        assertEquals(createdAt, post.getCreatedAt());
        assertEquals(updatedAt, post.getUpdatedAt());
    }
}
