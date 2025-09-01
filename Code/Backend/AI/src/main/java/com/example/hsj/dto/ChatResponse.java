// ChatResponse.java
package com.example.hsj.dto;

import java.util.List;

public class ChatResponse {
    private String id;
    private String type;
    private Boolean close;
    private Object error;
    private Integer chatId;
    private String textResponse;
    private List<Source> sources;
    private Metrics metrics;

    // Getter 和 Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getClose() {
        return close;
    }

    public void setClose(Boolean close) {
        this.close = close;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public String getTextResponse() {
        return textResponse;
    }

    public void setTextResponse(String textResponse) {
        this.textResponse = textResponse;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    @Override
    public String toString() {
        return "ChatResponse{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", close=" + close +
                ", error=" + error +
                ", chatId=" + chatId +
                ", textResponse='" + textResponse + '\'' +
                ", sources=" + sources +
                ", metrics=" + metrics +
                '}';
    }

    // 内部类：Source（来源文档信息）
    public static class Source {
        private String text;
        private String id;
        private String url;
        private String title;
        private String docAuthor;
        private String description;
        private String docSource;
        private String chunkSource;
        private String published;
        private Integer wordCount;
        private Integer token_count_estimate;

        // Getter 和 Setter
        // ... 省略（按需生成）

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDocAuthor() {
            return docAuthor;
        }

        public void setDocAuthor(String docAuthor) {
            this.docAuthor = docAuthor;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDocSource() {
            return docSource;
        }

        public void setDocSource(String docSource) {
            this.docSource = docSource;
        }

        public String getChunkSource() {
            return chunkSource;
        }

        public void setChunkSource(String chunkSource) {
            this.chunkSource = chunkSource;
        }

        public String getPublished() {
            return published;
        }

        public void setPublished(String published) {
            this.published = published;
        }

        public Integer getWordCount() {
            return wordCount;
        }

        public void setWordCount(Integer wordCount) {
            this.wordCount = wordCount;
        }

        public Integer getToken_count_estimate() {
            return token_count_estimate;
        }

        public void setToken_count_estimate(Integer token_count_estimate) {
            this.token_count_estimate = token_count_estimate;
        }

        @Override
        public String toString() {
            return "Source{" +
                    "text='" + text + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

    // 内部类：Metrics（性能指标）
    public static class Metrics {
        private Integer prompt_tokens;
        private Integer completion_tokens;
        private Integer total_tokens;
        private Double outputTps;
        private Double duration;

        // Getter 和 Setter
        // ... 省略（按需生成）

        public Integer getPrompt_tokens() {
            return prompt_tokens;
        }

        public void setPrompt_tokens(Integer prompt_tokens) {
            this.prompt_tokens = prompt_tokens;
        }

        public Integer getCompletion_tokens() {
            return completion_tokens;
        }

        public void setCompletion_tokens(Integer completion_tokens) {
            this.completion_tokens = completion_tokens;
        }

        public Integer getTotal_tokens() {
            return total_tokens;
        }

        public void setTotal_tokens(Integer total_tokens) {
            this.total_tokens = total_tokens;
        }

        public Double getOutputTps() {
            return outputTps;
        }

        public void setOutputTps(Double outputTps) {
            this.outputTps = outputTps;
        }

        public Double getDuration() {
            return duration;
        }

        public void setDuration(Double duration) {
            this.duration = duration;
        }

        @Override
        public String toString() {
            return "Metrics{" +
                    "prompt_tokens=" + prompt_tokens +
                    ", completion_tokens=" + completion_tokens +
                    ", duration=" + duration +
                    '}';
        }
    }
}