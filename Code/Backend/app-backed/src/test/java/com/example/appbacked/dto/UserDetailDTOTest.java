package com.example.appbacked.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailDTOTest {

    private UserDetailDTO userDetailDTO;

    @BeforeEach
    void setUp() {
        userDetailDTO = new UserDetailDTO();
    }

    @Test
    void testGetSetId() {
        // Given
        Long id = 1L;

        // When
        userDetailDTO.setId(id);

        // Then
        assertEquals(id, userDetailDTO.getId());
    }

    @Test
    void testGetSetUsername() {
        // Given
        String username = "testUser";

        // When
        userDetailDTO.setUsername(username);

        // Then
        assertEquals(username, userDetailDTO.getUsername());
    }

    @Test
    void testGetSetAvatar() {
        // Given
        String avatar = "avatar.png";

        // When
        userDetailDTO.setAvatar(avatar);

        // Then
        assertEquals(avatar, userDetailDTO.getAvatar());
    }

    @Test
    void testGetSetGrade() {
        // Given
        String grade = "Freshman";

        // When
        userDetailDTO.setGrade(grade);

        // Then
        assertEquals(grade, userDetailDTO.getGrade());
    }

    @Test
    void testGetSetCollege() {
        // Given
        String college = "Computer Science";

        // When
        userDetailDTO.setCollege(college);

        // Then
        assertEquals(college, userDetailDTO.getCollege());
    }

    @Test
    void testGetSetPostCount() {
        // Given
        Long postCount = 10L;

        // When
        userDetailDTO.setPostCount(postCount);

        // Then
        assertEquals(postCount, userDetailDTO.getPostCount());
    }

    @Test
    void testGetSetMyLikedPostCount() {
        // Given
        Long myLikedPostCount = 5L;

        // When
        userDetailDTO.setMyLikedPostCount(myLikedPostCount);

        // Then
        assertEquals(myLikedPostCount, userDetailDTO.getMyLikedPostCount());
    }

    @Test
    void testGetSetReceivedLikeCount() {
        // Given
        Long receivedLikeCount = 20L;

        // When
        userDetailDTO.setReceivedLikeCount(receivedLikeCount);

        // Then
        assertEquals(receivedLikeCount, userDetailDTO.getReceivedLikeCount());
    }

    @Test
    void testGetSetReceivedFavoriteCount() {
        // Given
        Long receivedFavoriteCount = 15L;

        // When
        userDetailDTO.setReceivedFavoriteCount(receivedFavoriteCount);

        // Then
        assertEquals(receivedFavoriteCount, userDetailDTO.getReceivedFavoriteCount());
    }

    @Test
    void testGetSetFavoritePostCount() {
        // Given
        Long favoritePostCount = 8L;

        // When
        userDetailDTO.setFavoritePostCount(favoritePostCount);

        // Then
        assertEquals(favoritePostCount, userDetailDTO.getFavoritePostCount());
    }

    @Test
    void testGetSetPosts() {
        // Given
        List<PostDetailDTO> posts = new ArrayList<>();
        PostDetailDTO post = new PostDetailDTO();
        post.setId(1L);
        post.setTitle("Test Post");
        posts.add(post);

        // When
        userDetailDTO.setPosts(posts);

        // Then
        assertEquals(posts, userDetailDTO.getPosts());
        assertEquals(1, userDetailDTO.getPosts().size());
        assertEquals(post.getId(), userDetailDTO.getPosts().get(0).getId());
    }

    @Test
    void testGetSetPosts_NullValue() {
        // When
        userDetailDTO.setPosts(null);

        // Then
        assertNull(userDetailDTO.getPosts());
    }

    @Test
    void testGetSetLikedPosts() {
        // Given
        List<PostDetailDTO> likedPosts = new ArrayList<>();
        PostDetailDTO post = new PostDetailDTO();
        post.setId(2L);
        post.setTitle("Liked Post");
        likedPosts.add(post);

        // When
        userDetailDTO.setLikedPosts(likedPosts);

        // Then
        assertEquals(likedPosts, userDetailDTO.getLikedPosts());
        assertEquals(1, userDetailDTO.getLikedPosts().size());
        assertEquals(post.getId(), userDetailDTO.getLikedPosts().get(0).getId());
    }

    @Test
    void testGetSetLikedPosts_NullValue() {
        // When
        userDetailDTO.setLikedPosts(null);

        // Then
        assertNull(userDetailDTO.getLikedPosts());
    }

    @Test
    void testGetSetFavoritePosts() {
        // Given
        List<PostDetailDTO> favoritePosts = new ArrayList<>();
        PostDetailDTO post = new PostDetailDTO();
        post.setId(3L);
        post.setTitle("Favorite Post");
        favoritePosts.add(post);

        // When
        userDetailDTO.setFavoritePosts(favoritePosts);

        // Then
        assertEquals(favoritePosts, userDetailDTO.getFavoritePosts());
        assertEquals(1, userDetailDTO.getFavoritePosts().size());
        assertEquals(post.getId(), userDetailDTO.getFavoritePosts().get(0).getId());
    }

    @Test
    void testGetSetFavoritePosts_NullValue() {
        // When
        userDetailDTO.setFavoritePosts(null);

        // Then
        assertNull(userDetailDTO.getFavoritePosts());
    }

    @Test
    void testNoArgsConstructor() {
        // When
        UserDetailDTO dto = new UserDetailDTO();

        // Then
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getUsername());
        assertNull(dto.getAvatar());
        assertNull(dto.getGrade());
        assertNull(dto.getCollege());
        assertNull(dto.getPostCount());
        assertNull(dto.getMyLikedPostCount());
        assertNull(dto.getReceivedLikeCount());
        assertNull(dto.getReceivedFavoriteCount());
        assertNull(dto.getFavoritePostCount());
        assertNull(dto.getPosts());
        assertNull(dto.getLikedPosts());
        assertNull(dto.getFavoritePosts());
    }

    @Test
    void testToString() {
        // Given
        userDetailDTO.setId(1L);
        userDetailDTO.setUsername("testUser");

        // When
        String toStringResult = userDetailDTO.toString();

        // Then
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("UserDetailDTO"));
        assertTrue(toStringResult.contains("id=1"));
        assertTrue(toStringResult.contains("username=testUser"));
    }

    @Test
    void testEquals_SameObject() {
        // When & Then
        assertEquals(userDetailDTO, userDetailDTO);
    }

    @Test
    void testEquals_EqualObjects() {
        // Given
        UserDetailDTO other = new UserDetailDTO();
        other.setId(1L);
        userDetailDTO.setId(1L);

        // When & Then
        assertEquals(userDetailDTO, other);
    }

    @Test
    void testEquals_DifferentObjects() {
        // Given
        UserDetailDTO other = new UserDetailDTO();
        other.setId(1L);
        userDetailDTO.setId(2L);

        // When & Then
        assertNotEquals(userDetailDTO, other);
    }

    @Test
    void testEquals_NullObject() {
        // When & Then
        assertNotEquals(null, userDetailDTO);
    }

    @Test
    void testEquals_DifferentClass() {
        // Given
        String other = "other";

        // When & Then
        assertNotEquals(userDetailDTO, other);
    }

    @Test
    void testHashCode_SameObject() {
        // When
        int hashCode1 = userDetailDTO.hashCode();
        int hashCode2 = userDetailDTO.hashCode();

        // Then
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCode_EqualObjects() {
        // Given
        UserDetailDTO other = new UserDetailDTO();
        other.setId(1L);
        userDetailDTO.setId(1L);

        // When
        int hashCode1 = userDetailDTO.hashCode();
        int hashCode2 = other.hashCode();

        // Then
        assertEquals(hashCode1, hashCode2);
    }
}
