// src/pages/Discussion.jsx
import React, { useMemo, useState, useEffect } from "react";
import "./Discussion.css";
import { getAuth } from "../auth/storage";
import { BASE } from "../lib/api";
import { useNavigate } from "react-router-dom"; // 添加路由导航

function hotScore(p) {
  const now = Date.now();
  const ageDays = Math.max(0, (now - (p.createdAt || now)) / (1000 * 60 * 60 * 24));
  const freshness = Math.exp(-ageDays / 7);
  return (p.likeCount * 1 + p.replyCount * 2) * freshness;
}

export default function Discussion() {
  const [kw, setKw] = useState("");
  const [tab, setTab] = useState("all"); // all | hot
  const [showDialog, setShowDialog] = useState(false);
  const navigate = useNavigate(); // 使用路由导航
  
  // 带时间戳的示例数据
  const demo = useMemo(
    () => [
      {
        id: 1,
        avatar: "📚",
        username: "小周",
        content: "下学期选《数据结构》推荐哪位老师？实验量如何？",
        isliked: false,
        likeCount: 34,
        replyCount: 6,
        college: "经管",
        grade: "大二",
        isFavorited: true,
        createdAt: Date.now() - 1000 * 60 * 60 * 24 * 2,
      },
      {
        id: 2,
        avatar: "🖥️",
        username: "LYY",
        college: "计算机",
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
        avatar: "🍜",
        username: "饭团", 
        college: "食品",
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

  useEffect(() => {
    const userId = getAuth().id;
    fetch(`${BASE}/api/posts/get?userId=${userId}`)
      .then(res => res.json())
      .then(data => {
        let arr = Array.isArray(data.data) ? data.data : Array.isArray(data) ? data : [];
        // 保证数值字段为数字，布尔字段为布尔值
        arr = arr.map(p => ({
          ...p,
          likeCount: Number(p.likeCount || 0),
          replyCount: Number(p.replyCount || 0),
          isliked: !!p.isliked,
          isFavorited: !!p.isFavorited, 
        }));
        setPosts(arr);
      })
      .catch(() => {});
  }, []);

  const [posts, setPosts] = useState(demo);

  // 全部：按时间倒序排列（最新的在前）
  const filtered = posts
    .slice()
    .sort((a, b) => (b.createdAt || 0) - (a.createdAt || 0))
    .filter(
      (p) => !kw || p.username.includes(kw) || p.college.includes(kw) || p.grade.includes(kw) || p.content.includes(kw)
    );

  const hotSorted = [...posts]
    .sort((a, b) => hotScore(b) - hotScore(a))
    .filter((p) => !kw || p.username.includes(kw) || p.college.includes(kw) || p.grade.includes(kw) || p.content.includes(kw));

  const viewList = tab === "all" ? filtered : hotSorted;

  const toggleLike = async (id, isliked) => {
    const userId = getAuth().id;
    
    // 先本地乐观更新
    setPosts((a) =>
      a.map((p) =>
        p.id === id
          ? {
              ...p,
              isliked: !p.isliked,
              likeCount: Number(p.likeCount || 0) + (!p.isliked ? 1 : -1),
            }
          : p
      )
    );

    // 调用后端接口
    try {
      const response = await fetch(`${BASE}/api/likes`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          postId: id,
          userId: userId,
          isliked: !isliked, // true=点赞，false=取消
        }),
      });

      const result = await response.json();

      // 根据后端返回的数据更新状态
      if (result.likeCount !== undefined && result.likeCount !== null) {
        setPosts((a) =>
          a.map((p) =>
            p.id === id
              ? {
                  ...p,
                  isliked: result.isLiked,
                  likeCount: result.likeCount,
                }
              : p
          )
        );
      }
    } catch (err) {
      // 失败时可回滚本地状态
      setPosts((a) =>
        a.map((p) =>
          p.id === id
            ? {
                ...p,
                isliked: isliked,
                likeCount: Number(p.likeCount || 0) + (isliked ? 1 : -1),
              }
            : p
        )
      );
      alert("点赞失败，请重试");
    }
  };

  const toggleFollow = async (id, isFavorited) => {
    const userId = getAuth().id;
    
    // 先本地乐观更新
    setPosts((a) =>
      a.map((p) =>
        p.id === id
          ? { ...p, isFavorited: !p.isFavorited }
          : p
      )
    );

    // 调用后端接口
    try {
      const response = await fetch(`${BASE}/api/favorites`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          postId: id,
          userId: userId,
          favorited: !isFavorited, // true=收藏，false=取消
        }),
      });
      
      const result = await response.json();
      
      // 根据后端返回的数据更新状态
      if (typeof result.isFavorited === "boolean") {
        setPosts((a) =>
          a.map((p) =>
            p.id === id
              ? { ...p, isFavorited: result.isFavorited }
              : p
          )
        );
      }
    } catch (err) {
      // 失败时回滚
      setPosts((a) =>
        a.map((p) =>
          p.id === id
            ? { ...p, isFavorited: isFavorited }
            : p
        )
      );
      alert("收藏失败，请重试");
    }
  };

  // 点击帖子跳转到详情页
  const handlePostClick = (post) => {
    // 使用路由跳转到详情页
    navigate(`/post/${post.id}`);
  };

  return (
    <div className="lx-discussion">
      <div className="lx-shell">
        <div className="lx-header">
          <h1 className="lx-page-title">动态</h1>
        </div>

        <div className="lx-search-line">
          <input
            value={kw}
            onChange={(e) => setKw(e.target.value)}
            placeholder="搜索主题、作者或标签…"
          />
          <button className="lx-btn">搜索</button>
        </div>

        <div className="lx-tabs">
          <button
            className={`lx-tab ${tab === "all" ? "on" : ""}`}
            onClick={() => setTab("all")}
          >
            全部
          </button>
          <button
            className={`lx-tab ${tab === "hot" ? "on" : ""}`}
            onClick={() => setTab("hot")}
          >
            热门
          </button>
        </div>

        <div className="lx-feed">
          {viewList.length === 0 ? (
            <div className="lx-empty">暂无内容，稍后再来~</div>
          ) : (
            viewList.map((p) => (
              <article 
                className="lx-feed-card" 
                key={p.id}
                onClick={() => handlePostClick(p)}
                style={{ cursor: 'pointer' }}
              >
                <div className="lx-feed-hd">
                  <div className="lx-avatar" aria-hidden>
                    {
                      /^https?:\/\//.test(p.avatar)
                        ? <img src={p.avatar} alt="头像" style={{width: "100%", height: "100%", borderRadius: "50%"}} />
                        : p.avatar
                    }
                  </div>
                  <div className="lx-author">
                    <div className="lx-name">{p.username}</div>
                    <div style={{display: 'flex'}}>
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
                <div className="lx-reply-time">
                   {new Date(p.createdAt).toLocaleString('zh-CN')}
                </div>
                <div className="lx-feed-content">{p.content}</div>
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
                  <button className="lx-act">
                    💬 <span>{p.replyCount}</span>
                  </button>
                  <button className="lx-act">⋯</button>
                </div>
              </article>
            ))
          )}
        </div>
      </div>

      {/* 悬浮发布按钮 */}
      <button
        className="lx-fab"
        onClick={() => navigate("/post")}
        aria-label="发布动态"
        style={{ bottom: "84px" }}
      >
        <svg className="lx-fab-icon" viewBox="0 0 24 24" width="18" height="18">
          <path d="M12 5v14M5 12h14" stroke="currentColor" strokeWidth="2" strokeLinecap="round" />
        </svg>
        发布动态
      </button>
    </div>
  );
}