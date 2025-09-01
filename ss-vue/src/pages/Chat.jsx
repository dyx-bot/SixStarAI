import React, { useState, useRef, useEffect } from "react";
import { BASE } from "../lib/api";

export default function Chat() {
  const [list, setList] = useState([
    {
      role: "assistant",
      avatar: "/bjfu-logo.png",
      textResponse: "你好，我是六星问知 AI 助手～有什么想问的吗？",
    },
  ]);
  const [msg, setMsg] = useState("");
  const [typing, setTyping] = useState(false);
  const fileRef = useRef(null);

  const listRef = useRef(null);
  const scrollToBottom = () => {
    const el = listRef.current;
    if (el) el.scrollTop = el.scrollHeight;
  };
  useEffect(scrollToBottom, [list, typing]);

  const send = async () => {
    const textResponse = msg.trim();
    if (!textResponse) return;
    setList((arr) => [...arr, { role: "user", textResponse }]);
    setMsg("");
    setTyping(true);

    try {
      const res = await fetch(`${BASE}/api/chat`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          message: textResponse,
          mode: "chat",
          sessionId: "user123-sessions456",
          reset: false,
        }),
      });

      if (!res.ok) {
        const errorData = await res.json().catch(() => ({}));
        setTyping(false);
        setList((arr) => [
          ...arr,
          {
            role: "assistant",
            avatar: "/bjfu-logo.png",
            textResponse: errorData?.message || "请求失败",
          },
        ]);
        return;
      }

      const data = await res.json();
      setTyping(false);
      setList((arr) => [
        ...arr,
        {
          role: "assistant",
          avatar: "/bjfu-logo.png",
          textResponse: data.textResponse || "（无回复）",
        },
      ]);
    } catch (err) {
      setTyping(false);
      setList((arr) => [
        ...arr,
        {
          role: "assistant",
          avatar: "/bjfu-logo.png",
          textResponse: "网络错误：" + err.message,
        },
      ]);
    }
  };

  const openImagePicker = () => fileRef.current?.click();

  return (
    <div className="m-chat">
      <div className="m-chat__wrap">
        {/* 聊天面板 */}
        <div className="chat-surface">
          <div className="chat-list" ref={listRef}>
            {list.map((m, i) => (
              <div key={i} className={`chat-row ${m.role}`}>
                {m.role === "assistant" && (
                  <div className="avatar">
                    <img src={m.avatar} alt="六星问知" />
                  </div>
                )}
                <div className={`bubble ${m.role}`}>
                  {m.textResponse}
                  <span className="tail" />
                </div>
              </div>
            ))}

            {typing && (
              <div className="chat-row assistant">
                <div className="avatar">
                  <img src="/bjfu-logo.png" alt="六星问知" />
                </div>
                <div className="bubble assistant typing">
                  <span></span>
                  <span></span>
                  <span></span>
                  <span className="tail" />
                </div>
              </div>
            )}
          </div>
        </div>

        {/* 底部输入栏 */}
        <div className="sendbar">
          <button className="icon-btn" onClick={openImagePicker} aria-label="选择图片">
            <svg viewBox="0 0 24 24" width="22" height="22">
              <path
                d="M9 3l1.5 2H18a2 2 0 012 2v10a2 2 0 01-2 2H6a2 2 0 01-2-2V7a2 2 0 012-2h2.5L10 3h-1z"
                fill="none"
                stroke="currentColor"
                strokeWidth="1.8"
              />
              <circle cx="12" cy="12" r="3.5" fill="none" stroke="currentColor" strokeWidth="1.8" />
            </svg>
          </button>

          <div className="input-shell">
            <input
              value={msg}
              onChange={(e) => setMsg(e.target.value)}
              placeholder="发送消息…"
              onKeyDown={(e) => e.key === "Enter" && send()}
            />
          </div>

          <input ref={fileRef} type="file" accept="image/*" style={{ display: "none" }} />
        </div>
      </div>

      {/* 注意：所有样式都以 .m-chat 开头，只影响本页 */}
      <style>{`
        :root{
          --teal-600:#15604c;
          --ai-grad-a:#e9fff9;
          --ai-grad-b:#e2fbff;
          --ai-grad-c:#d3f6ff;

          /* 头像与首行文字的“对齐微调”，需要更低/更高改这里即可（10~14px） */
          --avatar-nudge: 12px;
        }

        /* === 居中与布局（只针对聊天页） === */
        .m-chat{ height:100%; display:flex; }
        .m-chat .m-chat__wrap{
          width:min(420px,100%);
          margin:0 auto;
          display:grid;
          grid-template-rows:1fr auto;   /* 上：消息滚动；下：输入栏 */
          gap:10px;
          min-height:0;
        }

        .m-chat .chat-surface{
          background:#fff; border-radius:16px;
          box-shadow:0 10px 24px rgba(22,107,78,.10);
          padding:2px;
          display:flex; flex-direction:column;
          overflow:hidden; min-height:0; /* 外层不滚，交给列表滚 */
        }

        .m-chat .chat-list{
          flex:1; overflow:auto;
          display:grid; gap:6px;
          padding:6px 2px 8px 2px;   /* 左侧更窄一点 */
          align-content:start;
          min-height:0;
          scrollbar-gutter: stable both-edges;
        }

        /* 行：顶部对齐 */
        .chat-row{ display:flex; align-items:flex-start; gap:6px; padding:0; }

        /* 助手侧在左，安全边再小一点 */
        .chat-row.assistant{ justify-content:flex-start; padding-left:4px; }

        /* 头像：与首行文字视觉对齐（下移一点） */
        .chat-row.assistant .avatar{
          flex:0 0 26px;
          width:26px; height:26px;
          align-self:flex-start;
          margin-top: var(--avatar-nudge);   /* ★ 关键微调 */
        }

        /* 用户侧 */
        .m-chat .chat-row.user{ justify-content:flex-end; padding-right:6px; }

        .m-chat .avatar img{
          width:26px; height:26px;
          border-radius:999px; border:1px solid #e9f2ef; background:#fff;
        }

        .m-chat .bubble{
          max-width:78%;
          padding:8px 12px;                /* 首行上内边距 8px，用于估算对齐 */
          border-radius:16px;
          font-size:14px; line-height:1.55;
          position:relative;
          word-wrap:break-word; overflow-wrap:break-word;
        }

        .m-chat .bubble.assistant{
          color:var(--teal-600);
          font-weight:600;
          background:linear-gradient(180deg,var(--ai-grad-a) 0%, var(--ai-grad-b) 45%, var(--ai-grad-c) 100%);
          box-shadow:
            0 1px 0 rgba(16,94,72,.06),
            0 0 0 1px rgba(125,226,182,.18),
            0 6px 16px rgba(155,215,230,.18);
          white-space: pre-line;
          word-break: break-word;
        }
        .m-chat .bubble.assistant .tail{
          position:absolute;
          left:-5px;                       /* 安全边缩后，尾巴也收一点 */
          top:50%; transform:translateY(-50%);
          border:6px solid transparent; border-right-color:var(--ai-grad-c);
        }

        .m-chat .bubble.user{
          color:#184a38;
          background:linear-gradient(180deg,#f2fff9 0%,#d9f9ec 40%,#bdf0db 100%);
        }
        .m-chat .bubble.user .tail{
          position:absolute; right:-6px; top:50%; transform:translateY(-50%);
          border:6px solid transparent; border-left-color:#bdf0db;
        }

        .m-chat .bubble.typing{
          display:flex; gap:3px; align-items:center; justify-content:center; width:42px; height:24px
        }
        .m-chat .bubble.typing span{
          width:6px; height:6px; border-radius:50%; background:#bfeefc;
          animation:blink 1.4s infinite ease-in-out
        }
        .m-chat .bubble.typing span:nth-child(2){ animation-delay:.2s }
        .m-chat .bubble.typing span:nth-child(3){ animation-delay:.4s }
        @keyframes blink{0%,80%,100%{transform:scale(.8);opacity:.4}40%{transform:scale(1);opacity:1}}

        /* === 底部输入栏 === */
        .m-chat .sendbar{
          display:grid; grid-template-columns:auto 1fr; align-items:center; gap:6px;
          width:100%; margin:0 auto;
        }
        .m-chat .icon-btn{
          width:34px; height:34px; border-radius:50%; display:grid; place-items:center;
          border:none; background:#fff; color:#1f513f;
          box-shadow:0 2px 8px rgba(0,0,0,.05)
        }
        .m-chat .input-shell{
          height:38px; border-radius:19px; background:#fff; display:flex; align-items:center; padding:0 12px;
          box-shadow:0 2px 8px rgba(0,0,0,.05)
        }
        .m-chat .input-shell input{
          width:100%; border:none; outline:none; background:transparent; font-size:15px; color:#184a38;
        }

        /* 滚动条（仅聊天列表） */
        .m-chat .chat-list::-webkit-scrollbar{ width:6px; }
        .m-chat .chat-list::-webkit-scrollbar-track{ background:transparent; }
        .m-chat .chat-list::-webkit-scrollbar-thumb{ background:rgba(22,107,78,.25); border-radius:999px; }
        .m-chat .chat-list::-webkit-scrollbar-thumb:hover{ background:rgba(22,107,78,.35); }
        .m-chat .chat-list::-webkit-scrollbar-button{ display:none; width:0; height:0; }
        .m-chat .chat-list{ scrollbar-width:thin; scrollbar-color:rgba(22,107,78,.25) transparent; }
      `}</style>
    </div>
  );
}
