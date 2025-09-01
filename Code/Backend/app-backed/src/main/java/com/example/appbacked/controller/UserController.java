package com.example.appbacked.controller;

import com.example.appbacked.dto.PostDetailDTO;
import com.example.appbacked.dto.UserDetailDTO;
import com.example.appbacked.entity.User;
import com.example.appbacked.repository.FavoriteRepository;
import com.example.appbacked.repository.LikeRepository;
import com.example.appbacked.repository.PostRepository;
import com.example.appbacked.repository.UserRepository;
import com.example.appbacked.service.PostService;
import com.example.appbacked.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器：处理用户信息更新（用户名、头像）和用户详情
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(
        origins = {"http://localhost:3000", "http://localhost:8080"},
        allowCredentials = "true"
)
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    // 从配置文件读取上传目录
    @Value("${upload.avatar-dir:uploads/avatars/}")
    private String uploadDir;

    // 从配置文件读取服务器基础URL
    @Value("${server.base-url:http://localhost:8080}")
    private String serverBaseUrl;

    /**
     * 获取用户详情（包括统计信息和相关帖子）
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserDetail(@PathVariable Long userId,
                                          @RequestParam(required = false, defaultValue = "0") Long currentUserId) {
        try {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "用户不存在");
                return ResponseEntity.notFound().build();
            }

            UserDetailDTO userDetail = new UserDetailDTO();
            userDetail.setId(user.getId());
            userDetail.setUsername(user.getUsername());
            userDetail.setAvatar(user.getAvatar());
            userDetail.setGrade(user.getGrade());
            userDetail.setCollege(user.getCollege());

            // 统计信息
            Long postCount = postRepository.countByUserId(userId);
            userDetail.setPostCount(postCount);

            // 用户主动点赞的帖子数量（我点赞了多少帖子）
            Long myLikedPostCount = likeRepository.countByUserId(userId);
            userDetail.setMyLikedPostCount(myLikedPostCount);

            // 用户收到的点赞数（别人点赞我发布的帖子总数）
            Long receivedLikeCount = likeRepository.countByPostUserId(userId);
            userDetail.setReceivedLikeCount(receivedLikeCount);

            // 用户收到的收藏数（别人收藏我发布的帖子总数）
            Long receivedFavoriteCount = favoriteRepository.countByPostUserId(userId);
            userDetail.setReceivedFavoriteCount(receivedFavoriteCount);

            // 用户收藏的帖子数量（我收藏了多少帖子）
            Long favoritePostCount = favoriteRepository.countByUserId(userId);
            userDetail.setFavoritePostCount(favoritePostCount);

            // 获取用户发布的帖子
            List<PostDetailDTO> userPosts = postService.getPostsByUserId(userId);
            userDetail.setPosts(userPosts);

            // 获取用户点赞的帖子
            List<PostDetailDTO> likedPosts = postService.getLikedPostsByUserId(userId);
            userDetail.setLikedPosts(likedPosts);

            // 获取用户收藏的帖子
            List<PostDetailDTO> favoritePosts = postService.getFavoritePostsByUserId(userId);
            userDetail.setFavoritePosts(favoritePosts);

            return ResponseEntity.ok(userDetail);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "获取用户详情失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 更新用户信息（支持 username 和 avatar，可同时或单独更新）
     * 示例请求：PUT /api/update/1001
     * 请求体：
     * {
     *   "username": "小明同学",
     *   "avatar": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8/5+hHgAHggJ/PchI7wAAAABJRU5ErkJggg=="
     * }
     * 注意：字段可选，传 null 或不传表示不更新该字段
     * 如果 avatar 是 Base64 数据，则会自动保存并转换为 URL
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody User user
    ) {
        try {
            String newUsername = user.getUsername();
            String newAvatarData = user.getAvatar(); // 这里接收的是 Base64 数据或 URL

            // 处理头像数据：如果是 Base64，则保存并获取 URL
            String avatarUrl = processAvatarData(newAvatarData, id);

            User updatedUser = userService.updateUser(id, newUsername, avatarUrl);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "用户信息更新成功",
                    "data", updatedUser
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace(); // 便于调试
            return ResponseEntity.status(500)
                    .body(Map.of("success", false, "message", "服务器内部错误: " + e.getMessage()));
        }
    }

    /**
     * 仅更新用户名
     * 示例请求：PUT /api/update/name/1001
     * 请求体：{ "username": "小明同学" }
     */
    @PutMapping("/update/name/{id}")
    public ResponseEntity<?> updateUsername(
            @PathVariable Long id,
            @RequestBody Map<String, String> request
    ) {
        try {
            String username = request.get("username");
            if (username == null || username.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "用户名不能为空"));
            }

            User updatedUser = userService.updateUser(id, username.trim(), null);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "用户名更新成功",
                    "username", updatedUser.getUsername()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("success", false, "message", "服务器内部错误"));
        }
    }

    /**
     * 仅更新头像 (接收 Base64 数据)
     * 示例请求：POST /api/update/avatar/1001
     * 请求体：{ "avatar": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8/5+hHgAHggJ/PchI7wAAAABJRU5ErkJggg==" }
     */
    @PostMapping("/update/avatar/{id}")
    public ResponseEntity<?> updateAvatar(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            String avatarData = request.get("avatar");
            if (avatarData == null || avatarData.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "头像数据不能为空"));
            }

            // 处理头像数据：保存并获取 URL
            String avatarUrl = processAvatarData(avatarData, id);

            User updatedUser = userService.updateUser(id, null, avatarUrl);

            // 返回静态资源路径（简化版）
            String filename = avatarUrl.substring(avatarUrl.lastIndexOf("/") + 1);
            String filePath = "/uploads/avatars/" + filename;

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "avatar", filePath  // 返回静态路径
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("success", false, "message", "头像保存失败: " + e.getMessage()));
        }
    }

    /**
     * 处理头像数据。
     * 如果输入是 Base64 字符串 (以 "data:image/" 开头)，则将其解码并保存为文件，返回文件的 URL。
     * 如果输入是普通 URL，则直接返回。
     *
     * @param avatarData Base64 数据或 URL
     * @param userId     用户 ID，用于生成唯一的文件名
     * @return 处理后的图片 URL
     * @throws IOException 如果保存文件失败
     */
    private String processAvatarData(String avatarData, Long userId) throws IOException {
        if (avatarData == null || avatarData.trim().isEmpty()) {
            return null; // 不更新头像
        }

        // 检查是否为 Base64 数据 (Data URL)
        if (avatarData.startsWith("data:image/")) {
            // 1. 解析 Data URL，提取 MIME 类型和 Base64 数据
            String[] parts = avatarData.split(",");
            if (parts.length != 2) {
                throw new IllegalArgumentException("无效的 Base64 图像数据");
            }
            String base64Data = parts[1];

            // 2. 解码 Base64
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            // 3. 推断文件扩展名 (从 MIME Type)
            String mimeType = parts[0].split(";")[0].split(":")[1]; // "image/png"
            String fileExtension = mimeType.split("/")[1]; // "png"

            // 4. 生成唯一的文件名 (使用 userId 和时间戳)
            String fileName = "avatar_" + userId + "_" + System.currentTimeMillis() + "." + fileExtension;
            Path uploadPath = Paths.get(uploadDir);
            Path filePath = uploadPath.resolve(fileName).normalize();

            // 5. 确保上传目录存在
            Files.createDirectories(uploadPath);

            // 6. 保存文件
            Files.write(filePath, imageBytes);

            // 7. 构建可访问的 URL
            return serverBaseUrl + "/uploads/avatars/" + fileName;
        } else {
            // 如果不是 Data URL，假设它是有效的图片 URL，直接返回
            return avatarData;
        }
    }

    // 获取我发布的帖子
    @GetMapping("/users/{id}/posts")
    public ResponseEntity<List<?>> getMyPosts(@PathVariable Long id) {
        try {
            List<?> posts = postService.getPostsByUserId(id);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // 获取我收藏的帖子
    @GetMapping("/users/{id}/favorites")
    public ResponseEntity<List<?>> getFavoritePosts(@PathVariable Long id) {
        try {
            List<?> posts = postService.getFavoritePostsByUserId(id);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // 获取我点赞的帖子
    @GetMapping("/users/{id}/likes")
    public ResponseEntity<List<?>> getLikedPosts(@PathVariable Long id) {
        try {
            List<?> posts = postService.getLikedPostsByUserId(id);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
