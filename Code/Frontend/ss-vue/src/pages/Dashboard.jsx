import React, { useMemo, useState } from "react";
import "./Discussion.css";

export default function Discussion() {
  // 顶部 Tab（仿截图：关注 / 动态 / 征婚墙 / 故事）
  const tabs = [
    { key: "follow", label: "关注" },
    { key: "feed", label: "动态" },
    { key: "wall", label: "征婚墙" },
    { key: "story", label: "故事" },
  ];
  const [active, setActive] = useState("feed");
  const [kw, setKw] = useState("");

  // 演示数据（可接后端）
  const allPosts = useMemo(
    () => [
      {
        id: 1,
        avatar: "🧑",
        username: "晚风 k515",
        meta: "男 · 27 · 开封市",
        content:
          "希望对方能活泼开朗一点，平时可以像朋友一样，能一起聊天，不会打扰你的生活，节假日可以一起出游。",
        tag: "feed",
        liked: false,
        like: 12,
        comment: 3,
        follow: false,
      },
      {
        id: 2,
        avatar: "🧑‍🚀",
        username: "七洋",
        meta: "男 · 27 · 沈阳市",
        content:
          "必须要小孩，共同抚养。希望一年内结婚，给双方家庭一个完整的答案。性格成熟，情绪稳定，心里别太多事。",
        tag: "wall",
        liked: true,
        like: 86,
        comment: 9,
        follow: false,
      },
      {
        id: 3,
        avatar: "🐶",
        username: "nananow",
        meta: "男 · 30 · 株洲市",
        content:
          "长相阳光开朗，不娘不丑，过得去。性格随和，无不良嗜好，三观正直。喜欢小孩，希望可以一起养只猫或狗。",
        tag: "story",
        liked: false,
        like: 21,
        comment: 2,
        follow: true,
      },
      {
        id: 4,
        avatar: "👩",
        username: "小林",
        meta: "女 · 24 · 海淀区",
        content:
          "今天学校图书馆新开了自习区，环境很安静～欢迎一起学习，互相监督打卡！",
        tag: "feed",
        liked: false,
        like: 5,
        comment: 0,
        follow: false,
      },
    ],
    []
  );

  const [posts, setPosts] = useState(allPosts);

  const filtered = posts.filter(
    (p) =>
      (active === "follow" ? p.follow : p.tag === active || active === "feed") &&
      (!kw ||
        p.username.includes(kw) ||
        p.meta.includes(kw) ||
        p.content.includes(kw))
  );

  const toggleLike = (id) =>
    setPosts((arr) =>
      arr.map((p) =>
        p.id === id ? { ...p, liked: !p.liked, like: p.liked ? p.like - 1 : p.like + 1 } : p
      )
    );

  const toggleFollow = (id) =>
    setPosts((arr) =>
      arr.map((p) =>
        p.id === id ? { ...p, follow: !p.follow } : p
      )
    );

  // “发布动态”——演示：插到列表最前
  const handlePublish = () => {
    const title = window.prompt("写点什么吧（示例：今天在自习室很高效！）");
    if (!title) return;
    const newPost = {
      id: Date.now(),
      avatar: "🌿",
      username: "我",
      meta: "同学 · 校园",
      content: title,
      tag: active === "wall" || active === "story" ? active : "feed",
      liked: false,
      like: 0,
      comment: 0,
      follow: true,
    };
    setPosts((arr) => [newPost, ...arr]);
  };

  return (
    <div className="lx-discussion">
      {/* 品牌区 */}
      <div className="lx-brand">
        <img src="/bjfu-logo.png" alt="北京林业大学" className="school-logo" />
        <h1 className="lx-title">林小问</h1>
      </div>

      {/* 卡片容器（整体玻璃卡片） */}
      <div className="lx-shell">

        {/* 顶部 Tabs + 搜索框 */}
        <div className="lx-tabs-bar">
          <div className="lx-tabs">
            {tabs.map((t) => (
              <button
                key={t.key}
                className={`lx-tab-pill ${active === t.key ? "active" : ""}`}
                onClick={() => setActive(t.key)}
              >
                {t.label}
              </button>
            ))}
          </div>

          <div className="lx-search-line">
            <input
              type="text"
              placeholder="搜索主题、作者或标签…"
              value={kw}
              onChange={(e) => setKw(e.target.value)}
            />
            <button className="lx-btn">搜索</button>
          </div>
        </div>

        {/* 信息流列表 */}
        <div className="lx-feed">
          {filtered.map((p) => (
            <article className="lx-feed-card" key={p.id}>
              <div className="lx-feed-hd">
                <div className="lx-avatar" aria-hidden>{p.avatar}</div>
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
                  aria-label="点赞"
                >
                  👍 <span>{p.like}</span>
                </button>
                <button className="lx-act" aria-label="评论">
                  💬 <span>{p.comment}</span>
                </button>
                <button className="lx-act" aria-label="更多">
                  ⋯
                </button>
              </div>
            </article>
          ))}

          {filtered.length === 0 && (
            <div className="lx-empty">没有内容，换个筛选或发布一条试试～</div>
          )}
        </div>
      </div>

      {/* 底部悬浮“发布动态” */}
      <button className="lx-fab" onClick={handlePublish}>发布动态</button>
    </div>
  );
}
