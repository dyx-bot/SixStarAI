import React, { useState, useEffect, useRef } from "react";
import { getAuth } from "../auth/storage";
import { uploadAvatarApi } from "../lib/api";
import "./Setting.css";
import {BASE} from "../lib/api"

export default function Setting() {
  // ✅ 直接从 getAuth() 中解构字段
  const { id, username, avatar, college, grade } = getAuth();

  const isLoggedIn = !!username;

  const [isEditingUsername, setIsEditingUsername] = useState(false);
  const [usernameText, setUsernameText] = useState(username || "");
  const [avatarUrl, setAvatarUrl] = useState(avatar || "/bjfu-logo.png");
  const [uploading, setUploading] = useState(false);
  const fileInputRef = useRef(null);

  // 监听登录状态变化
  useEffect(() => {
    const { username, avatar } = getAuth();
    if (username) {
      setUsernameText(username);
    }
    if (avatar) {
      setAvatarUrl(avatar);
    }
    console.log("auth:", getAuth());
  }, []);

  


  // 监听回车或失焦保存
  const handleUsernameInputKeyDown = (e) => {
    if (e.key === "Enter") {
      handleUsernameSave();
    }
  };

  // 触发文件选择
  const updateAvatar = () => {
    if (!isLoggedIn) return;
    fileInputRef.current?.click();
  };

  // 处理文件选择
 // 处理文件选择
// ...existing code...

const handleFileChange = async (e) => {
  const file = e.target.files[0];
  if (!file) return;
  setUploading(true);

  // 1. 读取为 base64
  const reader = new FileReader();
  reader.onload = async function(evt) {
    const base64 = evt.target.result; // 形如 data:image/png;base64,xxxx
    try {
      const res = await fetch(`${BASE}/api/update/avatar/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ avatar: base64 })
      });
      const data = await res.json();
      if (data.success) {
        let avatarPath = data.avatar;
if (avatarPath && !avatarPath.startsWith("http") && !avatarPath.startsWith("data:")) {
        // 只返回了文件名，自动拼接
        avatarPath = "/uploads/avatar/" + avatarPath;
      }
        setAvatarUrl(data.avatar || base64); // 优先用后端返回，否则用本地base64
        // 可选：同步到localStorage
        localStorage.setItem("auth_user_avatar", data.avatar || base64);

      } else {
        alert("上传失败：" + (data.message || "未知错误"));
      }
    } catch (err) {
      alert("上传失败：" + err.message);
    } finally {
      setUploading(false);
    }
  };
  reader.readAsDataURL(file);
};

const handleUsernameSave = async() => {
  setIsEditingUsername(false);
  if (!usernameText.trim()) {
    alert("用户名不能为空！");
    return;
  }
  try {
    const res = await fetch(`${BASE}/api/update/name/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username: usernameText })
    });
    const data = await res.json();
    if (data.success) {
      alert("用户名已修改为：" + usernameText);
      // 同步到localStorage
      localStorage.setItem("auth_user_name", usernameText);
    } else {
      alert("修改失败：" + (data.message || "未知错误"));
    }
  } catch (err) {
    alert("修改失败：" + err.message);
  }
};

// ...existing code...

  return (
    <div className="setting">
      <div className="setting-container">
        <section className="setting-card">
          {/* 头像 */}
          <div className="setting-avatar-wrapper">
            <img
              src={avatarUrl}
              alt="头像"
              className={`setting-avatar ${uploading ? 'uploading' : ''}`}
              onClick={updateAvatar}
              style={{ cursor: isLoggedIn ? "pointer" : "default" }}
            />
            {uploading && (
              <div className="upload-loading">上传中...</div>
            )}
            <input
              type="file"
              ref={fileInputRef}
              accept="image/*"
              onChange={handleFileChange}
              style={{ display: "none" }}
            />
          </div>

          {/* 账号 */}
          <div className="setting-item">
            <span className="label">账号</span>
            <span className="value">{id || "未设置"}</span>
          </div>

          {/* 用户名 */}
          <div className="setting-item" prop="name">
            <span className="label">用户名</span>
            {isEditingUsername ? (
              <input
                type="text"
                className="setting-username-input"
                value={usernameText}
                onChange={e => setUsernameText(e.target.value)}
                onBlur={handleUsernameSave}
                onKeyDown={handleUsernameInputKeyDown}
                autoFocus
                disabled={!isLoggedIn}
                placeholder="请输入用户名"
              />
            ) : (
              <span
                className="value setting-username-text"
                onClick={() => isLoggedIn && setIsEditingUsername(true)}
                style={{ cursor: isLoggedIn ? "pointer" : "default" }}
                title="点击修改用户名"
              >
                {usernameText || "未设置"}
              </span>
            )}
          </div>

          {/* 学院 */}
          <div className="setting-item">
            <span className="label">学院</span>
            <span className="value">{college || "未设置"}</span>
          </div>

          {/* 年级 */}
          <div className="setting-item">
            <span className="label">年级</span>
            <span className="value">{grade || "未设置"}</span>
          </div>

          
        </section>

        <a href="/app/mine" className="back-link">
          返回个人中心
        </a>
      </div>
    </div>
  );
}