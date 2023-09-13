package co.mgentertainment.common.model;

/**
 * @author larry
 * @createTime 2023/9/3
 * @description key命名格式约定：app_usage
 * 应用名缩写:player:p manager:m configcenter:c gateway:g file-service:f
 */
public interface RedisKeys {

    // hashkey表示注册用户，hash value:clientId,{userInfo}
    String PLAYER_USER_REG_CLIENT = "p_client";
    // hashkey表示设备登录用户，hash value:clientId,loginUid
    String PLAYER_USER_LOGIN_CLIENT = "p_client_user";

}
