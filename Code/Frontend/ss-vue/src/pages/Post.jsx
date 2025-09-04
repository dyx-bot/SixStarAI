// src/pages/Post.jsx
import React, { useState } from "react";
import { getAuth } from "../auth/storage";
import { BASE } from "../lib/api";
import { ElMessage } from "element-plus";

function makeAutoTitle(content, tag) {
  const base = (content || "").trim().replace(/\s+/g, " ");
  // 取内容前 20 个字符作标题；没有内容就用 “标签 · 无标题”
  const t = base ? base.slice(0, 20) : "";
  return t || `${tag || "动态"} · 无标题`;
}

function pickErrMsg(data) {
  // 兼容常见的 Spring Validation 结构
  if (data?.errors?.length) {
    const e = data.errors[0];
    return e.defaultMessage || e.message || JSON.stringify(e);
  }
  return data?.message || "";
}

export default function Post() {
  const [form, setForm] = useState({
    content: "",
    tag: "",
    isHideCollege: false,
    isHideGrade: false,
  });
  const [formError, setFormError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handlePublish = async (e) => {
    e.preventDefault();

    if (!form.content.trim() || !form.tag.trim()) {
      setFormError("内容和标签必须填写！");
      return;
    }
    setFormError("");

    const auth = getAuth() || {};
    const userId = auth.id;
    if (!userId) {
      alert("未登录或登录已过期");
      return;
    }

    // ★ 自动生成标题，满足后端校验
    const title = makeAutoTitle(form.content, form.tag);

    const postData = {
      userId,
      title,                           // ★ 关键：提交 title
      content: form.content,
      tag: form.tag,
      isHideCollege: form.isHideCollege,
      isHideGrade: form.isHideGrade,
    };

    try {
      setIsSubmitting(true);

      const res = await fetch(`${BASE}/api/posts`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(postData),
      });

      let data = {};
      const ct = res.headers.get("content-type") || "";
      if (ct.includes("application/json")) {
        data = await res.json().catch(() => ({}));
      }

      if (res.ok) {
        ElMessage.success("发布成功！");
        setForm({
          content: "",
          tag: "",
          isHideCollege: false,
          isHideGrade: false,
        });
        window.location.href = "/app/discussion";
      } else {
        console.error("POST /api/posts error:", data);
        const msg = pickErrMsg(data) || "未知错误";
        alert("发布失败：" + msg);
      }
    } catch (err) {
      console.error(err);
      alert("请求失败：" + err.message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="lx-post-page">
      <div className="lx-shell">
        <div className="lx-header">
          <button className="lx-back-btn" onClick={() => window.history.back()}>
            <svg viewBox="0 0 24 24" width="24" height="24">
              <path d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z" fill="currentColor"/>
            </svg>
          </button>
          <h1 className="lx-page-title">发布动态</h1>
        </div>

        <form onSubmit={handlePublish} className="lx-post-form">
          {/* 内容 */}
          <div className="lx-input-group">
            <textarea
              placeholder="内容"
              value={form.content}
              onChange={(e) => setForm(f => ({ ...f, content: e.target.value }))}
              required
              maxLength={140}
              rows={6}
            />
            <div className="lx-char-count">{form.content.length}/140</div>
          </div>

          {/* 标签 */}
          <div className="lx-tag-select">
            <div className="lx-tag-label">选择标签：</div>
            <div className="lx-tags">
              {["学习/资料","生活/吐槽","表白/情感","求助/攻略","组队/组局","闲置/交易","寻物/招领"].map(t => (
                <button
                  key={t}
                  type="button"
                  className={`lx-tag ${form.tag === t ? "selected" : ""}`}
                  onClick={() => setForm(f => ({ ...f, tag: t }))}
                >
                  {t}
                </button>
              ))}
            </div>
          </div>

          {/* 隐私选项 */}
          <div className="lx-hide-options">
            <label className="lx-checkbox">
              <input
                type="checkbox"
                checked={form.isHideCollege}
                onChange={(e) => setForm(f => ({ ...f, isHideCollege: e.target.checked }))}
              />
              <span>隐藏学院</span>
            </label>
            <label className="lx-checkbox">
              <input
                type="checkbox"
                checked={form.isHideGrade}
                onChange={(e) => setForm(f => ({ ...f, isHideGrade: e.target.checked }))}
              />
              <span>隐藏年级</span>
            </label>
          </div>

          {formError && <div className="lx-error-message">{formError}</div>}

          <button
            type="submit"
            className="lx-btn"
            disabled={isSubmitting}
            style={{ width: "100%", marginTop: "16px" }}
          >
            {isSubmitting ? "发布中..." : "发布"}
          </button>
        </form>
      </div>

      <style>{`
        .lx-post-page {
          min-height: 100vh;
          display: flex;
          justify-content: center;
          align-items: center;
          padding: 20px;
          background: radial-gradient(120% 90% at 50% -20%,
            #fafffd 0%,
            #f0fff8 35%,
            #e9f7f1 65%,
            #e3f3ed 100%);
        }
        .lx-shell {
          max-width: 400px;
          width: 100%;
          margin: 0 auto;
          padding: 16px;
          border-radius: 16px;
          background: rgba(255,255,255,.7);
          backdrop-filter: blur(12px);
          box-shadow: 0 8px 24px rgba(34,160,110,.15);
        }
        .lx-header {
          display: flex;
          align-items: center;
          padding: 16px 0;
          border-bottom: 1px solid rgba(225,233,228,.6);
        }
        .lx-back-btn { background:none; border:none; cursor:pointer; color:#184a38; font-size:24px; }
        .lx-page-title { flex:1; text-align:center; font-size:18px; font-weight:600; color:#184a38; margin:0; }
        .lx-post-form { padding: 16px 0; }
        .lx-input-group { margin-bottom: 16px; }
        .lx-input-group textarea {
          width:100%; padding:12px; border:1px solid #e1e9e4; border-radius:12px;
          font-size:14px; box-sizing:border-box; background:#fff; resize:vertical; min-height:120px;
        }
        .lx-char-count { text-align:right; color:#6a8f82; font-size:12px; margin-top:4px; }
        .lx-tag-select { margin-bottom:16px; }
        .lx-tag-label { font-size:14px; color:#184a38; margin-bottom:8px; font-weight:500; }
        .lx-tags { display:flex; flex-wrap:wrap; gap:8px; }
        .lx-tag {
          padding:6px 12px; border:1px solid #e1e9e4; border-radius:16px; background:#f8fffc;
          color:#184a38; font-size:12px; cursor:pointer; transition:all .2s ease;
        }
        .lx-tag:hover { background:#e6f5ee; }
        .lx-tag.selected { background:#184a38; color:#fff; border-color:#184a38; }
        .lx-hide-options { display:flex; gap:16px; margin-bottom:16px; flex-wrap:wrap; }
        .lx-checkbox { display:flex; align-items:center; gap:4px; font-size:14px; color:#184a38; }
        .lx-checkbox input[type="checkbox"] { width:16px; height:16px; border:1px solid #e1e9e4; border-radius:4px; cursor:pointer; }
        .lx-checkbox input[type="checkbox"]:checked { background:#184a38; border-color:#184a38; }
        .lx-error-message { color:#e74c3c; font-size:14px; margin-bottom:16px; padding:8px; background:#ffeaea; border-radius:4px; }
        .lx-btn {
          padding:12px 20px; background:linear-gradient(180deg,#7ee2b6,#22a06e);
          color:#fff; border:none; border-radius:12px; font-size:16px; font-weight:500;
          cursor:pointer; box-shadow:0 2px 6px rgba(34,160,110,.2);
        }
        .lx-btn:hover:not(:disabled){ opacity:.9; }
        .lx-btn:disabled{ opacity:.6; cursor:not-allowed; }
      `}</style>
    </div>
  );
}
