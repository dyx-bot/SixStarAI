// src/pages/MyLike.jsx
import React, { useState, useEffect } from "react";
import { getAuth } from "../auth/storage";
import { BASE } from "../lib/api";
import { useNavigate } from "react-router-dom";

export default function MyLike() {
  const { id } = getAuth();
  const [likedPosts, setLikedPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    if (id) {
      fetch(`${BASE}/api/posts/liked/${id}?currentUserId=${id}`)
        .then(res => res.json())
        .then(data => {
          setLikedPosts(data || []);
          setLoading(false);
        })
        .catch(err => {
          console.error("获取点赞帖子失败:", err);
          setLoading(false);
          setLikedPosts([]);
        });
    }
  }, [id]);

  const handlePostClick = (postId) => {
    navigate(`/post/${postId}`);
  };

  return (
    <div className="my-likes">
      <div className="lx-shell">
        <div className="lx-header">
          <h1 className="lx-page-title">我点赞的帖子</h1>
        </div>

        {loading ? (
          <div className="lx-loading">加载中...</div>
        ) : likedPosts.length === 0 ? (
          <div className="lx-empty">暂无点赞的帖子</div>
        ) : (
          <div className="lx-feed">
            {likedPosts.map((post) => (
              <article 
                className="lx-feed-card" 
                key={post.id}
                onClick={() => handlePostClick(post.id)}
                style={{ cursor: 'pointer' }}
              >
                <div className="lx-feed-hd">
                  <div className="lx-avatar" aria-hidden>
                    {post.avatar || "📚"}
                  </div>
                  <div className="lx-author">
                    <span className="lx-name">{post.username}</span>
                    <span className="lx-meta">{post.college}</span>
                    <span className="lx-meta">{post.grade}</span>
                  </div>
                </div>
                <div className="lx-reply-time">
                  {new Date(post.createdAt).toLocaleString('zh-CN')}
                </div>
                <div className="lx-feed-content">{post.content}</div>
                <div className="lx-feed-actions">
                  <button className="lx-act on">
                    👍 <span>{post.likeCount || 0}</span>
                  </button>
                  <button className="lx-act">
                    💬 <span>{post.replyCount || 0}</span>
                  </button>
                </div>
              </article>
            ))}
          </div>
        )}

        <button
          className="lx-fab"
          onClick={() => navigate(-1)}
          aria-label="返回"
          style={{ bottom: "84px" }}
        >
          ← 返回
        </button>
      </div>

      <style>{`
        .my-likes {
          padding: 16px;
        }
        .lx-feed-card {
          background: #fff;
          border-radius: 12px;
          padding: 16px;
          margin-bottom: 12px;
          box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        }
        .lx-feed-hd {
          display: flex;
          align-items: center;
          gap: 12px;
        }
        .lx-avatar {
          width: 40px;
          height: 40px;
          border-radius: 50%;
          background: #7ee2b6;
          display: flex;
          align-items: center;
          justify-content: center;
          color: white;
          font-size: 18px;
        }
        .lx-author {
          flex: 1;
        }
        .lx-name {
          font-weight: bold;
          color: #184a38;
        }
        .lx-meta {
          font-size: 12px;
          color: #6a8f82;
          margin-right: 8px;
        }
        .lx-reply-time {
          font-size: 12px;
          color: #999;
          margin: 8px 0;
        }
        .lx-feed-content {
          margin: 12px 0;
          color: #333;
          line-height: 1.5;
        }
        .lx-feed-actions {
          display: flex;
          gap: 16px;
        }
        .lx-act {
          background: none;
          border: none;
          color: #6a8f82;
          cursor: pointer;
          display: flex;
          align-items: center;
          gap: 4px;
        }
        .lx-act.on {
          color: #22a06e;
        }
        .lx-fab {
          position: fixed;
          right: 16px;
          bottom: 16px;
          width: 56px;
          height: 56px;
          border-radius: 50%;
          background: #22a06e;
          color: white;
          border: none;
          box-shadow: 0 4px 12px rgba(34,160,110,0.3);
          cursor: pointer;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 16px;
        }
        .lx-loading, .lx-empty {
          text-align: center;
          padding: 32px;
          color: #6a8f82;
        }
      `}</style>
    </div>
  );
}