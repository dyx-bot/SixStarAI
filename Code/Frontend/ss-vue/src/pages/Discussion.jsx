// src/pages/Discussion.jsx
import React, { useMemo, useState, useEffect } from "react";
import "./Discussion.css";
import { getAuth } from "../auth/storage";
import { BASE } from "../lib/api";
import { useNavigate } from "react-router-dom";
import { message, Popconfirm } from "antd";

function hotScore(p) {
  return p.likeCount * 1 + p.replyCount * 2;
}

function hashStr(s) {
  let h = 0;
  for (let i = 0; i < s.length; i++) {
    h = (h << 5) - h + s.charCodeAt(i);
    h |= 0;
  }
  return Math.abs(h);
}

const P2_ICONS = [];
const P2_EMOJIS = [
  "📚", "🖥️", "🍜", "📷", "🎨", "🌿", "🎧", "💼", "☕", "🍩",
  "🎓", "🔬", "📱", "🧪", "⚽", "🧭", "🍎", "📖"
];

function pickP2Avatar(name = "", id = "") {
  const seed = hashStr(String(id) + String(name));
  if (P2_ICONS.length > 0) {
    return { type: "img", value: P2_ICONS[seed % P2_ICONS.length] };
  }
  return { type: "emoji", value: P2_EMOJIS[seed % P2_EMOJIS.length] };
}

function fmtTime(ts) {
  if (!ts) return "";
  const d = new Date(ts);
  return d.toLocaleString("zh-CN", {
    month: "numeric",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
    hour12: false,
  });
}

const TAGS = [
  "学习/资料",
  "生活/吐槽",
  "表白/情感",
  "求助/攻略",
  "组队/组局",
  "闲置/交易",
  "寻物/招领",
];

export default function Discussion() {
  const [kw, setKw] = useState("");
  const [tab, setTab] = useState("all"); // all | hot
  const [activeTag, setActiveTag] = useState(""); // 当前选择的标签
  const navigate = useNavigate();

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
        tag: "学习/资料",
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
        tag: "求助/攻略",
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
        tag: "生活/吐槽",
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
  .sort((a, b) => new Date(b.createdAt || 0) - new Date(a.createdAt || 0))
  .filter(
    (p) =>
      !kw ||
      p.username?.includes(kw) ||
      p.college?.includes(kw) ||
      p.grade?.includes(kw) ||
      p.content?.includes(kw)
  );


  const hotSorted = [...posts]
    .sort((a, b) => hotScore(b) - hotScore(a))
    .filter(
      (p) =>
        !kw ||
        p.username?.includes(kw) ||
        p.college?.includes(kw) ||
        p.grade?.includes(kw) ||
        p.content?.includes(kw)
    );

  let baseList = tab === "all" ? filtered : hotSorted;
  if (activeTag) {
    baseList = baseList.filter((p) => p.tag === activeTag);
  }
  const viewList = baseList;

  const toggleLike = async (id, isliked) => {
    const userId = getAuth().id;
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
    try {
      const response = await fetch(`${BASE}/api/likes`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ postId: id, userId, isliked: !isliked }),
      });
      const result = await response.json();
      if (result.likeCount !== undefined && result.likeCount !== null) {
        setPosts((a) =>
          a.map((p) =>
            p.id === id
              ? { ...p, isliked: result.isLiked, likeCount: result.likeCount }
              : p
          )
        );
      }
    } catch {
      setPosts((a) =>
        a.map((p) =>
          p.id === id
            ? {
                ...p,
                isliked,
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

  // 举报接口调用
  const handleReportConfirm = async (postId) => {
    const userId = getAuth().id;
    try {
      const res = await fetch(`${BASE}/api/posts/${postId}/report?userId=${userId}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
      });
      const result = await res.json();
      if (result.success) {
        message.success(result.message || "举报成功");
      } else {
        message.error(result.message || "举报失败");
      }
    } catch (err) {
      console.error("举报失败:", err);
      message.error("举报失败，请稍后重试");
    }
  };

  const handleReportCancel = () => {
    message.info("取消举报");
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
            placeholder="搜索主题、作者…"
          />
          <button className="lx-btn">搜索</button>
        </div>

        {/* Tabs + 标签合并 */}
        <div className="lx-tabs lx-tag-bar">
          <button
            className={`lx-tag ${tab === "all" && !activeTag ? "on" : ""}`}
            onClick={() => {
              setTab("all");
              setActiveTag(""); // 清除标签筛选
            }}
          >
            全部
          </button>
          <button
            className={`lx-tag ${tab === "hot" && !activeTag ? "on" : ""}`}
            onClick={() => {
              setTab("hot");
              setActiveTag(""); // 清除标签筛选
            }}
          >
            热门
          </button>

          {TAGS.map((tag) => (
            <button
              key={tag}
              className={`lx-tag ${activeTag === tag ? "on" : ""}`}
              onClick={() => {
                setActiveTag(tag);
                setTab("all"); // 标签筛选使用时间排序
              }}
            >
              {tag}
            </button>
          ))}
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

                  <div className="lx-feed-content">{p.content}</div>

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

                      <button
                        className="lx-act"
                        onClick={(e) => {
                          e.stopPropagation();
                          navigate(`/post/${p.id}?focus=comment`);
                        }}
                      >
                        💬 <span>{p.replyCount}</span>
                      </button>

                      {/* 举报按钮 */}
                      <Popconfirm
                        title="举报帖子"
                        description="确定要举报该帖子吗？"
                        onConfirm={(e) => {
                          e?.stopPropagation?.();
                          handleReportConfirm(p.id);
                        }}
                        onCancel={(e) => {
                          e?.stopPropagation?.();
                          handleReportCancel();
                        }}
                        okText="Yes"
                        cancelText="No"
                      >
                        <button
                          className="lx-act"
                          onClick={(e) => e.stopPropagation()}
                        >
                          ⋯
                        </button>
                      </Popconfirm>
                    </div>
                    <time className="lx-time">{fmtTime(p.createdAt)}</time>
                  </div>
                </article>
              );
            })
          )}
        </div>
      </div>

      <button className="lx-fab" onClick={() => navigate("/post")} aria-label="发布动态">
        <svg className="lx-fab-icon" viewBox="0 0 24 24" width="18" height="18">
          <path
            d="M12 5v14M5 12h14"
            stroke="currentColor"
            strokeWidth="2"
            strokeLinecap="round"
          />
        </svg>
        发布动态
      </button>
    </div>
  );
}
