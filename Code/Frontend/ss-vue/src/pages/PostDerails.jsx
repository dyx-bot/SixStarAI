// src/pages/PostDetail.jsx
import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getAuth } from "../auth/storage";
import { BASE } from "../lib/api";
import "./Discussion.css";

/** 简单的相对时间 */
function timeAgo(ts) {
  if (!ts) return "";
  const diff = (Date.now() - new Date(ts).getTime()) / 1000;
  if (diff < 60) return `${Math.floor(diff)}秒前`;
  if (diff < 3600) return `${Math.floor(diff / 60)}分钟前`;
  if (diff < 86400) return `${Math.floor(diff / 3600)}小时前`;
  if (diff < 86400 * 7) return `${Math.floor(diff / 86400)}天前`;
  return new Date(ts).toLocaleString("zh-CN");
}

/* ===== 稳定随机 emoji ===== */
function hashStr(s) {
  let h = 0;
  for (let i = 0; i < s.length; i++) {
    h = (h << 5) - h + s.charCodeAt(i);
    h |= 0;
  }
  return Math.abs(h);
}
const P2_EMOJIS = [
  "📚","🖥️","🍜","📷","🎨","🌿","🎧","💼",
  "☕","🍩","🎓","🔬","📱","🧪","⚽","🧭","🍎","📖"
];
const pickEmoji = (name = "", id = "") =>
  P2_EMOJIS[hashStr(String(id) + String(name)) % P2_EMOJIS.length];

/* ===== 把后端返回的各种形状统一为前端使用的回复对象 ===== */
function normalizeReply(input) {
  if (!input) return null;
  const r = input.reply || input.data || input; // 兼容 {reply:{}} / {data:{}} / 直接对象
  const id = r.id ?? r.replyId ?? r._id ?? r.tempId; // 兼容不同字段名与临时ID
  return {
    id,
    tempId: r.tempId,               // 仅本地临时用
    userId: r.userId,
    username: r.username || "匿名用户",
    avatar: r.avatar,
    content: r.content || "",
    createdAt: r.createdAt || new Date().toISOString(),
    likeCount: (r.plikeCount ?? r.likeCount ?? 0),
    liked: !!r.liked,
  };
}

/* ===== 统一排序规则：点赞数降序 -> 时间降序 ===== */
function sortReplies(arr) {
  const list = [...(arr || [])].filter(Boolean);
  list.sort((a, b) => {
    const lb = b.likeCount || 0;
    const la = a.likeCount || 0;
    if (lb !== la) return lb - la;
    return new Date(b.createdAt) - new Date(a.createdAt);
  });
  return list;
}

