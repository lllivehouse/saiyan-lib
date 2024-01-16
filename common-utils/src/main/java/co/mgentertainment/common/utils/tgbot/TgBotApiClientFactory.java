package co.mgentertainment.common.utils.tgbot;

/**
 * @author leo
 * @createTime 2023/6/27
 * @description TelegramBotAPI
 */
public class TgBotApiClientFactory {

    public static TgBotApiClient create(String token) {
        return TgBotApiClient.getInstance(token);
    }
}
