// src/api/client.js
const BASE = import.meta.env.VITE_API_BASE || "http://10.10.24.41:8080"; // 直连你的后端

export async function request(path, { method = "GET", headers = {}, body, withCredentials = false } = {}) {
  const opts = {method, headers: { "Content-Type": "application/json", ...headers },
  };
  if (withCredentials) opts.credentials = "include";
  if (body !== undefined) opts.body = typeof body === "string" ? body : JSON.stringify(body);

  // 调试：在控制台打印最终 URL
  const url = `${BASE}${path}`;
  console.debug("[API]", method, url, opts);

  const res = await fetch(url, opts);
  const data = await res.json().catch(() => ({}));
  if (!res.ok) {
    const err = new Error(data?.message || `HTTP ${res.status}`);
    err.status = res.status;
    throw err;
  }
  return data;
}
