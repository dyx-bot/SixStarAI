// src/pages/Discussion.jsx
import React, { useMemo, useState, useEffect } from "react";
import "./Discussion.css";
import { getAuth } from "../auth/storage";
import { BASE } from "../lib/api";
import { useNavigate } from "react-router-dom";

function hotScore(p) {
  
  return (p.likeCount * 1 + p.replyCount * 2) ;
}

/* ====== 工具：稳定随机（同一个用户始终同一个头像） ====== */
function hashStr(s) {
  let h = 0;
  for (let i = 0; i < s.length; i++) {
    h = (h << 5) - h + s.charCodeAt(i);
    h |= 0;
  }
  return Math.abs(h);
}

/* ====== p2 风格随机头像素材 ====== */
/** 如果你以后有本地图标图片，放到 public/avatars/p2/ 里，然后把下面数组改成你的路径 */
const P2_ICONS = [
  // "/avatars/p2/book.png",
  // "/avatars/p2/monitor.png",
  // "/avatars/p2/noodles.png",
  // "/avatars/p2/camera.png",
  // ...
];

/** 现在先用 emoji 来模拟图标风；当 P2_ICONS 有内容时会自动切换为图片 */
const P2_EMOJIS = [
  "📚", "🖥️", "🍜", "📷", "🎨", "🌿", "🎧", "💼", "☕", "🍩",
  "🎓", "🔬", "📱", "🧪", "⚽", "🧭", "🍎", "📖"
];

/** 选取头像：优先图片，否则 emoji */
function pickP2Avatar(name = "", id = "") {
  const seed = hashStr(String(id) + String(name));
  if (P2_ICONS.length > 0) {
    return { type: "img", value: P2_ICONS[seed % P2_ICONS.length] };
  }
  return { type: "emoji", value: P2_EMOJIS[seed % P2_EMOJIS.length] };
}

/* ====== 时间格式（右下角显示） ====== */
function fmtTime(ts) {
  if (!ts) return "";
  const d = new Date(ts);
  // 月/日 时:分（24h）
  return d.toLocaleString("zh-CN", {
    month: "numeric",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
    hour12: false,
  });
}

