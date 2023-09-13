package co.mgentertainment.common.security.utils;

import co.mgentertainment.common.security.model.SysUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author larry
 * @createTime 2023/8/24
 * @description SysUserHelper
 */
public class SysUserHelper {

    public static SysUserDetails getCurrentUser() {
        Object principal = getAuthentication().getPrincipal();
        if (principal instanceof SysUserDetails) {
            return (SysUserDetails) principal;
        }
        return null;
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
