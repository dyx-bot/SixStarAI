// src/App.jsx
import React from "react";
import { NavLink, Outlet } from "react-router-dom";
import "./App.css";

export default function App() {
  return (
    <div className="m-app">
      {/* 顶栏 */}
      <header className="m-header">
        <div className="m-header__brand">
          <img src="/bjfu-logo.png" alt="logo" />
          <div className="m-header__title">林小问</div>
        </div>
      </header>

      {/* 主内容区 */}
      <main className="m-main">
        <Outlet />
      </main>

      {/* 底部 TabBar：扁扁的 + 图标 */}
      <nav className="m-tabbar" aria-label="底部导航">
       {/* 问答 / AI */}
<NavLink
  to="/app/"
  end
  aria-label="问答AI"
  className={({ isActive }) => `m-tab ${isActive ? "m-active" : ""}`}
>
  {/* 机器人“头”图标（无身体） */}
  <svg
    className="m-tab__icon"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    strokeWidth="1.8"
    strokeLinecap="round"
    strokeLinejoin="round"
    aria-hidden="true"
  >
    {/* 天线 */}
    <circle cx="12" cy="2.6" r="1.2" />
    <path d="M12 3.9v1.4" />

    {/* 头壳（圆角矩形） */}
    <rect x="5" y="6" width="14" height="11" rx="3" />

    {/* 耳朵（两侧小矩形） */}
    <rect x="3.6" y="9" width="1.8" height="5" rx="0.9" />
    <rect x="18.6" y="9" width="1.8" height="5" rx="0.9" />

    {/* 眼睛 */}
    <circle cx="9"  cy="11" r="1.3" />
    <circle cx="15" cy="11" r="1.3" />

    {/* 嘴巴 */}
    <path d="M9 14.5h6" />
  </svg>
</NavLink>


{/* 讨论区 */}
<NavLink
  to="/app/discussion"
  aria-label="讨论区"
  className={({ isActive }) => `m-tab ${isActive ? "m-active" : ""}`}
>
  {/* 俏皮双层对话气泡 */}
  <svg
    className="m-tab__icon"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    strokeWidth="1.8"
    strokeLinecap="round"
    strokeLinejoin="round"
    aria-hidden="true"
  >
    {/* 前景大气泡 + 小尾巴 */}
    <rect x="3" y="7" width="14" height="9.5" rx="3" />
    <path d="M9 16.5v2.8l2.8-2.8" />

    {/* 背景小气泡（叠一层更有“社交感”） */}
    <rect x="13.5" y="3.8" width="6.8" height="5.2" rx="2" />
    <path d="M18.1 9l1.2-.8" />

    {/* 说话小点点 */}
    <circle cx="7.4" cy="11.7" r="1" />
    <circle cx="11"  cy="11.7" r="1" />
    <circle cx="14.6" cy="11.7" r="1" />
  </svg>
</NavLink>


        {/* 我的 */}
        <NavLink
          to="/app/mine"
          aria-label="我的"
          className={({ isActive }) => `m-tab ${isActive ? "m-active" : ""}`}
        >
          {/* user */}
          <svg className="m-tab__icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
            <path d="M20 21a8 8 0 0 0-16 0"/>
            <circle cx="12" cy="7" r="4"/>
          </svg>
        </NavLink>
      </nav>
    </div>
  );
}
