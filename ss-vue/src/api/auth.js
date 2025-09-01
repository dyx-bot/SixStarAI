// src/lib/api.js
import { request } from "../api/client";

/**
 * 登录接口（你后端的约定）
 * POST /api/login
 * body: { id: number, password: string }
 * resp: { success: boolean, message?: string, token?: string, user?: any }
 */
export async function loginApi(id, password) {
  return request("/api/login", {
    method: "POST",
    body: { id, password }
    // 如果后端改为 Cookie 会话，把 withCredentials: true 打开：
    // , withCredentials: true
  });
}

/** 可选：退出接口（如果你后端提供的话） */
export async function logoutApi() {
  return request("/api/logout", { method: "POST" });
}

/** 可选：获取当前用户（如果你后端提供 /api/auth/me） */
export async function fetchMe() {
  return request("/api/auth/me", {
    method: "GET",
    // withCredentials: true
  });
}
