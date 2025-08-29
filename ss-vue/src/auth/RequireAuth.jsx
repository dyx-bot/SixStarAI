// src/auth/RequireAuth.jsx
import React from "react";
import { Navigate, useLocation } from "react-router-dom";
import { isAuthed } from "./storage"; // 注意路径：你当前 storage.js 在 auth 目录

export default function RequireAuth({ children }) {
  const ok = isAuthed();
  const location = useLocation();

  if (!ok) {
    return <Navigate to="/login" replace state={{ from: location.pathname }} />;
  }
  console.log("RequireAuth: 检查登录状态");
  console.log("Token 是否存在:", ok);
  console.log("当前路径:", location.pathname);
  return children;
}
