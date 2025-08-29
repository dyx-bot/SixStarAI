import React, { useState, useRef } from "react";
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
        mode:"chat",
        sessionId:"user123-sessions456",
        reset: false
      }),
    });

    // 检查状态码是否为 400
    if (!res.ok) {
      const errorData = await res.json();
      setTyping(false);
      setList((arr) => [
        ...arr,
        { role: "assistant", avatar: "/bjfu-logo.png", textResponse: errorData.message || "请求失败" },
      ]);
      return;
    }

    const data = await res.json();
    setTyping(false);
    setList((arr) => [
      ...arr,
      { role: "assistant", avatar: "/bjfu-logo.png", textResponse: data.textResponse || "（无回复）" },
    ]);
  } catch (err) {
    setTyping(false);
    setList((arr) => [
      ...arr,
      { role: "assistant", avatar: "/bjfu-logo.png", textResponse: "网络错误：" + err.message },
    ]);
  }
};


  const openImagePicker = () => fileRef.current?.click();

  return (
    <div className="m-chat">
      <div className="m-chat__wrap">
        {/* 聊天面板 */}
        <div className="chat-surface">
          <div className="chat-list">
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
                  <span></span><span></span><span></span>
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
              <path d="M9 3l1.5 2H18a2 2 0 012 2v10a2 2 0 01-2 2H6a2 2 0 01-2-2V7a2 2 0 012-2h2.5L10 3h-1z" fill="none" stroke="currentColor" strokeWidth="1.8" />
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

          {/* <button className="icon-btn" onClick={() => alert("更多功能")} aria-label="更多">
            <svg viewBox="0 0 24 24" width="22" height="22">
              <path d="M12 4v16M4 12h16" stroke="currentColor" strokeWidth="2" strokeLinecap="round" />
            </svg>
          </button> */}

          <input ref={fileRef} type="file" accept="image/*" style={{ display: "none" }} />
        </div>
      </div>

      {/* 主题化配色：AI 超浅薄荷青蓝渐变 + 你的白底绿描边 */}
      <style>{`
        :root{
          --mint-50:#f5fffb;
          --mint-100:#e9fbf4;
          --mint-200:#d7f6ea;
          --mint-300:#bdf0db;

          --teal-600:#15604c;      /* 深绿蓝（文字） */
          --ai-glow:#8fdff0;       /* AI 轻微外发光 */
          --ai-grad-a:#e9fff9;     /* 渐变起点（薄荷白） */
          --ai-grad-b:#e2fbff;     /* 渐变中段（浅青蓝） */
          --ai-grad-c:#d3f6ff;     /* 渐变终点（更浅青蓝） */
        }

        .m-chat{
          display:flex;justify-content:center;min-height:calc(100svh - 150px);
        }
        .m-chat__wrap{
          width:min(420px,96vw);
          display:grid;grid-template-rows:1fr auto;gap:10px;
        }

        .chat-surface{
          background:#fff;border-radius:16px;
          box-shadow:0 10px 24px rgba(22,107,78,.10);
          padding:6px;
        }

        .chat-list{
          display:grid;gap:6px;
          padding:6px 4px 8px;
          overflow:auto;align-content:start;
        }

        .chat-row{display:flex;align-items:center;gap:6px;padding:0;}
        .chat-row.assistant{justify-content:flex-start;padding-left:6px;} /* 6px 左安全边 */
        .chat-row.user{justify-content:flex-end;padding-right:2px;}

        /* AI 头像（保持小巧） */
        .avatar{width:26px;height:26px;flex:0 0 26px}
        .avatar img{width:26px;height:26px;border-radius:999px;border:1px solid #e9f2ef;background:#fff}

        /* ===== 气泡 ===== */
        .bubble{
          max-width:78%;
          padding:8px 12px;
          border-radius:16px;
          font-size:14px; line-height:1.55;
          position:relative;
          word-wrap:break-word; overflow-wrap:break-word;
        }

        /* AI：超浅薄荷青蓝渐变 + 轻微外发光（保持“AI感”但不突兀） */
        .bubble.assistant{
          color:var(--teal-600);
          font-weight:600;
          background:linear-gradient(180deg,var(--ai-grad-a) 0%, var(--ai-grad-b) 45%, var(--ai-grad-c) 100%);
          box-shadow:
            0 1px 0 rgba(16,94,72,.06),
            0 0 0 1px rgba(125, 226, 182, .18),
            0 6px 16px rgba(143,223,240,.18); /* 轻微青蓝光晕 */
          white-space: pre-line; /* 添加这行代码 */
          word-break: break-word; 
        }
        .bubble.assistant .tail{
          position:absolute;left:-6px;top:50%;transform:translateY(-50%);
          border:6px solid transparent;border-right-color:var(--ai-grad-c);
        }

        /* 你这边：白底 + 绿色细描边 + 柔和阴影，整体更干净 */
/* 你这边：回到浅绿渐变 */
.bubble.user{
  color:#184a38;
  background:linear-gradient(
    180deg,
    #f2fff9 0%,   /* 上：淡白绿 */
    #d9f9ec 40%,  /* 中：浅绿色 */
    #bdf0db 100%  /* 下：稍深一点 */
  );
}
.bubble.user .tail{
  position:absolute;
  right:-6px;
  top:50%;
  transform:translateY(-50%);
  border:6px solid transparent;
  border-left-color:#bdf0db;
}

        /* 打字动画（保持一致） */
        .bubble.typing{
          display:flex;gap:3px;align-items:center;justify-content:center;width:42px;height:24px
        }
        .bubble.typing span{
          width:6px;height:6px;border-radius:50%;background:#bfeefc;
          animation:blink 1.4s infinite ease-in-out
        }
        .bubble.typing span:nth-child(2){animation-delay:.2s}
        .bubble.typing span:nth-child(3){animation-delay:.4s}
        @keyframes blink{0%,80%,100%{transform:scale(.8);opacity:.4}40%{transform:scale(1);opacity:1}}

        /* 底部输入栏（不变） */
        .sendbar{
          display:grid;grid-template-columns:auto 1fr auto;align-items:center;gap:6px;
          width:min(420px,96vw);margin:0 auto;
        }
        .icon-btn{
          width:34px;height:34px;border-radius:50%;display:grid;place-items:center;
          border:none;background:#fff;color:#1f513f;
          box-shadow:0 2px 8px rgba(0,0,0,.05)
        }
        .input-shell{
          height:38px;border-radius:19px;background:#fff;display:flex;align-items:center;padding:0 12px;
          box-shadow:0 2px 8px rgba(0,0,0,.05)
        }
        .input-shell input{
          width:100%;border:none;outline:none;background:transparent;font-size:15px;color:#184a38;
        }
      `}</style>
    </div>
  );
}
