package co.mgentertainment.common.model.data;

import java.util.Arrays;

/**
 * @author larry
 * @createTime 2023/9/14
 * @description IndicatorKeyEnum
 */
public enum IndicatorKeyEnum {

    ACTIVE_USER_COUNT("ACTIVE_USER_COUNT", "活跃用户数"),
    ACTIVE_IP_COUNT("ACTIVE_IP_COUNT", "活跃IP数"),
    ACTIVE_ANDROID_USER_COUNT("ACTIVE_ANDROID_USER_COUNT", "安卓活跃用户数"),
    ACTIVE_IOS_USER_COUNT("ACTIVE_IOS_USER_COUNT", "iOS活跃用户数"),
    NEW_USER_COUNT("NEW_USER_COUNT", "新增用户数"),
    NEW_INVITE_USER_COUNT("NEW_INVITE_USER_COUNT", "裂变新增用户数"),
    NEW_NATURAL_USER_COUNT("NEW_NATURAL_USER_COUNT", "自然新增用户数"),
    NEW_ANDROID_USER_COUNT("NEW_ANDROID_USER_COUNT", "安卓新增用户数"),
    NEW_IOS_USER_COUNT("NEW_IOS_USER_COUNT", "iOS新增用户数"),
    REMAINED_1DAY_USER_COUNT("REMAINED_1DAY_USER_COUNT", "1日留存用户数"),
    REMAINED_3DAY_USER_COUNT("REMAINED_3DAY_USER_COUNT", "3日留存用户数"),
    REMAINED_7DAY_USER_COUNT("REMAINED_7DAY_USER_COUNT", "7日留存用户数"),
    PAID_ORDER_COUNT("PAID_ORDER_COUNT", "付费笔数"),
    PAID_USER_COUNT("PAID_USER_COUNT", "付费用户数"),
    FIRST_PAID_USER_COUNT("FIRST_PAID_USER_COUNT", "首付用户数"),
    RENEWAL_PAID_USER_COUNT("RENEWAL_PAID_USER_COUNT", "续付用户数"),
    PAID_ANDROID_USER_COUNT("PAID_ANDROID_USER_COUNT", "安卓付费用户数"),
    PAID_IOS_USER_COUNT("PAID_IOS_USER_COUNT", "iOS付费用户数"),
    PAID_ORDER_AMOUNT("PAID_ORDER_AMOUNT", "付费金额"),
    FIRST_PAID_AMOUNT("FIRST_PAID_AMOUNT", "首付金额"),
    RENEWAL_PAID_AMOUNT("RENEWAL_PAID_AMOUNT", "续付金额"),
    INVITE_USER_PAID_AMOUNT("INVITE_USER_PAID_AMOUNT", "裂变付费金额"),
    NATURAL_USER_PAID_AMOUNT("NATURAL_USER_PAID_AMOUNT", "直推付费金额"),
    ANDROID_PAID_AMOUNT("ANDROID_PAID_AMOUNT", "安卓付费金额"),
    IOS_PAID_AMOUNT("IOS_PAID_AMOUNT", "iOS付费金额"),

    LANDING_PAGE_VIEW_NUM("LANDING_PAGE_VIEW_NUM", "落地页访问量"),
    LANDING_PAGE_IP_NUM("LANDING_PAGE_IP_NUM", "落地页访问IP量"),
    INSTALLATION_NUM("INSTALLATION_NUM", "点击安装量"),
    REGISTER_API_CALL_NUM("REGISTER_API_ACCESS_NUM", "注册接口调用次数"),
    DEDUCTION_NEW_USER_COUNT("DEDUCTION_NEW_USER_COUNT", "扣量新增用户数"),
    DEDUCTION_PAID_ORDER_COUNT("DEDUCTION_PAID_ORDER_COUNT", "扣量付费笔数"),
    DEDUCTION_PAID_ORDER_AMOUNT("DEDUCTION_PAID_ORDER_AMOUNT", "扣量付费金额"),
    ;

    private final String keyName;
    private final String keyDesc;

    IndicatorKeyEnum(String keyName, String keyDesc) {
        this.keyName = keyName;
        this.keyDesc = keyDesc;
    }

    public static IndicatorKeyEnum getByKeyName(String keyName) {
        return Arrays.stream(IndicatorKeyEnum.values()).filter(item -> item.keyName.equalsIgnoreCase(keyName)).findFirst().orElse(null);
    }

    public static IndicatorKeyEnum getByKeyDesc(String keyDesc) {
        return Arrays.stream(IndicatorKeyEnum.values()).filter(item -> item.keyDesc.equals(keyDesc)).findFirst().orElse(null);
    }

    public String getKeyName() {
        return keyName;
    }

    public String getKeyDesc() {
        return keyDesc;
    }
}
