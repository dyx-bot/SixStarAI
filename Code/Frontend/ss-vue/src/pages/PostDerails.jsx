// src/pages/PostDetail.jsx
import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getAuth } from "../auth/storage";
import { BASE } from "../lib/api";
import "./Discussion.css"; // 复用样式

export default function PostDetail() {
  const { id } = useParams(); // 获取帖子ID
  const navigate = useNavigate();
  const [post, setPost] = useState(null);
  const [replies, setReplies] = useState([]);
  const [replyContent, setReplyContent] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [loading, setLoading] = useState(true);
// 复用 Discussion 页的居中宽度样式
// 让帖子详情内容宽度与 Discussion 一致并居中
// 你可以直接在 Discussion.css 里定义 
  useEffect(() => {
    // 获取帖子详情
    const fetchPostDetail = async () => {
      try {
        const userId = getAuth().id;
        const res = await fetch(`${BASE}/api/posts/${id}?userId=${userId}`);
        const data = await res.json();
        if (data.success) {
          setPost(data.data);
        }
      } catch (err) {
        console.error("获取帖子详情失败:", err);
      }
    };

    // 获取回复列表 - 根据您的接口信息调整
    const fetchReplies = async () => {
      try {
        const res = await fetch(`${BASE}/api/replies/post/${id}`); // 使用正确的接口路径
        const data = await res.json();
        
        if (Array.isArray(data)) {
          // 按时间倒序排列（最新的在前）
          const sortedReplies = data.sort((a, b) => 
            new Date(b.createdAt) - new Date(a.createdAt)
          );
          setReplies(sortedReplies);
        }
        setLoading(false);
      } catch (err) {
        console.error("获取回复列表失败:", err);
        setLoading(false);
      }
    };

    fetchPostDetail();
    fetchReplies();
  }, [id]);

  const handleReplySubmit = async (e) => {
    e.preventDefault();
    
    if (!replyContent.trim()) return;

    try {
      setIsSubmitting(true);
      
      const userId = getAuth().id;
      const replyData = {
        userId,
        postId: id,
        content: replyContent,
      };

      const res = await fetch(`${BASE}/api/replies`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(replyData),
      });

      if (res.ok) {
        const newReply = await res.json();
        // 将新回复添加到列表顶部
        setReplies(prev => [newReply, ...prev]);
        setReplyContent("");
        window.location.reload(); // 刷新页面以显示最新回复
      }
    } catch (err) {
      console.error("回复失败:", err);
      alert("回复失败，请重试");
    } finally {
      setIsSubmitting(false);
    }
  };

  if (loading) {
    return (
      <div className="lx-post-detail">
        <div className="lx-post-detail-header">
          <button className="lx-back-btn" onClick={() => navigate(-1)}>
            <svg viewBox="0 0 24 24" width="24" height="24">
              <path d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z" fill="currentColor"/>
            </svg>
          </button>
          <h2 className="lx-post-detail-title">加载中...</h2>
        </div>
      </div>
    );
  }

  return (
    <div className="lx-post-detail">
      <div className="lx-post-detail-header">
        <button className="lx-back-btn" onClick={() => navigate(-1)}>
          <svg viewBox="0 0 24 24" width="24" height="24">
            <path d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z" fill="currentColor"/>
          </svg>
        </button>
        <h2 className="lx-post-detail-title">帖子详情</h2>
      </div>

      <div className="lx-post-detail-body">
        {post && (
          <div className="lx-post-detail-author">
            <div className="lx-avatar" aria-hidden>
              {
                /^https?:\/\//.test(post.avatar)
                  ? <img src={post.avatar} alt="头像" style={{width: "100%", height: "100%", borderRadius: "50%"}} />
                  : post.avatar
              }
            </div>
            <div className="lx-author-info">
              <div className="lx-name">{post.username}</div>
              <div className="lx-meta-group">
                <span className="lx-meta">{post.college}</span>
                <span className="lx-meta">{post.grade}</span>
              </div>
            </div>
          </div>
        )}

        <div className="lx-post-content">
          {post?.content}
        </div>

        <div className="lx-post-detail-replies">
          <h3>回复 ({replies.length})</h3>
          <div className="lx-replies-list">
            {replies.length === 0 ? (
              <div className="lx-empty">暂无回复，快来抢沙发吧~</div>
            ) : (
              replies.map(reply => (
                <div key={reply.id} className="lx-reply">
                  <div className="lx-reply-header">
                    <div className="lx-avatar" aria-hidden>
                      {reply.avatar || "👤"}
                    </div>
                    <div className="lx-reply-info">
                      <div className="lx-reply-author">{reply.username || "匿名用户"}</div>
                      <div className="lx-reply-time">
                        {new Date(reply.createdAt).toLocaleString('zh-CN')}
                      </div>
                    </div>
                  </div>
                  <div className="lx-reply-content">{reply.content}</div>
                  
                </div>
              ))
            )}
          </div>
        </div>

        <form onSubmit={handleReplySubmit} className="lx-reply-form">
          <textarea
            value={replyContent}
            onChange={(e) => setReplyContent(e.target.value)}
            placeholder="回复楼主..."
            rows="3"
            required
          ></textarea>
          <button 
            type="submit" 
            className="lx-btn"
            disabled={isSubmitting}
          >
            {isSubmitting ? '发布中...' : '发布'}
          </button>
        </form>
      </div>
    </div>
  );
}