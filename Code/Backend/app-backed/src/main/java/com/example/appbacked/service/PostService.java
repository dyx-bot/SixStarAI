package com.example.appbacked.service;

import com.example.appbacked.dto.PostDetailDTO;
import com.example.appbacked.entity.*;
import com.example.appbacked.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    // 添加 logger 声明
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    // 在 PostService 类中添加以下字段（如果还没有的话）
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private ReplyRepository replyRepository;

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository,
                       UserRepository userRepository,
                       LikeRepository likeRepository,
                       FavoriteRepository favoriteRepository,
                       ReplyRepository replyRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.favoriteRepository = favoriteRepository;
        this.replyRepository = replyRepository;
    }

    // 获取所有帖子
    @Transactional(readOnly = true)
    public List<PostDetailDTO> getAllPosts(Long currentUserId) {
        List<Post> posts = postRepository.findAll();
        return convertToDTOs(posts, currentUserId);
    }

    // 获取某用户发布的所有帖子
    @Transactional(readOnly = true)
    public List<PostDetailDTO> getPostsByUserId(Long userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        return convertToDTOs(posts, userId);
    }

    // 获取某用户收藏的帖子
    @Transactional(readOnly = true)
    public List<PostDetailDTO> getFavoritePostsByUserId(Long userId) {
        List<Post> posts = postRepository.findPostsByFavoriteUserId(userId);
        return convertToDTOs(posts, userId);
    }

    // 获取某用户点赞的帖子
    @Transactional(readOnly = true)
    public List<PostDetailDTO> getLikedPostsByUserId(Long userId) {
        List<Post> posts = postRepository.findPostsByLikeUserId(userId);
        return convertToDTOs(posts, userId);
    }

    // 转换为 DTO 并填充点赞/收藏状态
    private List<PostDetailDTO> convertToDTOs(List<Post> posts, Long currentUserId) {
        return posts.stream().map(post -> {
            User user = userRepository.findById(post.getUserId()).orElse(null);
            boolean isLiked = likeRepository.findByUserIdAndPostId(currentUserId, post.getId()).isPresent();
            boolean isFavorited = favoriteRepository.findByUserIdAndPostId(currentUserId, post.getId()).isPresent();

            PostDetailDTO dto = new PostDetailDTO();
            dto.setId(post.getId());
            dto.setTitle(post.getTitle());
            dto.setContent(post.getContent());
            dto.setIsHideCollege(post.getIsHideCollege());
            dto.setIsHideGrade(post.getIsHideGrade());
            dto.setTag(post.getTag());

            // 设置用户信息
            if (user != null) {
                dto.setUsername(user.getUsername());
                dto.setAvatar(user.getAvatar());

                // 根据隐藏设置决定是否显示年级和学院
                if (Boolean.TRUE.equals(post.getIsHideGrade())) {
                    dto.setGrade(null);
                } else {
                    dto.setGrade(user.getGrade());
                }

                if (Boolean.TRUE.equals(post.getIsHideCollege())) {
                    dto.setCollege(null);
                } else {
                    dto.setCollege(user.getCollege());
                }
            } else {
                dto.setUsername("未知用户");
                dto.setAvatar(null);
                dto.setGrade(null);
                dto.setCollege(null);
            }

            // 使用数据库中存储的计数
            dto.setLikeCount(post.getLikeCount());
            dto.setReplyCount(post.getReplyCount());
            dto.setIsLiked(isLiked);
            dto.setIsFavorited(isFavorited);
            dto.setCreatedAt(post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            return dto;
        }).collect(Collectors.toList());
    }

    // 将单个 Post 转换为 DTO 的方法（供 Controller 复用）
    public PostDetailDTO convertToDTO(Post post, Long currentUserId) {
        List<Post> posts = List.of(post);
        List<PostDetailDTO> dtos = convertToDTOs(posts, currentUserId);
        return dtos.isEmpty() ? null : dtos.get(0);
    }

    // 更新帖子的点赞数
    @Transactional
    public void updatePostLikeCount(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            Long likeCount = likeRepository.countByPostId(postId);
            post.setLikeCount(likeCount.intValue());
            postRepository.save(post);
        }
    }

    // 更新帖子的评论数
    @Transactional
    public void updatePostReplyCount(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            Long replyCount = replyRepository.countByPostId(postId);
            post.setReplyCount(replyCount.intValue());
            postRepository.save(post);
        }
    }

    /**
     * 删除帖子（仅允许帖子作者删除）
     * @param postId 帖子ID
     * @param userId 当前用户ID
     */
    @Transactional
    public void deletePost(Long postId, Long userId) {
        logger.info("尝试删除帖子ID为 {} 的帖子，操作用户ID: {}", postId, userId);

        // 验证帖子是否存在
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            logger.warn("帖子ID为 {} 的帖子不存在", postId);
            throw new IllegalArgumentException("帖子不存在");
        }

        // 验证是否为帖子作者
        if (!post.getUserId().equals(userId)) {
            logger.warn("用户 {} 试图删除不属于自己的帖子 {}", userId, postId);
            throw new IllegalArgumentException("只能删除自己发布的帖子");
        }

        try {
            // 删除帖子相关的点赞、收藏、回复等数据
            logger.info("删除帖子 {} 相关的点赞数据", postId);
            likeRepository.deleteByPostId(postId);

            logger.info("删除帖子 {} 相关的收藏数据", postId);
            favoriteRepository.deleteByPostId(postId);

            logger.info("删除帖子 {} 相关的回复数据", postId);
            replyRepository.deleteByPostId(postId);

            // 删除帖子
            logger.info("删除帖子 {}", postId);
            postRepository.deleteById(postId);

            logger.info("帖子 {} 删除成功", postId);
        } catch (Exception e) {
            logger.error("删除帖子 {} 失败", postId, e);
            throw new RuntimeException("删除帖子失败: " + e.getMessage());
        }
    }
}
