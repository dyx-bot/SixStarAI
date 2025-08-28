import React from "react";
import { getAuth, clearAuth } from "../auth/storage";
import { useMemo, useState, useEffect } from "react";
import {BASE} from "../lib/api"
import axios from "axios";

export default function Profile() {
  const { id } = getAuth();               // 从本地登录态取学工号（auth_user_id）
  const displayId = id || "未登录";
  const [posts, setPosts] = useState([]); // 帖子列表
  const [postCount, setPostCount] = useState(0); // 帖子数
  const [likeCount, setLikeCount] = useState(0); // 点赞数
  const [favoriteCount, setFavoriteCount] = useState(0); // 收藏数

  // 获取用户头像，如果未设置则使用默认头像
  const [avatarUrl, setAvatarUrl] = useState("/bjfu-logo.png"); // 默认头像
  
  // 在组件挂载时获取用户头像
  useEffect(() => {
  if (id) {
    fetch(`${BASE}/api/update/avatar/${id}`) // 这里应该改为 GET 接口
      .then(res => res.json())
      .then(data => {
        if (data.avatar) {
          // 处理相对路径
          const avatarPath = data.avatar;
          const fullAvatarUrl = avatarPath.startsWith("http") 
            ? avatarPath 
            : `${BASE}${avatarPath.startsWith("/") ? "" : "/"}${avatarPath}`;
          setAvatarUrl(fullAvatarUrl);
        }
      })
      .catch(err => {
        console.error("获取头像失败:", err);
      });
  }
}, [id]);

  //帖子
  useEffect(() => {
  if (id) {
    axios.get(`${BASE}/api/users/${id}?currentUserId=${id}`)
      .then(res => {
        const data= res.data;
        setPosts(data.posts||[]);
        setPostCount(data.postCount || 0);
        setLikeCount(data.myLikedPostCount || 0);
        setFavoriteCount(data.favoritePostCount || 0);
      })
      .catch(err => {
        console.error("获取用户信息失败:", err);
        setPostCount(0);
        setLikeCount(0);
        setFavoriteCount(0);
        setPosts([]);
      });
  }
}, [id]);



  function handleSet() {
    window.location.href = "/setting";
  }

  function handleLogout() {
    clearAuth();
    window.location.href = "/login";
  }


  return (
    <div className="me">
      {/* 顶部个人卡片（仅替换文案为动态值，样式不变） */}
      <section className="me-card">
        <div className="me-avatar">
          <img
              src={avatarUrl}
              alt="头像"
              style={{ cursor:"pointer" }}
            />
        </div>
        <div className="me-name">个人中心</div>
        <div className="me-id">学工号：{displayId}</div>

        <div className="me-stats">
          <div>
            <div 
              className="stat-link"
              onClick={() => window.location.href = "/my/likes"}
              style={{ cursor: "pointer" }}
            >
              <b>{likeCount}</b><span>点赞</span>
            </div>
          </div>

          <div>
            <div 
              className="stat-link"
              onClick={() => window.location.href = "/my/favorites"}
              style={{ cursor: "pointer" }}
            >
              <b>{favoriteCount}</b><span>收藏</span>
            </div>
          </div>

          <div>
            <div 
              className="post-link"
              onClick={() => window.location.href = "/my/posts"}
              style={{ cursor: "pointer" }}
            >
              <b>{postCount}</b><span>帖子</span>
            </div>
          </div>
        </div>
      </section>

      {/* 菜单列表（样式保持不变，新增“退出登录”项） */}
      <section className="me-list">
        <button className="me-item" onClick={handleSet}>设置</button>
        <button className="me-item">帮助与反馈</button>
        <button className="me-item">关于</button>
        <button className="me-item" onClick={handleLogout}>退出登录</button>
      </section>

      <style>{`
        .me{display:grid;gap:14px}
        .me-card{
          background:#fff;border-radius:18px;padding:16px;box-shadow:0 1px 0 #edf6f1;
          display:grid;justify-items:center;gap:8px
        }
        .me-avatar{
          width:72px;height:72px;border-radius:999px;display:grid;place-items:center;
          background:linear-gradient(180deg,#7ee2b6,#22a06e);color:#fff;font-weight:900;
          font-size:28px;box-shadow:0 6px 16px rgba(34,160,110,.2)
        }
          
        .me-name{font-weight:900;color:#184a38}
        .me-id{color:#6a8f82;font-size:13px}
        .me-stats{
          width:100%;margin-top:8px;background:#f8fffc;border:1px solid #e6f5ee;border-radius:16px;
          display:grid;grid-template-columns:repeat(3,1fr);text-align:center;padding:12px 6px;gap:6px
        }
        .me-stats b{font-size:22px;color:#184a38}
        .me-stats span{display:block;margin-top:2px;color:#557a6e;font-size:12px}

        .me-list{background:#fff;border-radius:18px;overflow:hidden;border:1px solid #f1f6f3}
        .me-item{width:100%;text-align:left;padding:14px 16px;border:none;background:#fff;color:#184a38;font-size:16px;cursor:pointer}
        .me-item+.me-item{border-top:1px solid #f3f7f4}
        .me-item:active{background:#f7fffb}

        .stat-link {
          cursor: pointer;
          user-select: none;
        }

        .stat-link:hover {
          text-decoration: underline;
        }
      `}</style>
    </div>
  );
}
