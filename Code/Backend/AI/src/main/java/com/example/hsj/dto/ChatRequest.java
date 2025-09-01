package com.example.hsj.dto;

import java.util.List;

public class ChatRequest {
    private String message;
    private String mode;
    private String sessionId;
    private List<Attachment> attachments;
    private Boolean reset;

    // 必须有无参构造函数（Jackson 反序列化需要）
    public ChatRequest() {}

    public ChatRequest(String message, String mode) {
        this.message = message;
        this.mode = mode;
    }

    // Getter 和 Setter
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public List<Attachment> getAttachments() { return attachments; }
    public void setAttachments(List<Attachment> attachments) { this.attachments = attachments; }

    public Boolean getReset() { return reset; }
    public void setReset(Boolean reset) { this.reset = reset; }

    // 附件内嵌类
    public static class Attachment {
        private String name;
        private String mime;
        private String contentString;

        public Attachment() {}

        public Attachment(String name, String mime, String contentString) {
            this.name = name;
            this.mime = mime;
            this.contentString = contentString;
        }

        // Getter 和 Setter
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getMime() { return mime; }
        public void setMime(String mime) { this.mime = mime; }

        public String getContentString() { return contentString; }
        public void setContentString(String contentString) { this.contentString = contentString; }
    }
}