export default function PostDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [post, setPost] = useState(null);
  const [replies, setReplies] = useState([]);
  const [replyContent, setReplyContent] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchAll() {
      try {
        const userId = getAuth().id;

        // 1) 帖子详情
        const resPost = await fetch(`${BASE}/api/posts/${id}?userId=${userId}`);
        const dataPost = await resPost.json();
        setPost(dataPost);

        // 2) 回复列表
        const resReplies = await fetch(`${BASE}/api/replies/post/${id}?userId=${userId}`);
        let dataReplies = await resReplies.json();

        if (Array.isArray(dataReplies)) {
          const normalized = dataReplies.map(normalizeReply);
          setReplies(sortReplies(normalized));
        } else {
          setReplies([]);
        }
      } catch (e) {
        console.error("加载帖子详情失败：", e);
      } finally {
        setLoading(false);
      }
    }
    fetchAll();
  }, [id]);

  const handleReplySubmit = async (e) => {
    e.preventDefault();
    const text = replyContent.trim();
    if (!text) return;

    // —— 先乐观插入一条本地临时回复（立刻可见）——
    const auth = getAuth();
    const userId = auth.id;
    const temp = normalizeReply({
      tempId: `tmp-${Date.now()}`,
      userId,
      username: auth.username || "匿名用户",
      avatar: auth.avatar,
      content: text,
      createdAt: new Date().toISOString(),
      likeCount: 0,
      liked: false,
    });
    setReplies((prev) => sortReplies([temp, ...prev]));
    setReplyContent("");

    // —— 发请求，成功后用真实数据替换临时回复；失败则回滚 —— 
    try {
      setIsSubmitting(true);
      const res = await fetch(`${BASE}/api/replies`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ userId, postId: id, content: text }),
      });

      if (res.ok) {
        const raw = await res.json();
        const real = normalizeReply(raw);
        setReplies((prev) =>
          sortReplies(
            prev.map((r) => (r.tempId && r.tempId === temp.tempId ? real : r))
          )
        );
      } else {
        // 回滚移除临时回复
        setReplies((prev) => prev.filter((r) => r.tempId !== temp.tempId));
        alert("发布失败，请稍后重试");
      }
    } catch (e) {
      setReplies((prev) => prev.filter((r) => r.tempId !== temp.tempId));
      console.error("发布回复失败：", e);
      alert("发布失败，请稍后重试");
    } finally {
      setIsSubmitting(false);
    }
  };

  // 点赞 / 取消点赞 回复
  const handleReplyLike = async (replyId) => {
    try {
      const userId = getAuth().id;
      const res = await fetch(`${BASE}/api/replies/${replyId}/like?userId=${userId}`, {
        method: "POST",
        // headers: {
        //   "Content-Type": "application/json",
        // },
        // body: JSON.stringify({
        //   userId: getAuth().id,
        //   postId: userId, // 当前帖子 ID
        // }),
        });

      if (res.ok) {
        const data = await res.json();
        setReplies((prev) =>
          prev.map((r) =>
            r.id === replyId
              ? {
                  ...r,
                  likeCount:
                    data.likeCount ??
                    (data.liked
                      ? (r.likeCount || 0) + 1
                      : Math.max((r.likeCount || 1) - 1, 0)),
                  liked: data.liked,
                }
              : r
          )
        );
        // 点赞后维持排序一致性（可选）
        setReplies((prev) => sortReplies(prev));
      } else {
        console.error("点赞失败：", res.status);
      }
    } catch (err) {
      console.error("点赞请求出错：", err);
    }
  };

  if (loading) {
    return <div className="lx-card">加载中…</div>;
  }

  return (
    <div className="lx-post-detail">
      <div className="lx-post-detail-header">
        <button className="lx-back-btn" onClick={() => navigate(-1)} aria-label="返回">
          ←
        </button>
        <h2 className="lx-post-detail-title">帖子详情</h2>
      </div>

      <div className="lx-post-detail-body">
        {/* 楼主原帖 */}
        {!post ? (
          <div className="lx-card">原帖不存在或加载失败</div>
        ) : (
          <section className="lx-original-post-card lx-card">
            <div className="lx-op-header">
              <div className="lx-avatar">
                {post.avatar && /^https?:\/\//.test(post.avatar) ? (
                  <img src={post.avatar} alt="头像" />
                ) : (
                  <span>{pickEmoji(post.username, post.id)}</span>
                )}
              </div>
              <div className="lx-op-meta">
                <div className="lx-name">{post.username || "匿名用户"}</div>
                <div className="lx-time">{timeAgo(post.createdAt)}</div>
              </div>
            </div>

            <div className="lx-op-content">{post.content || "（无内容）"}</div>

            <div className="lx-op-actions">
              <div className="lx-op-action">👍 {post.likeCount ?? 0}</div>
              <div className="lx-op-action">💬 {replies.length}</div>
            </div>
          </section>
        )}

        {/* 回复列表 */}
        <h3 className="lx-block-title">回复（{replies.length}）</h3>
        <div className="lx-replies-list">
          {replies.length === 0 ? (
            <div className="lx-empty lx-card">暂无回复</div>
          ) : (
            replies.map((reply) => (
              <div key={reply.id || reply.tempId} className="lx-reply lx-card">
                <div className="lx-reply-header">
                  <div className="lx-avatar">
                    {reply.avatar && /^https?:\/\//.test(reply.avatar) ? (
                      <img src={reply.avatar} alt="头像" />
                    ) : (
                      <span>{pickEmoji(reply.username, reply.userId || reply.id)}</span>
                    )}
                  </div>
                  <div className="lx-reply-info">
                    <div className="lx-reply-author">{reply.username || "匿名用户"}</div>
                    <div className="lx-reply-time">{timeAgo(reply.createdAt)}</div>
                  </div>
                </div>

                <div className="lx-reply-content">{reply.content}</div>

                {/* 点赞 */}
                <div className="lx-reply-actions">
                  <span
                    className={`lx-reply-like ${reply.liked ? "liked" : ""}`}
                    onClick={() => handleReplyLike(reply.id)}
                  >
                    👍 {reply.likeCount || 0}
                  </span>
                </div>
              </div>
            ))
          )}
        </div>

        {/* 回复输入框 */}
        <form onSubmit={handleReplySubmit} className="lx-reply-form lx-card">
          <textarea
            value={replyContent}
            onChange={(e) => setReplyContent(e.target.value)}
            placeholder="回复楼主…"
            rows="3"
            required
          />
          <button type="submit" className="lx-btn" disabled={isSubmitting}>
            {isSubmitting ? "发布中…" : "发布"}
          </button>
        </form>
      </div>
    </div>
  );
}
