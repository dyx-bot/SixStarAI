package com.example.appbacked.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserDetailDTO {
    private Long id;
    private String username;
    private String avatar;
    private String grade;
    private String college;
    private Long postCount;              // 发帖数
    private Long myLikedPostCount;       // 主动点赞的帖子数
    private Long receivedLikeCount;      // 收到的点赞数
    private Long receivedFavoriteCount;  // 收到的收藏数
    private Long favoritePostCount;      // 主动收藏的帖子数
    private List<PostDetailDTO> posts;           // 用户发布的帖子
    private List<PostDetailDTO> likedPosts;      // 用户点赞的帖子
    private List<PostDetailDTO> favoritePosts;   // 用户收藏的帖子

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public void setGrade(String grade) { this.grade = grade; }
    public void setCollege(String college) { this.college = college; }
    public void setPostCount(Long postCount) { this.postCount = postCount; }
    public void setMyLikedPostCount(Long myLikedPostCount) { this.myLikedPostCount = myLikedPostCount; }
    public void setReceivedLikeCount(Long receivedLikeCount) { this.receivedLikeCount = receivedLikeCount; }
    public void setReceivedFavoriteCount(Long receivedFavoriteCount) { this.receivedFavoriteCount = receivedFavoriteCount; }
    public void setFavoritePostCount(Long favoritePostCount) { this.favoritePostCount = favoritePostCount; }
    public void setPosts(List<PostDetailDTO> posts) { this.posts = posts; }
    public void setLikedPosts(List<PostDetailDTO> likedPosts) { this.likedPosts = likedPosts; }
    public void setFavoritePosts(List<PostDetailDTO> favoritePosts) { this.favoritePosts = favoritePosts; }

}