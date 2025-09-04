// src/pages/MyPosts.jsx
import React, { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getAuth } from "../auth/storage";
import { BASE } from "../lib/api";

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

export default function MyPosts(){
  const { id } = getAuth();
  const navigate = useNavigate();
  const [posts,setPosts]=useState([]);
  const [loading,setLoading]=useState(true);

  useEffect(()=>{
    if(!id) return;
    fetch(`${BASE}/api/users/${id}?currentUserId=${id}`)
      .then(r=>r.json())
      .then(data=>{
        setPosts(Array.isArray(data?.posts)?data.posts:[]);
        setLoading(false);
      })
      .catch(()=>{ setPosts([]); setLoading(false); });
  },[id]);

  const handleDelete=async(postId)=>{
    if(!window.confirm("确定删除这条帖子吗？删除后不可恢复。")) return;
    try{
      const r=await fetch(`${BASE}/api/posts/${postId}?userId=${id}`,{method:"DELETE",headers:{"Content-Type":"application/json"}});
      if(!r.ok) throw new Error();
      setPosts(arr=>arr.filter(p=>p.id!==postId));
    }catch{ alert("删除失败，请稍后再试"); }
  };

  const viewList = useMemo(()=>posts.map(p=>({...p,_emoji:pickEmoji(p.username,p.id)})),[posts]);

  return (
    <div className="my-posts-page">
      <div className="lx-shell">
        {/* 顶部栏 */}
        <div className="lx-header">
          <button className="lx-back-btn" onClick={()=>navigate(-1)} aria-label="返回">
            <svg viewBox="0 0 24 24" width="22" height="22"><path d="M15.41 7.41 14 6l-6 6 6 6 1.41-1.41L10.83 12z" fill="currentColor"/></svg>
          </button>
          <h1 className="lx-page-title">我的帖子</h1>
        </div>

        {/* 列表：现在仅这一块滚动 */}
        {loading ? (
          <div className="lx-empty">加载中…</div>
        ) : viewList.length===0 ? (
          <div className="lx-empty">暂无帖子</div>
        ) : (
          <div className="lx-feed">
            {viewList.map(p=>(
              <article className="lx-feed-card lx-compact" key={p.id}>
                <div className="lx-feed-hd">
                  <div className="lx-avatar p2" aria-hidden><span className="em">{p._emoji}</span></div>
                  <div className="lx-author">
                    <div className="lx-name">{p.username||"我"}</div>
                    <div style={{display:"flex",gap:8}}>
                      <span className="lx-meta">{p.college}</span>
                      <span className="lx-meta">{p.grade}</span>
                    </div>
                  </div>
                  <button className="del-btn" title="删除" aria-label="删除" onClick={()=>handleDelete(p.id)}>
                    <svg viewBox="0 0 24 24" width="16" height="16">
                      <path d="M9 3h6l1 2h4v2H4V5h4l1-2zm1 6h2v8h-2V9zm4 0h2v8h-2V9z" fill="currentColor" opacity=".85"/>
                    </svg>
                  </button>
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
        /* ===== 页面骨架：固定高度，列表滚动 ===== */
        .my-posts-page{
          height:100%;               /* 关键1：占满父容器高度（和讨论页一致） */
          display:flex; justify-content:center;
          padding:0 8px;
          background:transparent;
        }
        .lx-shell{
          height:100%;               /* 关键2：让内部栅格同样占满高度 */
          width:min(420px,100%);
          margin:0 auto;
          display:grid;
          grid-template-rows:auto 1fr;   /* 顶部栏 + 可伸缩的列表 */
          gap:10px;
          position:relative; isolation:isolate;
        }
        /* 面板浅绿色背景（与其它页一致） */
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

        /* 头部 */
        .lx-header{ display:flex; align-items:center; gap:6px; padding:8px 2px 0; }
        .lx-back-btn{ border:none; background:transparent; color:#166b4e; padding:6px; cursor:pointer; }
        .lx-page-title{ flex:1; text-align:center; margin:0; color:#166b4e; font-weight:900; font-size:22px; }

        /* 列表区域：唯一滚动区 */
        .lx-feed{
          overflow:auto;             /* 关键3：只让这里滚动 */
          min-height:0;              /* 关键4：防止撑开容器 */
          display:flex; flex-direction:column; gap:12px;
          padding-bottom:10px;
          scrollbar-gutter:stable;
        }
        .lx-feed::-webkit-scrollbar{ width:6px; }
        .lx-feed::-webkit-scrollbar-thumb{ background:rgba(22,107,78,.25); border-radius:999px; }
        .lx-feed::-webkit-scrollbar-track{ background:transparent; }

        /* 卡片（紧凑尺寸，和讨论区一致） */
        .lx-feed-card{
          position:relative;
          background:#fff;
          border:1px solid #eaf5ef;
          border-radius:14px;
          padding:10px 12px 36px; /* 给右下角时间留位 */
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
          padding-right:96px;  /* 右侧时间占位 */
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

        /* 删除按钮（小而扁） */
        .del-btn{
          margin-left:auto;
          border:1px solid #cfe8df; background:#f8fffc;
          color:#18624e; border-radius:999px;
          padding:4px 8px; display:inline-flex; align-items:center; justify-content:center;
          cursor:pointer; box-shadow:0 6px 14px rgba(22,107,78,.08);
        }
        .del-btn:hover{ background:#eef9f3; }

        .lx-empty{ text-align:center; color:#6aa38f; padding:22px 0; }
      `}</style>
    </div>
  );
}
