package com.example.appbacked.service;

import com.example.appbacked.entity.User;
import com.example.appbacked.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Value("${upload.avatar-dir:D:/aaa软件/idea/app-backed/uploads}")
    private String uploadDir;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * 用户登录验证
     * @param user 包含 id 和 password 的用户对象
     * @return 是否登录成功，并自动回填用户名、头像、年级、学院
     */
    public boolean login(User user) {
        // ✅ 增加空值检查
        if (user == null) {
            logger.warn("登录请求对象为空");
            return false;
        }

        Long id = user.getId();
        String rawPassword = user.getPassword();

        if (id == null) {
            logger.warn("用户ID为空");
            return false;
        }

        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            logger.warn("用户 {} 提交的密码为空", id);
            return false;
        }

        Optional<User> result = userRepository.findById(id);

        if (result.isPresent()) {
            User dbUser = result.get();
            String storedPassword = dbUser.getPassword();
            String hashedInput = hashWithSHA256(rawPassword);

            // 密码校验
            if (!hashedInput.equalsIgnoreCase(storedPassword)) {
                logger.warn("用户 {} 密码错误", id);
                return false;
            }

            boolean needUpdate = false;

            // 初始化用户名
            if (dbUser.getUsername() == null || dbUser.getUsername().trim().isEmpty()) {
                String defaultName = "用户" + id;
                dbUser.setUsername(defaultName);
                needUpdate = true;
                logger.info("用户 {} 首次登录，使用默认用户名：{}", id, defaultName);
            }

            // 初始化头像
            if (dbUser.getAvatar() == null || dbUser.getAvatar().trim().isEmpty()) {
                String name = dbUser.getUsername();
                String lastName = name.length() <= 2 ? name : name.substring(name.length() - 2);
                String defaultAvatar = "https://api.dicebear.com/7.x/initials/svg?seed=" + lastName +
                        "&backgroundColor=ffab40&color=000000&fontSize=40";
                dbUser.setAvatar(defaultAvatar);
                needUpdate = true;
                logger.info("用户 {} 头像已生成：{}", id, defaultAvatar);
            }

            // ✅ 保存初始化信息（用户名、头像）
            if (needUpdate) {
                userRepository.save(dbUser);
            }

            // ✅ 回填信息到传入的 user 对象（用于返回前端）
            user.setUsername(dbUser.getUsername());
            user.setAvatar(dbUser.getAvatar());
            user.setGrade(dbUser.getGrade());     // 新增：回填年级

            user.setCollege(dbUser.getCollege());
            // ✅ 添加这一行
            user.setRole(dbUser.getRole());  // 把数据库中的 role 回填到传入的 user 对象

            logger.info("用户 {} 登录成功", id);
            return true;
        } else {
            logger.warn("用户 {} 不存在", id);
            return false;
        }
    }

    /**
     * 更新用户信息（支持 username 和 avatar）
     * @param userId 用户ID
     * @param newUsername 新用户名（可为空）
     * @param newAvatar 新头像URL（可为空）
     * @return 更新后的用户对象
     */
    public User updateUser(Long userId, String newUsername, String newAvatar) {
        logger.info("尝试更新用户ID为 {} 的信息", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("用户ID为 {} 的用户不存在", userId);
                    return new IllegalArgumentException("用户不存在");
                });

        boolean updated = false;

        // 更新用户名
        if (newUsername != null && !newUsername.trim().isEmpty()) {
            newUsername = newUsername.trim();

            if (newUsername.length() < 2 || newUsername.length() > 20) {
                throw new IllegalArgumentException("用户名长度应在2-20字符之间");
            }

            if (isSensitiveWord(newUsername)) {
                throw new IllegalArgumentException("用户名包含敏感词");
            }

            user.setUsername(newUsername);
            updated = true;
            logger.info("用户 {} 的用户名已更新为 '{}'", userId, newUsername);
        }

        // 更新头像
        if (newAvatar != null && !newAvatar.trim().isEmpty()) {
            if (newAvatar.length() > 500) {
                throw new IllegalArgumentException("头像URL过长");
            }

            // 如果是Base64数据，处理并保存到指定目录
            if (newAvatar.startsWith("data:image/")) {
                try {
                    newAvatar = processAndSaveAvatar(userId, newAvatar);
                } catch (Exception e) {
                    throw new RuntimeException("头像处理失败: " + e.getMessage(), e);
                }
            }

            user.setAvatar(newAvatar.trim());
            updated = true;
            logger.info("用户 {} 的头像已更新", userId);
        }

        if (!updated) {
            logger.warn("用户 {} 未提供任何更新字段", userId);
            throw new IllegalArgumentException("未提供需要更新的信息");
        }

        // 保存并处理异常
        try {
            userRepository.save(user); // 保存更新
            return user; // 返回更新后的用户（前端需要）
        } catch (Exception e) {
            logger.error("保存用户 {} 时发生数据库错误", userId, e);
            throw new RuntimeException("更新失败，请稍后重试", e);
        }
    }

    /**
     * 【新增】仅更新用户名（供Controller调用）
     * @param userId 用户ID
     * @param newName 新用户名
     * @return 是否更新成功
     */
    public boolean updateName(Long userId, String newName) {
        try {
            User user = userRepository.findById(userId)
                    .orElse(null);
            if (user == null) {
                return false;
            }

            // 复用 updateUser 的用户名校验逻辑
            updateUser(userId, newName, null);
            return true;
        } catch (Exception e) {
            logger.error("更新用户名失败", e);
            return false;
        }
    }

    /**
     * 处理并保存用户头像
     * @param userId 用户ID
     * @param base64Avatar Base64格式的头像数据
     * @return 保存后的头像访问URL
     */
    public String processAndSaveAvatar(Long userId, String base64Avatar) throws IOException {
        if (base64Avatar == null || base64Avatar.trim().isEmpty()) {
            throw new IllegalArgumentException("头像数据不能为空");
        }

        // 检查是否为 Base64 数据 (Data URL)
        if (base64Avatar.startsWith("data:image/")) {
            // 1. 解析 Data URL，提取 MIME 类型和 Base64 数据
            String[] parts = base64Avatar.split(",");
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
            Path uploadPath = Paths.get(uploadDir).normalize(); // 可以是中文路径
            Path filePath = uploadPath.resolve(fileName).normalize();
            Files.createDirectories(uploadPath);
            Files.write(filePath, imageBytes);

            // 返回相对路径（重要！）
            return "/uploads/avatars/" + fileName;
        } else {
            // 如果不是 Data URL，假设它是有效的图片 URL，直接返回
            return base64Avatar;
        }
    }

    /**
     * 更新用户头像
     * @param userId 用户ID
     * @param base64Avatar Base64格式的头像数据
     * @return 更新后的用户对象
     */
    public User updateAvatar(Long userId, String base64Avatar) {
        try {
            // 处理并保存头像
            String avatarUrl = processAndSaveAvatar(userId, base64Avatar);

            // 更新用户信息
            return updateUser(userId, null, avatarUrl);
        } catch (Exception e) {
            logger.error("更新用户头像失败", e);
            throw new RuntimeException("头像更新失败: " + e.getMessage(), e);
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
            logger.error("SHA-256 算法不可用", e);
            throw new RuntimeException("加密失败", e);
        }
    }

    /**
     * 敏感词检查
     * @param username 待检查用户名
     * @return 是否包含敏感词
     */
    private boolean isSensitiveWord(String username) {
        if (username == null) return false;

        String[] badWords = {"admin", "root", "test", "system"};
        String lowerName = username.toLowerCase();
        for (String word : badWords) {
            if (lowerName.contains(word)) {
                return true;
            }
        }
        return false;
    }

    // UserService.java - 添加新方法
    /**
     * 检查用户是否为管理员
     * @param userId 用户ID
     * @return 是否为管理员
     */
    public boolean isAdmin(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.isPresent() && "ADMIN".equals(user.get().getRole());
    }

    /**
     * 获取所有用户（管理员功能）
     * @return 用户列表
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 删除用户（管理员功能）
     * 相当于"拉黑"用户，直接从数据库删除
     * @param userId 用户ID
     */
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }


    /**
     * 更新用户角色（管理员功能）
     * @param userId 用户ID
     * @param role 角色
     */
    public void updateUserRole(Long userId, String role) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setRole(role);
            userRepository.save(user);
        }
    }
}
