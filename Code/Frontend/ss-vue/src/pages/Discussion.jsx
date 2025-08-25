import React, { useMemo, useState } from "react";
import "./Discussion.css";

/**
 * 热度评分说明：
 * score = (like * 1 + comment * 2) * freshness
 * 其中 freshness = exp( - ageDays / 7 )
 * - 评论权重更高（2）
 * - 7 天为半衰期：越新的内容 freshness 越接近 1，越旧分值越小
 */
function hotScore(p) {
  const now = Date.now();
  const ageDays = Math.max(0, (now - (p.createdAt || now)) / (1000 * 60 * 60 * 24));
  const freshness = Math.exp(-ageDays / 7);
  return (p.like * 1 + p.comment * 2) * freshness;
}

export default function Discussion() {
  const [kw, setKw] = useState("");
  const [tab, setTab] = useState("all"); // all | hot

  // 带时间戳的示例数据（用于热门排序的时间衰减）
  const demo = useMemo(
    () => [
      {
        id: 1,
        avatar: "📚",
        name: "小周",
        meta: "信息学院 · 大二",
        content: "下学期选《数据结构》推荐哪位老师？实验量如何？",
        liked: false,
        like: 34,
        comment: 6,
        follow: true,
        createdAt: Date.now() - 1000 * 60 * 60 * 24 * 2, // 2 天前
      },
      {
        id: 2,
        avatar: "🖥️",
        name: "LYY",
        meta: "信科 · 大三",
        content: "图书馆研讨间怎么预约更容易抢到位？",
        liked: true,
        like: 58,
        comment: 10,
        follow: false,
        createdAt: Date.now() - 1000 * 60 * 60 * 24 * 5, // 5 天前
      },
      {
        id: 3,
        avatar: "🍜",
        name: "饭团",
        meta: "经管 · 大二",
        content: "食堂哪家性价比最高？求安利隐藏菜单～",
        liked: false,
        like: 18,
        comment: 5,
        follow: false,
        createdAt: Date.now() - 1000 * 60 * 60 * 24 * 1, // 1 天前
      },
    ],
    []
  );

  const [posts, setPosts] = useState(demo);

  // 全部：仍按你的关键词过滤
  const filtered = posts.filter(
    (p) => !kw || p.name.includes(kw) || p.meta.includes(kw) || p.content.includes(kw)
  );

  // 热门：先按热度分数排序，再做同样的关键词过滤
  const hotSorted = [...posts]
    .sort((a, b) => hotScore(b) - hotScore(a))
    .filter((p) => !kw || p.name.includes(kw) || p.meta.includes(kw) || p.content.includes(kw));

  const toggleLike = (id) =>
    setPosts((a) =>
      a.map((p) =>
        p.id === id ? { ...p, liked: !p.liked, like: p.liked ? p.like - 1 : p.like + 1 } : p
      )
    );

  const toggleFollow = (id) =>
    setPosts((a) => a.map((p) => (p.id === id ? { ...p, follow: !p.follow } : p)));

  const publish = () => {
    const text = window.prompt("写点什么吧（示例：今晚自习室很高效！）");
    if (!text) return;
    setPosts((a) => [
      {
        id: Date.now(),
        avatar: "🌿",
        name: "我",
        meta: "同学 · 校园",
        content: text,
        liked: false,
        like: 0,
        comment: 0,
        follow: true,
        createdAt: Date.now(), // 新发布的时间
      },
      ...a,
    ]);
  };

  // 视图数据：全部=filtered；热门=hotSorted
  const viewList = tab === "all" ? filtered : hotSorted;

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

        {/* 标签栏（不改你现有样式，其类名为新增 .lx-tabs/.lx-tab） */}
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
              <article className="lx-feed-card" key={p.id}>
                <div className="lx-feed-hd">
                  <div className="lx-avatar" aria-hidden>
                    {p.avatar}
                  </div>
                  <div className="lx-author">
                    <div className="lx-name">{p.name}</div>
                    <div className="lx-meta">{p.meta}</div>
                  </div>
                  <button
                    className={`lx-follow ${p.follow ? "on" : ""}`}
                    onClick={() => toggleFollow(p.id)}
                  >
                    {p.follow ? "已关注" : "关注"}
                  </button>
                </div>
                <div className="lx-feed-content">{p.content}</div>
                <div className="lx-feed-actions">
                  <button
                    className={`lx-act ${p.liked ? "on" : ""}`}
                    onClick={() => toggleLike(p.id)}
                  >
                    👍 <span>{p.like}</span>
                  </button>
                  <button className="lx-act">
                    💬 <span>{p.comment}</span>
                  </button>
                  <button className="lx-act">⋯</button>
                </div>
              </article>
            ))
          )}
        </div>
      </div>

      {/* 悬浮发布按钮（不挡住底部Tab，位置略上移） */}
      <button
        className="lx-fab"
        onClick={publish}
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
