import React from "react";
import { getAuth, clearAuth } from "../auth/storage";

export default function Profile() {
  const { uid } = getAuth();               // 从本地登录态取学工号（auth_user_id）
  const displayId = uid || "未登录";
  const avatarChar = (uid && String(uid).charAt(0)) || "用"; // 头像里的字，取学工号首字

  function handleLogout() {
    clearAuth();
    window.location.href = "/login";
  }

  return (
    <div className="me">
      {/* 顶部个人卡片（仅替换文案为动态值，样式不变） */}
      <section className="me-card">
        <div className="me-avatar">{avatarChar}</div>
        <div className="me-name">个人中心</div>
        <div className="me-id">学工号：{displayId}</div>

        <div className="me-stats">
          <div><b>24</b><span>点赞</span></div>
          <div><b>18</b><span>收藏</span></div>
          <div><b>12</b><span>帖子</span></div>
        </div>
      </section>

      {/* 菜单列表（样式保持不变，新增“退出登录”项） */}
      <section className="me-list">
        <button className="me-item">设置</button>
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
      `}</style>
    </div>
  );
}
