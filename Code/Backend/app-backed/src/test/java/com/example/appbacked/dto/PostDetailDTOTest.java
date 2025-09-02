package com.example.appbacked.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostDetailDTOTest {

    private PostDetailDTO postDetailDTO;

    @BeforeEach
    void setUp() {
        postDetailDTO = new PostDetailDTO();
    }

    @Test
    void testGetSetId() {
        // Given
        Long id = 1L;

        // When
        postDetailDTO.setId(id);

        // Then
        assertEquals(id, postDetailDTO.getId());
    }

    @Test
    void testGetSetTitle() {
        // Given
        String title = "Test Title";

        // When
        postDetailDTO.setTitle(title);

        // Then
        assertEquals(title, postDetailDTO.getTitle());
    }

    @Test
    void testGetSetContent() {
        // Given
        String content = "Test Content";

        // When
        postDetailDTO.setContent(content);

        // Then
        assertEquals(content, postDetailDTO.getContent());
    }

    @Test
    void testGetSetIsHideCollege() {
        // Given
        Boolean isHideCollege = true;

        // When
        postDetailDTO.setIsHideCollege(isHideCollege);

        // Then
        assertEquals(isHideCollege, postDetailDTO.getIsHideCollege());
    }

    @Test
    void testGetSetIsHideGrade() {
        // Given
        Boolean isHideGrade = false;

        // When
        postDetailDTO.setIsHideGrade(isHideGrade);

        // Then
        assertEquals(isHideGrade, postDetailDTO.getIsHideGrade());
    }

    @Test
    void testGetSetTag() {
        // Given
        String tag = "Test Tag";

        // When
        postDetailDTO.setTag(tag);

        // Then
        assertEquals(tag, postDetailDTO.getTag());
    }

    @Test
    void testGetSetUsername() {
        // Given
        String username = "testUser";

        // When
        postDetailDTO.setUsername(username);

        // Then
        assertEquals(username, postDetailDTO.getUsername());
    }

    @Test
    void testGetSetAvatar() {
        // Given
        String avatar = "avatar.png";

        // When
        postDetailDTO.setAvatar(avatar);

        // Then
        assertEquals(avatar, postDetailDTO.getAvatar());
    }

    @Test
    void testGetSetGrade() {
        // Given
        String grade = "Freshman";

        // When
        postDetailDTO.setGrade(grade);

        // Then
        assertEquals(grade, postDetailDTO.getGrade());
    }

    @Test
    void testGetSetCollege() {
        // Given
        String college = "Computer Science";

        // When
        postDetailDTO.setCollege(college);

        // Then
        assertEquals(college, postDetailDTO.getCollege());
    }

    @Test
    void testGetSetLikeCount() {
        // Given
        Integer likeCount = 10;

        // When
        postDetailDTO.setLikeCount(likeCount);

        // Then
        assertEquals(likeCount, postDetailDTO.getLikeCount());
    }

    @Test
    void testGetSetReplyCount() {
        // Given
        Integer replyCount = 5;

        // When
        postDetailDTO.setReplyCount(replyCount);

        // Then
        assertEquals(replyCount, postDetailDTO.getReplyCount());
    }

    @Test
    void testGetSetIsLiked() {
        // Given
        Boolean isLiked = true;

        // When
        postDetailDTO.setIsLiked(isLiked);

        // Then
        assertEquals(isLiked, postDetailDTO.getIsLiked());
    }

    @Test
    void testGetSetIsFavorited() {
        // Given
        Boolean isFavorited = false;

        // When
        postDetailDTO.setIsFavorited(isFavorited);

        // Then
        assertEquals(isFavorited, postDetailDTO.getIsFavorited());
    }

    @Test
    void testGetSetCreatedAt() {
        // Given
        String createdAt = "2023-01-01 12:00:00";

        // When
        postDetailDTO.setCreatedAt(createdAt);

        // Then
        assertEquals(createdAt, postDetailDTO.getCreatedAt());
    }

    @Test
    void testToString() {
        // Given
        postDetailDTO.setId(1L);
        postDetailDTO.setTitle("Test Title");

        // When
        String toStringResult = postDetailDTO.toString();

        // Then
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("PostDetailDTO"));
        assertTrue(toStringResult.contains("id=1"));
        assertTrue(toStringResult.contains("title=Test Title"));
    }

    @Test
    void testEquals_SameObject() {
        // When & Then
        assertEquals(postDetailDTO, postDetailDTO);
    }

    @Test
    void testEquals_EqualObjects() {
        // Given
        PostDetailDTO other = new PostDetailDTO();
        other.setId(1L);
        postDetailDTO.setId(1L);

        // When & Then
        assertEquals(postDetailDTO, other);
    }

    @Test
    void testEquals_DifferentObjects() {
        // Given
        PostDetailDTO other = new PostDetailDTO();
        other.setId(1L);
        postDetailDTO.setId(2L);

        // When & Then
        assertNotEquals(postDetailDTO, other);
    }

    @Test
    void testEquals_NullObject() {
        // When & Then
        assertNotEquals(null, postDetailDTO);
    }

    @Test
    void testEquals_DifferentClass() {
        // Given
        String other = "other";

        // When & Then
        assertNotEquals(postDetailDTO, other);
    }

    @Test
    void testHashCode_SameObject() {
        // When
        int hashCode1 = postDetailDTO.hashCode();
        int hashCode2 = postDetailDTO.hashCode();

        // Then
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCode_EqualObjects() {
        // Given
        PostDetailDTO other = new PostDetailDTO();
        other.setId(1L);
        postDetailDTO.setId(1L);

        // When
        int hashCode1 = postDetailDTO.hashCode();
        int hashCode2 = other.hashCode();

        // Then
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testNoArgsConstructor() {
        // When
        PostDetailDTO dto = new PostDetailDTO();

        // Then
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getTitle());
        assertNull(dto.getContent());
        assertNull(dto.getIsHideCollege());
        assertNull(dto.getIsHideGrade());
        assertNull(dto.getTag());
        assertNull(dto.getUsername());
        assertNull(dto.getAvatar());
        assertNull(dto.getGrade());
        assertNull(dto.getCollege());
        assertNull(dto.getLikeCount());
        assertNull(dto.getReplyCount());
        assertNull(dto.getIsLiked());
        assertNull(dto.getIsFavorited());
        assertNull(dto.getCreatedAt());
    }
}

