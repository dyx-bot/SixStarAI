package com.example.appbacked.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class PostDetailDTO {
    private Long id;
    private String title;
    private String content;
    private Boolean isHideCollege;
    private Boolean isHideGrade;
    private String tag;
    private String username;
    private String avatar;
    private String grade;
    private String college;
    private Integer likeCount;
    private Integer replyCount;
    private Boolean isLiked;
    private Boolean isFavorited;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdAt;
}