package co.mgentertainment.common.security.dal.repository;

import co.mgentertainment.common.security.dal.po.SysRoleDO;
import co.mgentertainment.common.security.dal.po.SysUserDO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author larry
 * @createTime 18/08/2023
 * @description UserInfo
 */
@Data
@Builder
public class SysUserInfo {

    /**
     * 用户基本信息
     */
    private SysUserDO sysUser;

    /**
     * 权限标识集合
     */
    private List<String> permissions;

    /**
     * 角色集合
     */
    private List<SysRoleDO> roles;
}
