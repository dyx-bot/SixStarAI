// src/pages/Login.jsx
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Login.css";
import { loginApi } from "../lib/api"; // 引入优化后的api（已自动处理JSON序列化）
import { saveAuth } from "../auth/storage";
import { ElMessage } from 'element-plus';

export default function Login() {
 
  const [id, setId] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [agree, setAgree] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    // 1. 基础校验（保留你的原有校验逻辑：协议勾选、学工号/密码非空）
    if (!agree) {
      alert("请阅读并同意用户协议");
      return;
    }
    if (!id.trim()) {
      alert("请输入学工号");
      return;
    }
    if (!password.trim()) {
      alert("请输入密码");
      return;
    }

    try {
      setLoading(true); // 显示"登录中"，禁用输入和按钮

      // 2. 调用登录接口（与优化后的api.js兼容：直接传参数，api内自动处理JSON序列化）
      // 无需手动写JSON.stringify，api.js的request函数已自动处理
      const data = await loginApi(id.trim(), password.trim());

      // 3. 后端响应处理（完全保留你的原有逻辑：成功存登录态、跳转首页；失败提示原因）
      if (data?.success) { 
        saveAuth({ 
          token: data.token, 
          userInfo: data.userInfo 
        });
        ElMessage.success('登录成功！');
        console.log("Navigating to /");

        navigate("/app"); // 登录成功跳转到首页路由
      } else {
        alert(`登录失败：${data?.message || "账号或密码错误"}`);
      }
    } catch (err) {
      // 4. 前端请求错误捕获（保留你的错误提示，与api.js的错误信息联动）
      // api.js抛出的错误信息（如地址错、后端没启动、跨域）会在这里显示
      alert(err.message);
    } finally {
      setLoading(false); // 无论成功/失败，都结束"登录中"状态
    }
  };

  // 5. 完全保留你的原有UI结构：学校logo、"六星问知"标题、样式类、交互状态（禁用/加载中）
  return (
    <div className="lx-login">
      <div className="lx-brand">
        <img src="/bjfu-logo.png" alt="北京林业大学" className="school-logo" />
        <h1 className="lx-title">六星问知</h1>
      </div>

      <form className="lx-card" onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="请输入学工号"
          value={id}
          onChange={(e) => setId(e.target.value)}
          className="lx-input"
          disabled={loading} // 加载中禁用输入框
        />
        <input
          type="password"
          placeholder="请输入密码"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="lx-input"
          disabled={loading}
        />
        <button 
          type="submit" 
          className="lx-btn active" 
          disabled={loading}
        >
          {loading ? "登录中…" : "登录"} {/* 加载中显示"登录中…" */}
        </button>
        <div className="lx-agree">
          <input 
            type="checkbox" 
            id="agree" 
            checked={agree}
            onChange={(e) => setAgree(e.target.checked)}
            disabled={loading}
          />
          <label htmlFor="agree">
            我已阅读并同意 <a href="#">用户协议</a>
          </label>
        </div>
      </form>

      
    </div>
  );
}