// src/pages/Post.jsx
import React, { useState } from "react";
import { getAuth } from "../auth/storage";
import { BASE } from "../lib/api";
import { ElMessage } from "element-plus";

export default function Post() {
  const [form, setForm] = useState({
    title: "",
    content: "",
    tag: "",
    isHideCollege: false,
    isHideGrade: false,
  });
  const [formError, setFormError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  // 发布动态
  const handlePublish = async (e) => {
    e.preventDefault();
    
    // 验证表单
    if (!form.title.trim() || !form.content.trim() || !form.tag.trim()) {
      setFormError("所有内容都必须填写！");
      return;
    }
    setFormError(""); // 清除错误信息

    const { id } = getAuth();
    const postData = {
      userId: id,
      title: form.title,
      content: form.content,
      isHideCollege: form.isHideCollege,
      isHideGrade: form.isHideGrade,
      tag: form.tag,
    };

    try {
      setIsSubmitting(true);
      
      const res = await fetch(`${BASE}/api/posts`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(postData),
      });

      let data = {};
      const contentType = res.headers.get("content-type");
      if (contentType && contentType.includes("application/json")) {
        data = await res.json();
      } else {
        data = { success: true };
      }

      if (res.ok) {
        ElMessage.success("发布成功！");
        // 重置表单
        setForm({ 
          title: "", 
          content: "", 
          tag: "", 
          isHideCollege: false, 
          isHideGrade: false 
        });
        
        // 跳转到讨论页
        window.location.href = "/app/discussion";
      } else {
        alert("发布失败：" + (data.message || "未知错误"));
      }
    } catch (err) {
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
          {/* 标题输入 */}
          <div className="lx-input-group">
            <input
              type="text"
              placeholder="标题"
              value={form.title}
              onChange={(e) => setForm(f => ({ ...f, title: e.target.value }))}
              required
              maxLength={50}
            />
          </div>

          {/* 内容输入 */}
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

          {/* 标签选择 */}
          <div className="lx-tag-select">
            <div className="lx-tag-label">选择标签：</div>
            <div className="lx-tags">
              <button 
                type="button" 
                className={`lx-tag ${form.tag === '学习/资料' ? 'selected' : ''}`}
                onClick={() => setForm(f => ({ ...f, tag: '学习/资料' }))}
              >
                学习/资料
              </button>
              <button 
                type="button" 
                className={`lx-tag ${form.tag === '生活/吐槽' ? 'selected' : ''}`}
                onClick={() => setForm(f => ({ ...f, tag: '生活/吐槽' }))}
              >
                生活/吐槽
              </button>
              <button 
                type="button" 
                className={`lx-tag ${form.tag === '表白/情感' ? 'selected' : ''}`}
                onClick={() => setForm(f => ({ ...f, tag: '表白/情感' }))}
              >
                表白/情感
              </button>
              <button 
                type="button" 
                className={`lx-tag ${form.tag === '求助/攻略' ? 'selected' : ''}`}
                onClick={() => setForm(f => ({ ...f, tag: '求助/攻略' }))}
              >
                求助/攻略
              </button>
              <button 
                type="button" 
                className={`lx-tag ${form.tag === '组队/组局' ? 'selected' : ''}`}
                onClick={() => setForm(f => ({ ...f, tag: '组队/组局' }))}
              >
                组队/组局
              </button>
              <button 
                type="button" 
                className={`lx-tag ${form.tag === '闲置/交易' ? 'selected' : ''}`}
                onClick={() => setForm(f => ({ ...f, tag: '闲置/交易' }))}
              >
                闲置/交易
              </button>
              <button 
                type="button" 
                className={`lx-tag ${form.tag === '寻物/招领' ? 'selected' : ''}`}
                onClick={() => setForm(f => ({ ...f, tag: '寻物/招领' }))}
              >
                寻物/招领
              </button>
            </div>
          </div>

          {/* 隐藏选项 */}
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

          {/* 错误提示 */}
          {formError && <div className="lx-error-message">{formError}</div>}

          {/* 发布按钮 */}
          <button 
            type="submit" 
            className="lx-btn"
            disabled={isSubmitting}
            style={{ width: "100%", marginTop: "16px" }}
          >
            {isSubmitting ? '发布中...' : '发布'}
          </button>
        </form>
      </div>

      <style>{`
        .lx-post-page {
          min-height: 100vh;
          background-color: #f0f4f1;
          display: flex;
          justify-content: center;
          align-items: center;
          padding: 20px;
        }
        
        .lx-shell {
          max-width: 600px;
          margin: 0 auto;
          padding: 16px;
          border-radius: 12px;
          background-color: #fff;
          box-shadow: 0 2px 6px rgba(34, 160, 110, 0.2);
          width: 100%;
          max-width: 400px;
        }

        .lx-header {
          display: flex;
          align-items: center;
          padding: 16px 0;
          border-bottom: 1px solid #e1e9e4;
        }
        
        .lx-back-btn {
          background: none;
          border: none;
          cursor: pointer;
          color: #184a38;
          font-size: 24px;
        }
        
        .lx-page-title {
          flex: 1;
          text-align: center;
          font-size: 18px;
          font-weight: 600;
          color: #184a38;
          margin: 0;
        }
        
        .lx-post-form {
          padding: 16px 0;
        }
        
        .lx-input-group {
          margin-bottom: 16px;
        }
        
        .lx-input-group input,
        .lx-input-group textarea {
          width: 100%;
          padding: 12px;
          border: 1px solid #e1e9e4;
          border-radius: 12px;
          font-size: 14px;
          box-sizing: border-box;
          background: #fff;
        }
        
        .lx-input-group textarea {
          resize: vertical;
          min-height: 120px;
        }
        
        .lx-char-count {
          text-align: right;
          color: #6a8f82;
          font-size: 12px;
          margin-top: 4px;
        }
        
        .lx-tag-select {
          margin-bottom: 16px;
        }
        
        .lx-tag-label {
          font-size: 14px;
          color: #184a38;
          margin-bottom: 8px;
          font-weight: 500;
        }
        
        .lx-tags {
          display: flex;
          flex-wrap: wrap;
          gap: 8px;
        }
        
        .lx-tag {
          padding: 6px 12px;
          border: 1px solid #e1e9e4;
          border-radius: 16px;
          background-color: #f8fffc;
          color: #184a38;
          font-size: 12px;
          cursor: pointer;
          transition: all 0.2s ease;
        }
        
        .lx-tag:hover {
          background-color: #e6f5ee;
        }
        
        .lx-tag.selected {
          background-color: #184a38;
          color: white;
          border-color: #184a38;
        }
        
        .lx-hide-options {
          display: flex;
          gap: 16px;
          margin-bottom: 16px;
          flex-wrap: wrap;
        }
        
        .lx-checkbox {
          display: flex;
          align-items: center;
          gap: 4px;
          font-size: 14px;
          color: #184a38;
        }
        
        .lx-checkbox input[type="checkbox"] {
          width: 16px;
          height: 16px;
          border: 1px solid #e1e9e4;
          border-radius: 4px;
          cursor: pointer;
        }
        
        .lx-checkbox input[type="checkbox"]:checked {
          background-color: #184a38;
          border-color: #184a38;
        }
        
        .lx-error-message {
          color: #e74c3c;
          font-size: 14px;
          margin-bottom: 16px;
          padding: 8px;
          background-color: #ffeaea;
          border-radius: 4px;
        }
        
        .lx-btn {
          padding: 12px 20px;
          background: linear-gradient(180deg, #7ee2b6, #22a06e);
          color: #fff;
          border: none;
          border-radius: 12px;
          font-size: 16px;
          font-weight: 500;
          cursor: pointer;
          box-shadow: 0 2px 6px rgba(34, 160, 110, 0.2);
        }
        
        .lx-btn:hover:not(:disabled) {
          opacity: 0.9;
        }
        
        .lx-btn:disabled {
          opacity: 0.6;
          cursor: not-allowed;
        }
      `}</style>
    </div>
  );
}