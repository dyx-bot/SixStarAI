// AdminController.java
package com.example.appbacked.controller;

import com.example.appbacked.dto.PostDetailDTO;
import com.example.appbacked.entity.Post;
import com.example.appbacked.entity.User;
import com.example.appbacked.repository.PostRepository;
import com.example.appbacked.repository.ReportRepository;
import com.example.appbacked.repository.UserRepository;
import com.example.appbacked.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@RestController
@RequestMapping("/api/admin")
@CrossOrigin(
        origins = {"http://localhost:3000", "http://localhost:8080"},
        allowCredentials = "true"
)
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    /**
     * 管理员登录接口
     */
    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestBody User user) {
        try {
            // 使用现有的登录逻辑
            if (userService.login(user)) {
                // 检查用户是否为管理员
                if ("admin".equals(user.getRole())) {
                    return ResponseEntity.ok(Map.of(
                            "success", true,
                            "message", "管理员登录成功",
                            "data", user
                    ));
                } else {
                    return ResponseEntity.badRequest()
                            .body(Map.of("success", false, "message", "权限不足，您不是管理员"));
                }
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "登录失败，用户名或密码错误"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("success", false, "message", "服务器内部错误: " + e.getMessage()));
        }
    }

    /**
     * 获取所有用户列表（仅管理员可访问）
     */
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", users
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("success", false, "message", "获取用户列表失败: " + e.getMessage()));
        }
    }

    /**
     * 更新用户角色（仅管理员可访问）
     */
    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String role = request.get("role");
            if (role == null || role.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "角色不能为空"));
            }

            userService.updateUserRole(id, role);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "用户角色更新成功"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("success", false, "message", "更新用户角色失败: " + e.getMessage()));
        }
    }

    /**
     * 获取统计信息（用户数和帖子数）
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        try {
            // 获取普通用户数量（排除管理员）
            long userCount = userRepository.countNormalUsers();
            // 获取帖子总数
            long postCount = postRepository.count();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", Map.of(
                            "userCount", userCount,
                            "postCount", postCount
                    )
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("success", false, "message", "获取统计信息失败: " + e.getMessage()));
        }
    }

    /**
     * 获取专业分布统计
     */
    @GetMapping("/stats/grades")
    public ResponseEntity<?> getGradeStats() {
        try {
            List<Object[]> gradeStats = userRepository.countUsersByGrade();
            Map<String, Long> gradeMap = new HashMap<>();

            for (Object[] row : gradeStats) {
                String grade = (String) row[0];
                Long count = (Long) row[1];
                if (grade != null && !grade.trim().isEmpty()) {
                    gradeMap.put(grade, count);
                }
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", gradeMap
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("success", false, "message", "获取专业统计失败: " + e.getMessage()));
        }
    }

    /**
     * 获取学院分布统计
     */
    @GetMapping("/stats/colleges")
    public ResponseEntity<?> getCollegeStats() {
        try {
            List<Object[]> collegeStats = userRepository.countUsersByCollege();
            Map<String, Long> collegeMap = new HashMap<>();

            for (Object[] row : collegeStats) {
                String college = (String) row[0];
                Long count = (Long) row[1];
                if (college != null && !college.trim().isEmpty()) {
                    collegeMap.put(college, count);
                }
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", collegeMap
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("success", false, "message", "获取学院统计失败: " + e.getMessage()));
        }
    }

    /**
     * 获取标签分布统计
     */
    @GetMapping("/stats/tags")
    public ResponseEntity<?> getTagStats() {
        try {
            List<Object[]> tagStats = postRepository.countPostsByTag();
            Map<String, Long> tagMap = new HashMap<>();

            for (Object[] row : tagStats) {
                String tag = (String) row[0];
                Long count = (Long) row[1];
                if (tag != null && !tag.trim().isEmpty()) {
                    tagMap.put(tag, count);
                }
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", tagMap
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("success", false, "message", "获取标签统计失败: " + e.getMessage()));
        }
    }

    /**
     * 分页获取所有帖子（按时间倒序排列）
     * @param page 页码（从0开始）
     * @param size 每页大小
     */
    @GetMapping("/posts")
    public ResponseEntity<?> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            // 创建分页请求，按创建时间倒序排列
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

            // 获取分页数据
            Page<Post> postPage = postRepository.findAllByOrderByCreatedAtDesc(pageable);

            // 转换为 PostDetailDTO
            List<PostDetailDTO> postDTOs = postPage.getContent().stream().map(post -> {
                PostDetailDTO dto = new PostDetailDTO();
                dto.setId(post.getId());
                dto.setTitle(post.getTitle());
                dto.setContent(post.getContent());
                dto.setTag(post.getTag());
                dto.setIsHideCollege(post.getIsHideCollege());
                dto.setIsHideGrade(post.getIsHideGrade());
                dto.setLikeCount(post.getLikeCount() != null ? post.getLikeCount() : 0);
                dto.setReplyCount(post.getReplyCount() != null ? post.getReplyCount() : 0);
                // 添加举报数处理，确保不为null
                dto.setReportCount(post.getReportCount() != null ? post.getReportCount() : 0);


                // 使用 DateTimeFormatter 格式化 LocalDateTime
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                dto.setCreatedAt(post.getCreatedAt().format(formatter));
                // 获取用户信息
                User user = userRepository.findById(post.getUserId()).orElse(null);
                if (user != null) {
                    dto.setUsername(user.getUsername());
                    dto.setAvatar(user.getAvatar());
                    dto.setGrade(user.getGrade());
                    dto.setCollege(user.getCollege());
                } else {
                    dto.setUsername("未知用户");
                    dto.setAvatar(null);
                    dto.setGrade(null);
                    dto.setCollege(null);
                }

                // 管理员界面不需要这些字段
                dto.setIsLiked(false);
                dto.setIsFavorited(false);

                return dto;
            }).collect(Collectors.toList());

            // 构建响应数据
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", postDTOs);
            response.put("currentPage", postPage.getNumber());
            response.put("totalItems", postPage.getTotalElements());
            response.put("totalPages", postPage.getTotalPages());
            response.put("pageSize", postPage.getSize());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("success", false, "message", "获取帖子列表失败: " + e.getMessage()));
        }
    }

    /**
     * 删除帖子（仅管理员可访问）
     */
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        try {
            postRepository.deleteById(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "帖子删除成功"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("success", false, "message", "删除帖子失败: " + e.getMessage()));
        }
    }

    /**
     * 分页获取所有用户（排除管理员，按ID倒序排列）
     * @param page 页码（从0开始）
     * @param size 每页大小
     */
    @GetMapping("/users/page")
    public ResponseEntity<?> getAllUsersWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            // 创建分页请求，按ID倒序排列
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

            // 获取分页数据，排除管理员
            Page<User> userPage = userRepository.findAllByRoleNotAdmin(pageable);

            // 构建响应数据
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", userPage.getContent());
            response.put("currentPage", userPage.getNumber());
            response.put("userItems", userPage.getTotalElements());
            response.put("totalPages", userPage.getTotalPages());
            response.put("pageSize", userPage.getSize());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("success", false, "message", "获取用户列表失败: " + e.getMessage()));
        }
    }


    /**
     * 删除用户（仅管理员可访问）
     * 这个功能相当于"拉黑"用户，直接从数据库删除
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "用户删除成功"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("success", false, "message", "删除用户失败: " + e.getMessage()));
        }
    }

    /**
     * 创建新用户（仅管理员可访问）
     */
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            // 验证必要字段
            if (user.getId() == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "用户ID不能为空"));
            }

            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "密码不能为空"));
            }

            // 检查用户是否已存在
            if (userRepository.existsById(user.getId())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "用户ID已存在"));
            }

            // 对密码进行SHA-256加密
            String hashedPassword = hashWithSHA256(user.getPassword());
            user.setPassword(hashedPassword);

            // 设置默认角色为普通用户
            if (user.getRole() == null) {
                user.setRole("USER");
            }

            // 保存用户
            User savedUser = userRepository.save(user);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "用户创建成功",
                    "data", savedUser
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("success", false, "message", "创建用户失败: " + e.getMessage()));
        }
    }

    /**
     * SHA-256 密码哈希
     * @param input 明文密码
     * @return SHA-256 哈希值（十六进制字符串）
     */
    private String hashWithSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("加密失败", e);
        }
    }

    // 在类中添加 ReportRepository 注入
    @Autowired
    private ReportRepository reportRepository;


