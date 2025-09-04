// src/pages/Profile.jsx
import React, { useEffect, useState, useMemo } from "react";
import { getAuth, clearAuth } from "../auth/storage";
import axios from "axios";
import { BASE } from "../lib/api";

/* ========= 稳定随机：同一 id 永远拿到同一个 emoji ========= */
function hashStr(s = "") {
  let h = 0;
  for (let i = 0; i < s.length; i++) {
    h = (h << 5) - h + s.charCodeAt(i);
    h |= 0;
  }
  return Math.abs(h);
}
const EMOJIS = [
  "📚","🖥️","🍜","📷","🎨","🌿","🎧","💼","☕","🍩",
  "🎓","🔬","📱","🧪","⚽","🧭","🍎","📖","💡","🧠",
  "🧋","🍀","🐱","🐼","🚀","🌈","✨","🪴","📒","🧩"
];
function pickEmoji(id) {
  const seed = hashStr(String(id || "guest"));
  return EMOJIS[seed % EMOJIS.length];
}

export default function Profile() {
  const auth = getAuth() || {};
  const userId = auth?.id;
  const displayId = userId || "未登录";

  const [postCount, setPostCount] = useState(0);
  const [likeCount, setLikeCount] = useState(0);       // 你在界面上显示为“点赞”
  const [favoriteCount, setFavoriteCount] = useState(0); // “收藏”

  // 头像：稳定随机 emoji
  const avatarEmoji = useMemo(() => pickEmoji(userId), [userId]);

  // 拉取统计
  useEffect(() => {
    if (!userId) return;
    axios
      .get(`${BASE}/api/users/${userId}?currentUserId=${userId}`)
      .then((res) => {
        const data = res.data || {};
        setPostCount(Number(data.postCount || 0));
        setLikeCount(Number(data.myLikedPostCount || 0));
        setFavoriteCount(Number(data.favoritePostCount || 0));
      })
      .catch((err) => {
        console.error("获取用户信息失败:", err);
        setPostCount(0);
        setLikeCount(0);
        setFavoriteCount(0);
      });
  }, [userId]);

  function handleLogout() {
    clearAuth();
    window.location.href = "/login";
  }

  return (
    <div className="me">
      {/* 顶部个人卡片 */}
      <section className="me-card">
        <div className="me-avatar p2" aria-hidden>
          <span className="em">{avatarEmoji}</span>
        </div>
        <div className="me-name">个人中心</div>
        <div className="me-id">学工号：{displayId}</div>

        <div className="me-stats">
          <div>
            <div
              className="stat-link"
              onClick={() => (window.location.href = "/my/likes")}
            >
              <b>{likeCount}</b>
              <span>点赞</span>
            </div>
          </div>
          <div>
            <div
              className="stat-link"
              onClick={() => (window.location.href = "/my/favorites")}
            >
              <b>{favoriteCount}</b>
              <span>收藏</span>
            </div>
          </div>
          <div>
            <div
              className="post-link"
              onClick={() => (window.location.href = "/my/posts")}
            >
              <b>{postCount}</b>
              <span>帖子</span>
            </div>
          </div>
        </div>
      </section>

      {/* 菜单列表 */}
      <section className="me-list">
        <button className="me-item" onClick={() => (window.location.href = "/setting")}>
          设置
        </button>
        <button className="me-item">帮助与反馈</button>
        <button className="me-item">关于</button>
        <button className="me-item" onClick={handleLogout}>
          退出登录
        </button>
      </section>

      <style>{`
        /* 页面背景与 Discussion 保持一致，并清除任何背景图案（去掉“徽章印”） */
        .me{
          min-height: 100%;
          display: grid;
          gap: 14px;
          background: radial-gradient(120% 90% at 50% -20%,
            #fafffd 0%,
            #f0fff8 35%,
            #e9f7f1 65%,
            #e3f3ed 100%);
          background-image: none !important; /* 防止外部样式插入奇怪的徽章 */
          padding: 12px;
        }

        /* 玻璃卡片风格，强制移除任何背景图 */
        .me-card{
          background: rgba(255,255,255,.78);
          backdrop-filter: blur(12px);
          border-radius: 18px;
          padding: 16px;
          box-shadow: 0 10px 26px rgba(22,107,78,.12);
          display: grid;
          justify-items: center;
          gap: 8px;
          border: 1px solid #e6f5ee;
          background-image: none !important; /* 去掉徽章叠加 */
          overflow: hidden;
        }

        /* p2 风格 emoji 头像 */
        .me-avatar.p2{
          width: 92px;
          height: 92px;
          border-radius: 999px;
          display: grid;
          place-items: center;
          background: radial-gradient(60% 60% at 35% 25%, #f7fff9, #e8f8f0);
          border: 1px solid #e6f5ee;
          box-shadow: inset 0 0 0 4px rgba(126,226,182,.32), 0 8px 20px rgba(22,107,78,.18);
        }
        .me-avatar.p2 .em{
          font-size: 40px;
          line-height: 1;
        }

        .me-name{
          font-weight: 900;
          color: #184a38;
          font-size: 18px;
          letter-spacing: .04em;
        }
        .me-id{ color:#6a8f82; font-size:13px; }

        .me-stats{
          width: 100%;
          margin-top: 2px;
          background: #f8fffc;
          border: 1px solid #e6f5ee;
          border-radius: 16px;
          display: grid;
          grid-template-columns: repeat(3, 1fr);
          text-align: center;
          padding: 12px 6px;
          gap: 6px;
        }
        .me-stats b{ font-size: 22px; color:#184a38; }
        .me-stats span{ display:block; margin-top:2px; color:#557a6e; font-size:12px; }

        .me-list{
          background: rgba(255,255,255,.9);
          backdrop-filter: blur(10px);
          border-radius: 18px;
          overflow: hidden;
          border: 1px solid #eaf5ef;
          box-shadow: 0 8px 22px rgba(22,107,78,.10);
          background-image: none !important; /* 保底消除背景图 */
        }
        .me-item{
          width:100%;
          text-align:left;
          padding:14px 16px;
          border:none;
          background:transparent;
          color:#184a38;
          font-size:16px;
          cursor:pointer;
        }
        .me-item + .me-item{ border-top:1px solid #f3f7f4; }
        .me-item:active{ background:#f7fffb; }

        .stat-link, .post-link{ cursor:pointer; user-select:none; }
        .stat-link:hover, .post-link:hover{ text-decoration: underline; }
      `}</style>
    </div>
  );
}
