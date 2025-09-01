package com.example.appbacked.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreatePostRequest {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;

    private Boolean isHideCollege;
    private Boolean isHideGrade;
    private String tag;

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsHideCollege() {
        return isHideCollege;
    }

    public void setIsHideCollege(Boolean isHideCollege) {
        this.isHideCollege = isHideCollege;
    }

    public Boolean getIsHideGrade() {
        return isHideGrade;
    }

    public void setIsHideGrade(Boolean isHideGrade) {
        this.isHideGrade = isHideGrade;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