// 在 AdminController.java 中添加以下方法
    /**
     * 获取被举报的帖子列表（按举报数量排序）
     * @param page 页码（从0开始）
     * @param size 每页大小
     */
    @GetMapping("/reported-posts")
    public ResponseEntity<?> getReportedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            // 创建分页请求，按举报数量倒序排列
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "reportCount"));

            // 获取举报数量大于0的帖子
            // 修改 getReportedPosts 方法中的这一行
            Page<Post> postPage = postRepository.findByReportCountGreaterThanOrderByReportCountDesc(0, pageable);

            // 转换为 PostDetailDTO
            List<PostDetailDTO> postDTOs = postPage.getContent().stream().map(post -> {
                PostDetailDTO dto = new PostDetailDTO();
                dto.setId(post.getId());
                dto.setTitle(post.getTitle());
                dto.setContent(post.getContent());
                dto.setTag(post.getTag());
                dto.setIsHideCollege(post.getIsHideCollege());
                dto.setIsHideGrade(post.getIsHideGrade());
                dto.setLikeCount(post.getLikeCount() != null ? post.getLikeCount() : 0);
                dto.setReplyCount(post.getReplyCount() != null ? post.getReplyCount() : 0);
                dto.setReportCount(post.getReportCount() != null ? post.getReportCount() : 0); // 添加举报数量

                // 使用 DateTimeFormatter 格式化 LocalDateTime
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                dto.setCreatedAt(post.getCreatedAt().format(formatter));
                // 获取用户信息
                User user = userRepository.findById(post.getUserId()).orElse(null);
                if (user != null) {
                    dto.setUsername(user.getUsername());
                    dto.setAvatar(user.getAvatar());
                    dto.setGrade(user.getGrade());
                    dto.setCollege(user.getCollege());
                } else {
                    dto.setUsername("未知用户");
                    dto.setAvatar(null);
                    dto.setGrade(null);
                    dto.setCollege(null);
                }

                // 管理员界面不需要这些字段
                dto.setIsLiked(false);
                dto.setIsFavorited(false);

                return dto;
            }).collect(Collectors.toList());

            // 构建响应数据
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", postDTOs);
            response.put("currentPage", postPage.getNumber());
            response.put("reportItems", postPage.getTotalElements());
            response.put("totalPages", postPage.getTotalPages());
            response.put("pageSize", postPage.getSize());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("success", false, "message", "获取举报帖子列表失败: " + e.getMessage()));
        }
    }
}