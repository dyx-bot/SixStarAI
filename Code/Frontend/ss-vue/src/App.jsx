// src/App.jsx
import React from "react";
import { NavLink, Outlet } from "react-router-dom";
import "./App.css";   // 必须引入你的样式

export default function App() {
  return (
    <div className="m-app">
      {/* 顶栏：保持你的结构和类名 */}
      <header className="m-header">
        <div className="m-header__brand">
          <img src="/bjfu-logo.png" alt="logo" />
          <div className="m-header__title">六星问知</div>
        </div>
      </header>

      {/* 主内容区：唯一挂载点 */}
      <main className="m-main">
        <Outlet />
      </main>

      {/* 底部 TabBar：完全使用你的类名与样式 */}
      <nav className="m-tabbar">
        <NavLink
          to="/app/"
          end
          className={({ isActive }) => `m-tab ${isActive ? "m-active" : ""}`}
        >
          问答/AI
        </NavLink>

        <NavLink
          to="/app/discussion"
          className={({ isActive }) => `m-tab ${isActive ? "m-active" : ""}`}
        >
          讨论区
        </NavLink>

        <NavLink
          to="/app/mine"
          className={({ isActive }) => `m-tab ${isActive ? "m-active" : ""}`}
        >
          我的
        </NavLink>
      </nav>
    </div>
  );
}