export default function Discussion() {
  const [kw, setKw] = useState("");
  const [tab, setTab] = useState("all"); // all | hot
  const navigate = useNavigate();

  // 示例本地数据兜底
  const demo = useMemo(
    () => [
      {
        id: 1,
        username: "小周",
        content: "下学期选《数据结构》推荐哪位老师？实验量如何？",
        isliked: false,
        likeCount: 34,
        replyCount: 6,
        college: "信息学院",
        grade: "大二",
        isFavorited: true,
        createdAt: Date.now() - 1000 * 60 * 60 * 24 * 2,
      },
      {
        id: 2,
        username: "LYY",
        college: "信科",
        grade: "大三",
        content: "图书馆研讨间怎么预约更容易抢到位？",
        isliked: true,
        likeCount: 58,
        replyCount: 10,
        isFavorited: false,
        createdAt: Date.now() - 1000 * 60 * 60 * 24 * 5,
      },
      {
        id: 3,
        username: "饭团",
        college: "经管",
        grade: "大一",
        content: "食堂哪家性价比最高？求安利隐藏菜单～",
        isliked: false,
        likeCount: 18,
        replyCount: 5,
        isFavorited: false,
        createdAt: Date.now() - 1000 * 60 * 60 * 24 * 1,
      },
    ],
    []
  );

  const [posts, setPosts] = useState(demo);

  useEffect(() => {
    const userId = getAuth().id;
    fetch(`${BASE}/api/posts/get?userId=${userId}`)
      .then((res) => res.json())
      .then((data) => {
        let arr = Array.isArray(data?.data) ? data.data : Array.isArray(data) ? data : [];
        arr = arr.map((p) => ({
          ...p,
          likeCount: Number(p.likeCount || 0),
          replyCount: Number(p.replyCount || 0),
          isliked: !!p.isliked,
          isFavorited: !!p.isFavorited,
        }));
        if (arr.length) setPosts(arr);
      })
      .catch(() => {});
  }, []);

  const filtered = posts
    .slice()
    .sort((a, b) => (b.createdAt || 0) - (a.createdAt || 0))
    .filter(
      (p) => !kw || p.username?.includes(kw) || p.college?.includes(kw) || p.grade?.includes(kw) || p.content?.includes(kw)
    );

  const hotSorted = [...posts]
    .sort((a, b) => hotScore(b) - hotScore(a))
    .filter(
      (p) => !kw || p.username?.includes(kw) || p.college?.includes(kw) || p.grade?.includes(kw) || p.content?.includes(kw)
    );

  const viewList = tab === "all" ? filtered : hotSorted;

  const toggleLike = async (id, isliked) => {
    const userId = getAuth().id;
    setPosts((a) =>
      a.map((p) =>
        p.id === id ? { ...p, isliked: !p.isliked, likeCount: Number(p.likeCount || 0) + (!p.isliked ? 1 : -1) } : p
      )
    );
    try {
      const response = await fetch(`${BASE}/api/likes`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ postId: id, userId, isliked: !isliked }),
      });
      const result = await response.json();
      if (result.likeCount !== undefined && result.likeCount !== null) {
        setPosts((a) =>
          a.map((p) => (p.id === id ? { ...p, isliked: result.isLiked, likeCount: result.likeCount } : p))
        );
      }
    } catch {
      setPosts((a) =>
        a.map((p) =>
          p.id === id ? { ...p, isliked, likeCount: Number(p.likeCount || 0) + (isliked ? 1 : -1) } : p
        )
      );
      alert("点赞失败，请重试");
    }
  };

  const toggleFollow = async (id, isFavorited) => {
    const userId = getAuth().id;
    setPosts((a) => a.map((p) => (p.id === id ? { ...p, isFavorited: !p.isFavorited } : p)));
    try {
      const response = await fetch(`${BASE}/api/favorites`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ postId: id, userId, favorited: !isFavorited }),
      });
      const result = await response.json();
      if (typeof result.isFavorited === "boolean") {
        setPosts((a) => a.map((p) => (p.id === id ? { ...p, isFavorited: result.isFavorited } : p)));
      }
    } catch {
      setPosts((a) => a.map((p) => (p.id === id ? { ...p, isFavorited } : p)));
      alert("收藏失败，请重试");
    }
  };

  const handlePostClick = (post) => {
    navigate(`/post/${post.id}`);
  };

  return (
    <div className="lx-discussion">
      <div className="lx-shell">
        <div className="lx-header">
          <h1 className="lx-page-title">动态</h1>
        </div>

        <div className="lx-search-line">
          <input value={kw} onChange={(e) => setKw(e.target.value)} placeholder="搜索主题、作者或标签…" />
          <button className="lx-btn">搜索</button>
        </div>

        <div className="lx-tabs">
          <button className={`lx-tab ${tab === "all" ? "on" : ""}`} onClick={() => setTab("all")}>
            全部
          </button>
          <button className={`lx-tab ${tab === "hot" ? "on" : ""}`} onClick={() => setTab("hot")}>
            热门
          </button>
        </div>

        <div className="lx-feed">
          {viewList.length === 0 ? (
            <div className="lx-empty">暂无内容，稍后再来~</div>
          ) : (
            viewList.map((p) => {
              const av = pickP2Avatar(p.username, p.id);
              return (
                <article
                  className="lx-feed-card lx-compact"
                  key={p.id}
                  onClick={() => handlePostClick(p)}
                  style={{ cursor: "pointer" }}
                >
                  <div className="lx-feed-hd">
                    {/* p2 风格随机头像（图片优先，emoji 兜底） */}
                    <div className="lx-avatar p2" aria-hidden>
                      {av.type === "img" ? (
                        <img src={av.value} alt="" />
                      ) : (
                        <span className="em">{av.value}</span>
                      )}
                    </div>

                    <div className="lx-author">
                      <div className="lx-name">{p.username}</div>
                      <div style={{ display: "flex", gap: 8 }}>
                        <span className="lx-meta">{p.college}</span>
                        <span className="lx-meta">{p.grade}</span>
                      </div>
                    </div>

                    <button
                      className={`lx-follow ${p.isFavorited ? "on" : ""}`}
                      onClick={(e) => {
                        e.stopPropagation();
                        toggleFollow(p.id, p.isFavorited);
                      }}
                    >
                      {p.isFavorited ? "已收藏" : "收藏"}
                    </button>
                  </div>

                  {/* 内容更紧凑 */}
                  <div className="lx-feed-content">{p.content}</div>

                  {/* 底部：左侧操作，右侧时间 */}
                  <div className="lx-bottom">
                    <div className="lx-feed-actions">
                      <button
                        className={`lx-act ${p.isliked ? "on" : ""}`}
                        onClick={(e) => {
                          e.stopPropagation();
                          toggleLike(p.id, p.isliked);
                        }}
                      >
                        👍 <span>{p.likeCount}</span>
                      </button>
                      <button className="lx-act" onClick={(e) => e.stopPropagation()}>
                        💬 <span>{p.replyCount}</span>
                      </button>
                      <button className="lx-act" onClick={(e) => e.stopPropagation()}>
                        ⋯
                      </button>
                    </div>
                    <time className="lx-time">{fmtTime(p.createdAt)}</time>
                  </div>
                </article>
              );
            })
          )}
        </div>
      </div>

      {/* 悬浮发布按钮（保持不变） */}
      <button className="lx-fab" onClick={() => navigate("/post")} aria-label="发布动态">
        <svg className="lx-fab-icon" viewBox="0 0 24 24" width="18" height="18">
          <path d="M12 5v14M5 12h14" stroke="currentColor" strokeWidth="2" strokeLinecap="round" />
        </svg>
        发布动态
      </button>
    </div>
  );
}
