const KEY_TOKEN = "auth_token";
const KEY_UID = "auth_user_id";
const KEY_NAME = "auth_user_name";
const KEY_AVATAR = "auth_user_avatar";
const KEY_COLLEGE = "auth_user_college";
const KEY_GRADE = "auth_user_grade";

/** 登录成功后保存（如果后端返回 token 就存 token，否则只存 id） */
export function saveAuth({ token, id, username, avatar, college, grade }) {
  if (token) localStorage.setItem(KEY_TOKEN, token);
  if (id) localStorage.setItem(KEY_UID, String(id));
  if (username) localStorage.setItem(KEY_NAME, username);
  if (avatar) localStorage.setItem(KEY_AVATAR, avatar);
  if (college) localStorage.setItem(KEY_COLLEGE, college);
  if (grade) localStorage.setItem(KEY_GRADE, grade);
}

/** 读取 */
export function getAuth() {
  const token = localStorage.getItem(KEY_TOKEN);
  const id = localStorage.getItem(KEY_UID);
  const username = localStorage.getItem(KEY_NAME);
  const avatar = localStorage.getItem(KEY_AVATAR);
  const college = localStorage.getItem(KEY_COLLEGE);
  const grade = localStorage.getItem(KEY_GRADE);
  return { token, id, username, avatar, college, grade };
}

/** 清理 */
export function clearAuth() {
  localStorage.removeItem(KEY_TOKEN);
  localStorage.removeItem(KEY_UID);
  localStorage.removeItem(KEY_NAME);
  localStorage.removeItem(KEY_AVATAR);
  localStorage.removeItem(KEY_COLLEGE);
  localStorage.removeItem(KEY_GRADE);
}

/** 是否已登录（本地标记法；如果改为 Cookie 会话，换成请求 /me 校验） */
export function isAuthed() {
  const { token, id } = getAuth();
  return !!token || !!id;
}