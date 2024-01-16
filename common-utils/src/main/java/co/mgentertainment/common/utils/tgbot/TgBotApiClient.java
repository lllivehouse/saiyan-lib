package co.mgentertainment.common.utils.tgbot;

import co.mgentertainment.common.utils.http.OkHttpClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author larry
 * @createTime 2024/1/16
 * @description TgBotApiClient
 */
public class TgBotApiClient {

    private static final String BOT_API_PREFIX = "https://api.telegram.org";

    private static volatile TgBotApiClient instance;
    private AtomicBoolean initialized = new AtomicBoolean(false);
    private CountDownLatch latch = new CountDownLatch(1);

    private String token;

    private TgBotApiClient(String apiToken) {
        if (!this.initialized.compareAndSet(false, true)) {
            try {
                this.latch.await(1, TimeUnit.SECONDS);
            } catch (InterruptedException var5) {
                throw new RuntimeException("TgBotApiClient Init Failed");
            }
        } else {
            this.token = apiToken;
        }
    }

    public static TgBotApiClient getInstance(String apiToken) {
        if (instance == null) {
            synchronized (TgBotApiClient.class) {
                if (instance == null) {
                    instance = new TgBotApiClient(apiToken);
                }
            }
        }
        return instance;
    }

    public void sendMessage(Long chatId, String text) {
        String url = String.format("%s/bot%s/sendMessage", BOT_API_PREFIX, this.token);
        Map<String, String> q = new HashMap<>();
        q.put("chat_id", String.valueOf(chatId));
        q.put("text", text);
        q.put("parse_mode", "HTML");
        OkHttpClient.getInstance().get(url, null, q);
    }
}
