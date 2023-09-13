package co.mgentertainment.common.mybatisplus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ClassUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

/**
 * MybatisPlus 自动填充配置
 *
 * @author L.cm
 */
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now1 = LocalDateTime.now();
        Date now2 = new Date();

        fillValIfNullByName("createTime", new Object[]{now1, now2}, metaObject, false);
        fillValIfNullByName("updateTime", new Object[]{now1, now2}, metaObject, false);
        fillValIfNullByName("createBy", new Object[]{getUserName()}, metaObject, false);
        fillValIfNullByName("updateBy", new Object[]{getUserName()}, metaObject, false);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        fillValIfNullByName("updateTime", new Object[]{LocalDateTime.now(), new Date()}, metaObject, true);
        fillValIfNullByName("updateBy", new Object[]{getUserName()}, metaObject, true);
    }

    /**
     * 填充值，先判断是否有手动设置，优先手动设置的值，例如：job必须手动设置
     *
     * @param fieldName  属性名
     * @param fieldVals  属性值
     * @param metaObject MetaObject
     * @param isCover    是否覆盖原有值,避免更新操作手动入参
     */
    private static void fillValIfNullByName(String fieldName, Object[] fieldVals, MetaObject metaObject, boolean isCover) {
        // 1. 没有 set 方法
        if (!metaObject.hasSetter(fieldName)) {
            return;
        }
        // 2. 如果用户有手动设置的值
        Object userSetValue = metaObject.getValue(fieldName);
        if (userSetValue != null && !isCover) {
            return;
        }
        // 3. field 类型相同时设置
        Class<?> getterType = metaObject.getGetterType(fieldName);
        for (Object fieldVal : fieldVals) {
            if (ClassUtils.isAssignableValue(getterType, fieldVal)) {
                metaObject.setValue(fieldName, fieldVal);
                break;
            }
        }
    }

    /**
     * 获取 spring security 当前的用户名
     *
     * @return 当前用户名
     */
    private String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Optional.ofNullable(authentication).isPresent()) {
            return authentication.getName();
        }
        return null;
    }

}
