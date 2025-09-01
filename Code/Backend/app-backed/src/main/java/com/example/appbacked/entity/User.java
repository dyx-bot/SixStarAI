package com.example.appbacked.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "password", nullable = false, length = 64)
    @NotNull(message = "密码不能为空")
    @JsonIgnore  // ✅ 安全：序列化时不返回密码（防止暴露）
    private String password;

    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "avatar", length = 500)
    private String avatar;

    @Column(name = "grade", length = 20)
    private String grade;

    @Column(name = "college", length = 50)
    private String college;


    // 添加角色字段
    @Column(name = "role", length = 20)
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    // 无参构造
    public User() {}

    // 全参构造（可选）
    public User(Long id, String password, String username, String avatar, String grade, String college) {
        this.id = id;
        this.password = password;
        this.username = username;
        this.avatar = avatar;
        this.grade = grade;
        this.college = college;
    }

    // ======== Getter 和 Setter ========

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore  // ✅ 重要：确保序列化时不输出密码
    public String getPassword() {
        return password;
    }

    /**
     * 使用 @JsonProperty 允许从 JSON 反序列化 password（如登录请求）
     * 同时保留 @JsonIgnore 在 getter 上，防止密码被返回
     */
    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    // ======== toString() 方法（可选，便于调试） ========

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                ", grade='" + grade + '\'' +
                ", college='" + college + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}