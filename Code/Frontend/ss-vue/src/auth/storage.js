// src/auth/storage.js

const KEY_TOKEN = "auth_token";
const KEY_UID = "auth_user_id";

/** 登录成功后保存（如果后端返回 token 就存 token，否则只存 id） */
export function saveAuth({ token, id }) {
  if (token) localStorage.setItem(KEY_TOKEN, token);
  if (id) localStorage.setItem(KEY_UID, String(id));
}

/** 读取 */
export function getAuth() {
  const token = localStorage.getItem(KEY_TOKEN);
  const uid = localStorage.getItem(KEY_UID);
  return { token, uid };
}

/** 清理 */
export function clearAuth() {
  localStorage.removeItem(KEY_TOKEN);
  localStorage.removeItem(KEY_UID);
}

/** 是否已登录（本地标记法；如果改为 Cookie 会话，换成请求 /me 校验） */
export function isAuthed() {
  const { token, uid } = getAuth();
  return !!token || !!uid;
}
