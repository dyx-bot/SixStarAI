package com.example.appbacked.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatResponseTest {

    private ChatResponse chatResponse;

    @BeforeEach
    void setUp() {
        chatResponse = new ChatResponse();
    }

    @Test
    void chatResponseShouldBeCreated() {
        ChatResponse response = new ChatResponse();
        assertNotNull(response);
    }

    @Test
    void idGetterAndSetterShouldWork() {
        chatResponse.setId("test-id");
        assertEquals("test-id", chatResponse.getId());
    }

    @Test
    void typeGetterAndSetterShouldWork() {
        chatResponse.setType("response-type");
        assertEquals("response-type", chatResponse.getType());
    }

    @Test
    void closeGetterAndSetterShouldWork() {
        chatResponse.setClose(true);
        assertTrue(chatResponse.getClose());

        chatResponse.setClose(false);
        assertFalse(chatResponse.getClose());
    }

    @Test
    void errorGetterAndSetterShouldWork() {
        String errorMessage = "Test error";
        chatResponse.setError(errorMessage);
        assertEquals(errorMessage, chatResponse.getError());

        Exception exception = new RuntimeException("Test exception");
        chatResponse.setError(exception);
        assertEquals(exception, chatResponse.getError());
    }

    @Test
    void chatIdGetterAndSetterShouldWork() {
        chatResponse.setChatId(123);
        assertEquals(Integer.valueOf(123), chatResponse.getChatId());
    }

    @Test
    void textResponseGetterAndSetterShouldWork() {
        chatResponse.setTextResponse("Test response text");
        assertEquals("Test response text", chatResponse.getTextResponse());
    }

    @Test
    void sourcesGetterAndSetterShouldWork() {
        List<ChatResponse.Source> sources = new ArrayList<>();
        ChatResponse.Source source = new ChatResponse.Source();
        source.setTitle("Test Source");
        source.setText("Test content");
        sources.add(source);

        chatResponse.setSources(sources);
        assertEquals(sources, chatResponse.getSources());
        assertEquals(1, chatResponse.getSources().size());
        assertEquals("Test Source", chatResponse.getSources().get(0).getTitle());
    }

    @Test
    void metricsGetterAndSetterShouldWork() {
        ChatResponse.Metrics metrics = new ChatResponse.Metrics();
        metrics.setPrompt_tokens(100);
        metrics.setCompletion_tokens(50);
        metrics.setTotal_tokens(150);

        chatResponse.setMetrics(metrics);
        assertEquals(metrics, chatResponse.getMetrics());
        assertEquals(Integer.valueOf(100), chatResponse.getMetrics().getPrompt_tokens());
        assertEquals(Integer.valueOf(50), chatResponse.getMetrics().getCompletion_tokens());
        assertEquals(Integer.valueOf(150), chatResponse.getMetrics().getTotal_tokens());
    }

    @Test
    void idShouldBeNullByDefault() {
        assertNull(chatResponse.getId());
    }

    @Test
    void typeShouldBeNullByDefault() {
        assertNull(chatResponse.getType());
    }

    @Test
    void closeShouldBeNullByDefault() {
        assertNull(chatResponse.getClose());
    }

    @Test
    void errorShouldBeNullByDefault() {
        assertNull(chatResponse.getError());
    }

    @Test
    void chatIdShouldBeNullByDefault() {
        assertNull(chatResponse.getChatId());
    }

    @Test
    void textResponseShouldBeNullByDefault() {
        assertNull(chatResponse.getTextResponse());
    }

    @Test
    void sourcesShouldBeNullByDefault() {
        assertNull(chatResponse.getSources());
    }

    @Test
    void metricsShouldBeNullByDefault() {
        assertNull(chatResponse.getMetrics());
    }

    @Test
    void toStringShouldReturnString() {
        String result = chatResponse.toString();
        assertNotNull(result);
        assertTrue(result.contains("ChatResponse"));
    }

    // Source 内部类测试
    @Test
    void sourceShouldBeCreated() {
        ChatResponse.Source source = new ChatResponse.Source();
        assertNotNull(source);
    }

    @Test
    void sourceTextGetterAndSetterShouldWork() {
        ChatResponse.Source source = new ChatResponse.Source();
        source.setText("Test content");
        assertEquals("Test content", source.getText());
    }

    @Test
    void sourceIdGetterAndSetterShouldWork() {
        ChatResponse.Source source = new ChatResponse.Source();
        source.setId("source-id");
        assertEquals("source-id", source.getId());
    }

    @Test
    void sourceUrlGetterAndSetterShouldWork() {
        ChatResponse.Source source = new ChatResponse.Source();
        source.setUrl("http://example.com");
        assertEquals("http://example.com", source.getUrl());
    }

    @Test
    void sourceTitleGetterAndSetterShouldWork() {
        ChatResponse.Source source = new ChatResponse.Source();
        source.setTitle("Test Title");
        assertEquals("Test Title", source.getTitle());
    }

    @Test
    void sourceDocAuthorGetterAndSetterShouldWork() {
        ChatResponse.Source source = new ChatResponse.Source();
        source.setDocAuthor("Test Author");
        assertEquals("Test Author", source.getDocAuthor());
    }

    @Test
    void sourceDescriptionGetterAndSetterShouldWork() {
        ChatResponse.Source source = new ChatResponse.Source();
        source.setDescription("Test Description");
        assertEquals("Test Description", source.getDescription());
    }

    @Test
    void sourceDocSourceGetterAndSetterShouldWork() {
        ChatResponse.Source source = new ChatResponse.Source();
        source.setDocSource("Test Doc Source");
        assertEquals("Test Doc Source", source.getDocSource());
    }

    @Test
    void sourceChunkSourceGetterAndSetterShouldWork() {
        ChatResponse.Source source = new ChatResponse.Source();
        source.setChunkSource("Test Chunk Source");
        assertEquals("Test Chunk Source", source.getChunkSource());
    }

    @Test
    void sourcePublishedGetterAndSetterShouldWork() {
        ChatResponse.Source source = new ChatResponse.Source();
        source.setPublished("2023-01-01");
        assertEquals("2023-01-01", source.getPublished());
    }

    @Test
    void sourceWordCountGetterAndSetterShouldWork() {
        ChatResponse.Source source = new ChatResponse.Source();
        source.setWordCount(1000);
        assertEquals(Integer.valueOf(1000), source.getWordCount());
    }

    @Test
    void sourceTokenCountEstimateGetterAndSetterShouldWork() {
        ChatResponse.Source source = new ChatResponse.Source();
        source.setToken_count_estimate(500);
        assertEquals(Integer.valueOf(500), source.getToken_count_estimate());
    }

    @Test
    void sourceTextShouldBeNullByDefault() {
        ChatResponse.Source source = new ChatResponse.Source();
        assertNull(source.getText());
    }

    @Test
    void sourceIdShouldBeNullByDefault() {
        ChatResponse.Source source = new ChatResponse.Source();
        assertNull(source.getId());
    }

    @Test
    void sourceUrlShouldBeNullByDefault() {
        ChatResponse.Source source = new ChatResponse.Source();
        assertNull(source.getUrl());
    }

    @Test
    void sourceTitleShouldBeNullByDefault() {
        ChatResponse.Source source = new ChatResponse.Source();
        assertNull(source.getTitle());
    }

    @Test
    void sourceDocAuthorShouldBeNullByDefault() {
        ChatResponse.Source source = new ChatResponse.Source();
        assertNull(source.getDocAuthor());
    }

    @Test
    void sourceDescriptionShouldBeNullByDefault() {
        ChatResponse.Source source = new ChatResponse.Source();
        assertNull(source.getDescription());
    }

    @Test
    void sourceDocSourceShouldBeNullByDefault() {
        ChatResponse.Source source = new ChatResponse.Source();
        assertNull(source.getDocSource());
    }

    @Test
    void sourceChunkSourceShouldBeNullByDefault() {
        ChatResponse.Source source = new ChatResponse.Source();
        assertNull(source.getChunkSource());
    }

    @Test
    void sourcePublishedShouldBeNullByDefault() {
        ChatResponse.Source source = new ChatResponse.Source();
        assertNull(source.getPublished());
    }

    @Test
    void sourceWordCountShouldBeNullByDefault() {
        ChatResponse.Source source = new ChatResponse.Source();
        assertNull(source.getWordCount());
    }

    @Test
    void sourceTokenCountEstimateShouldBeNullByDefault() {
        ChatResponse.Source source = new ChatResponse.Source();
        assertNull(source.getToken_count_estimate());
    }

    @Test
    void sourceToStringShouldReturnString() {
        ChatResponse.Source source = new ChatResponse.Source();
        source.setTitle("Test Title");
        source.setText("Test content");

        String result = source.toString();
        assertNotNull(result);
        assertTrue(result.contains("Source"));
        assertTrue(result.contains("Test Title"));
    }

    // Metrics 内部类测试
    @Test
    void metricsShouldBeCreated() {
        ChatResponse.Metrics metrics = new ChatResponse.Metrics();
        assertNotNull(metrics);
    }

    @Test
    void metricsPromptTokensGetterAndSetterShouldWork() {
        ChatResponse.Metrics metrics = new ChatResponse.Metrics();
        metrics.setPrompt_tokens(100);
        assertEquals(Integer.valueOf(100), metrics.getPrompt_tokens());
    }

    @Test
    void metricsCompletionTokensGetterAndSetterShouldWork() {
        ChatResponse.Metrics metrics = new ChatResponse.Metrics();
        metrics.setCompletion_tokens(50);
        assertEquals(Integer.valueOf(50), metrics.getCompletion_tokens());
    }

    @Test
    void metricsTotalTokensGetterAndSetterShouldWork() {
        ChatResponse.Metrics metrics = new ChatResponse.Metrics();
        metrics.setTotal_tokens(150);
        assertEquals(Integer.valueOf(150), metrics.getTotal_tokens());
    }

    @Test
    void metricsOutputTpsGetterAndSetterShouldWork() {
        ChatResponse.Metrics metrics = new ChatResponse.Metrics();
        metrics.setOutputTps(25.5);
        assertEquals(Double.valueOf(25.5), metrics.getOutputTps());
    }

    @Test
    void metricsDurationGetterAndSetterShouldWork() {
        ChatResponse.Metrics metrics = new ChatResponse.Metrics();
        metrics.setDuration(1.25);
        assertEquals(Double.valueOf(1.25), metrics.getDuration());
    }

    @Test
    void metricsPromptTokensShouldBeNullByDefault() {
        ChatResponse.Metrics metrics = new ChatResponse.Metrics();
        assertNull(metrics.getPrompt_tokens());
    }

    @Test
    void metricsCompletionTokensShouldBeNullByDefault() {
        ChatResponse.Metrics metrics = new ChatResponse.Metrics();
        assertNull(metrics.getCompletion_tokens());
    }

    @Test
    void metricsTotalTokensShouldBeNullByDefault() {
        ChatResponse.Metrics metrics = new ChatResponse.Metrics();
        assertNull(metrics.getTotal_tokens());
    }

    @Test
    void metricsOutputTpsShouldBeNullByDefault() {
        ChatResponse.Metrics metrics = new ChatResponse.Metrics();
        assertNull(metrics.getOutputTps());
    }

    @Test
    void metricsDurationShouldBeNullByDefault() {
        ChatResponse.Metrics metrics = new ChatResponse.Metrics();
        assertNull(metrics.getDuration());
    }

    @Test
    void metricsToStringShouldReturnString() {
        ChatResponse.Metrics metrics = new ChatResponse.Metrics();
        metrics.setPrompt_tokens(100);
        metrics.setCompletion_tokens(50);
        metrics.setDuration(1.25);

        String result = metrics.toString();
        assertNotNull(result);
        assertTrue(result.contains("Metrics"));
        assertTrue(result.contains("100"));
        assertTrue(result.contains("50"));
        assertTrue(result.contains("1.25"));
    }
}
