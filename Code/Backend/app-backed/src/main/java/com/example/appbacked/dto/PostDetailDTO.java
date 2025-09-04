/*
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
<<<<<<< HEAD
}*/
// 修改 PostDetailDTO.java
package com.example.appbacked.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.List;

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
    private Integer preportCount; // 添加举报计数
    private Boolean isLiked;
    private Boolean isFavorited;

    // 添加图片URL列表
    private List<String> imageUrls;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdAt;

    // Getters and Setters
    // ... 现有的 getter/setter ...

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Integer getPreportCount() {
        return preportCount;
    }

    public void setPreportCount(Integer preportCount) {
        this.preportCount = preportCount;
    }

    // 在 PostDetailDTO.java 中添加以下字段和方法
    private Integer reportCount;

    public Integer getReportCount() {
        return reportCount;
    }

    public void setReportCount(Integer reportCount) {
        this.reportCount = reportCount;
    }

}