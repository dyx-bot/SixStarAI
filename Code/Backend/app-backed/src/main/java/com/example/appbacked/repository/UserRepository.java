package com.example.appbacked.repository;

import com.example.appbacked.entity.User;
//import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.domain.Page;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository 已经提供了 findById, existsById, save 等标准方法
    // 您可以在这里添加自定义查询方法

    // 统计普通用户数量（排除管理员）
    @Query("SELECT COUNT(u) FROM User u WHERE u.role != 'ADMIN' OR u.role IS NULL")
    long countNormalUsers();

    // 统计各专业人数（排除管理员）
    @Query("SELECT u.grade, COUNT(u) FROM User u WHERE u.role != 'ADMIN' OR u.role IS NULL GROUP BY u.grade")
    List<Object[]> countUsersByGrade();

    // 统计各学院人数（排除管理员）
    @Query("SELECT u.college, COUNT(u) FROM User u WHERE u.role != 'ADMIN' OR u.role IS NULL GROUP BY u.college")
    List<Object[]> countUsersByCollege();

    // 分页获取非管理员用户
    @Query("SELECT u FROM User u WHERE u.role != 'ADMIN' OR u.role IS NULL")
    Page<User> findAllByRoleNotAdmin(Pageable pageable);

    // 添加 existsById 方法的声明（实际上 JpaRepository 已经提供了）
    boolean existsById(Long id);

}
