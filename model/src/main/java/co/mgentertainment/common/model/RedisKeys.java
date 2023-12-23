package co.mgentertainment.common.model;

/**
 * @author larry
 * @createTime 2023/9/3
 * @description key命名格式约定：app_usage
 * 应用名缩写:player:p manager:m configcenter:c gateway:g file-service:f
 */
public interface RedisKeys {

    // hashkey表示设备登录用户，hash value:clientId,loginUid
    String PLAYER_CLIENT_LOGIN_USER = "p_client_user";
    String PLAYER_USER_LOGIN_CLIENT = "p_user_client";
    String PLAYER_INDICATOR_PREFIX = "indicator:";
    String PLAYER_RANK_KEY_FORMAT = "{}:{}:rank:{}:{}";
    String PLAYER_RANK_PAGE_KEY_FORMAT = "{}:{}:rank:{}:{}:{}";
    String PLAYER_SMS_BIND_PHONE = "SMS:Bind:Phone:{}";
    String PLAYER_SMS_LOGIN_PHONE = "SMS:Login:Phone:{}";
    String PLAYER_SMS_FIND_ACCOUNT = "SMS:Find:Phone:{}";
    String HOME_PAGE_USER_COLUMN = "home_data";
}
