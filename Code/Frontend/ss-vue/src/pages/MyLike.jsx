// src/pages/MyLike.jsx
import React, { useState, useEffect, useMemo } from "react";
import { getAuth } from "../auth/storage";
import { BASE } from "../lib/api";
import { useNavigate } from "react-router-dom";

/* ===== 稳定随机 emoji ===== */
function hashStr(s){ let h=0; for(let i=0;i<s.length;i++){ h=(h<<5)-h+s.charCodeAt(i); h|=0; } return Math.abs(h); }
const P2_EMOJIS=["📚","🖥️","🍜","📷","🎨","🌿","🎧","💼","☕","🍩","🎓","🔬","📱","🧪","⚽","🧭","🍎","📖"];
const pickEmoji=(name="",id="")=>P2_EMOJIS[ hashStr(String(id)+String(name)) % P2_EMOJIS.length ];

/* 时间：月/日 时:分（24h） */
function fmtTime(ts){
  if(!ts) return "";
  const d=new Date(ts);
  return d.toLocaleString("zh-CN",{month:"numeric",day:"numeric",hour:"2-digit",minute:"2-digit",hour12:false});
}

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

  const viewList = useMemo(
    () => likedPosts.map(p => ({ ...p, _emoji: pickEmoji(p.username, p.id) })),
    [likedPosts]
  );

  const handlePostClick = (postId) => {
    navigate(`/post/${postId}`);
  };

  return (
    <div className="my-likes-page">
      <div className="lx-shell">
        {/* 顶部栏 */}
        <div className="lx-header">
          <button className="lx-back-btn" onClick={() => navigate(-1)} aria-label="返回">
            <svg viewBox="0 0 24 24" width="22" height="22"><path d="M15.41 7.41 14 6l-6 6 6 6 1.41-1.41L10.83 12z" fill="currentColor"/></svg>
          </button>
          <h1 className="lx-page-title">我点赞的帖子</h1>
        </div>

        {/* 列表 */}
        {loading ? (
          <div className="lx-empty">加载中…</div>
        ) : viewList.length === 0 ? (
          <div className="lx-empty">暂无点赞的帖子</div>
        ) : (
          <div className="lx-feed">
            {viewList.map(p => (
              <article 
                className="lx-feed-card lx-compact" 
                key={p.id}
                onClick={() => handlePostClick(p.id)}
                style={{ cursor: "pointer" }}
              >
                <div className="lx-feed-hd">
                  <div className="lx-avatar p2" aria-hidden>
                    <span className="em">{p._emoji}</span>
                  </div>
                  <div className="lx-author">
                    <div className="lx-name">{p.username}</div>
                    <div style={{display:"flex",gap:8}}>
                      <span className="lx-meta">{p.college}</span>
                      <span className="lx-meta">{p.grade}</span>
                    </div>
                  </div>
                </div>

                <div className="lx-feed-content">{p.content}</div>

                <div className="lx-bottom">
                  <div className="lx-feed-actions">
                    <button className="lx-act">👍 <span>{Number(p.likeCount||0)}</span></button>
                    <button className="lx-act">💬 <span>{Number(p.replyCount||0)}</span></button>
                  </div>
                  <time className="lx-time">{fmtTime(p.createdAt)}</time>
                </div>
              </article>
            ))}
          </div>
        )}
      </div>

      <style>{`
        .my-likes-page{
          height:100%;
          display:flex; justify-content:center;
          padding:0 8px;
          background:transparent;
        }
        .lx-shell{
          height:100%;
          width:min(420px,100%);
          margin:0 auto;
          display:grid;
          grid-template-rows:auto 1fr;
          gap:10px;
          position:relative; isolation:isolate;
        }
        .lx-shell::before{
          content:"";
          position:absolute;
          top:-8px; left:-8px; right:-8px; bottom:-8px;
          border-radius:26px;
          background: radial-gradient(120% 90% at 50% -20%, #fafffd 0%, #f0fff8 35%, #e9f7f1 65%, #e3f3ed 100%);
          border:1px solid #d8efe5;
          box-shadow:0 12px 36px rgba(22,107,78,.12);
          z-index:0;
        }
        .lx-shell > *{ position:relative; z-index:1; }

        .lx-header{ display:flex; align-items:center; gap:6px; padding:8px 2px 0; }
        .lx-back-btn{ border:none; background:transparent; color:#166b4e; padding:6px; cursor:pointer; }
        .lx-page-title{ flex:1; text-align:center; margin:0; color:#166b4e; font-weight:900; font-size:22px; }

        .lx-feed{
          overflow:auto;
          min-height:0;
          display:flex; flex-direction:column; gap:12px;
          padding-bottom:10px;
          scrollbar-gutter:stable;
        }
        .lx-feed::-webkit-scrollbar{ width:6px; }
        .lx-feed::-webkit-scrollbar-thumb{ background:rgba(22,107,78,.25); border-radius:999px; }
        .lx-feed::-webkit-scrollbar-track{ background:transparent; }

        .lx-feed-card{
          position:relative;
          background:#fff;
          border:1px solid #eaf5ef;
          border-radius:14px;
          padding:10px 12px 36px;
          box-shadow:0 6px 18px rgba(22,107,78,.08);
        }
        .lx-feed-hd{ display:flex; align-items:center; gap:8px; margin-bottom:6px; }

        .lx-avatar.p2{
          width:28px; height:28px; border-radius:999px;
          display:grid; place-items:center; overflow:hidden;
          background: radial-gradient(60% 60% at 35% 25%, #f7fff9, #e8f8f0);
          border:1px solid #e6f5ee;
          box-shadow: inset 0 0 0 3px rgba(126,226,182,.35);
        }
        .lx-avatar.p2 .em{ font-size:16px; line-height:1; }

        .lx-name{ font-size:15px; font-weight:900; color:#184a38; line-height:1; }
        .lx-meta{ font-size:12px; color:#5f9c85; }

        .lx-feed-content{ color:#174a38; font-size:15px; line-height:1.5; margin:0 0 4px; }

        .lx-bottom{ display:flex; align-items:center; justify-content:space-between; }

        .lx-feed-actions{
          display:flex; align-items:center; gap:14px;
          padding-right:96px;
        }
        .lx-act{
          border:none; background:transparent; color:#2a6a55;
          font-weight:800; font-size:12px;
          display:flex; align-items:center; gap:6px; cursor:pointer;
          padding:2px 4px; border-radius:6px;
        }

        .lx-time{
          position:absolute; right:12px; bottom:10px;
          font-size:12px; color:#7aa896; white-space:nowrap;
        }

        .lx-empty{ text-align:center; color:#6aa38f; padding:22px 0; }
      `}</style>
    </div>
  );
}
