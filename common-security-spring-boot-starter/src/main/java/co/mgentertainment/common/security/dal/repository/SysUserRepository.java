package co.mgentertainment.common.security.dal.repository;

import co.mgentertainment.common.security.dal.po.SysMenuDO;
import co.mgentertainment.common.security.dal.po.SysRoleDO;
import co.mgentertainment.common.security.dal.po.SysUserDO;
import co.mgentertainment.common.utils.cache.CacheStore;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author larry
 * @createTime 18/08/2023
 * @description SysUserRepository
 */
public class SysUserRepository implements InitializingBean {

    private static final String SQL_QUERY_USER = "select user_id, username, password, phone, avatar, lock_flag, email, sex, login_ip, google_key, nickname from sys_user where username=? and del_flag='0'";
    private static final String SQL_QUERY_ROLE = "select r.role_id, r.role_name, r.role_code, r.role_desc from (select role_id from sys_user_role where user_id = ?) ur left join sys_role r on ur.role_id = r.role_id where r.del_flag = '0'";
    private static final String SQL_QUERY_PERMISSION = "select m.menu_id, m.parent_id, m.name, m.path, m.permission, m.icon, m.sort_order, m.keep_alive, m.type from sys_menu m left join sys_role_menu rm on m.menu_id = rm.menu_id where m.del_flag = '0' and rm.role_id in (?)";

    private JdbcTemplate jdbcTemplate;

    private CacheStore localCache;

    private final DataSource securityDataSource;

    public SysUserRepository(@Qualifier("securityDataSource") DataSource securityDataSource) {
        this.securityDataSource = securityDataSource;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.jdbcTemplate = new JdbcTemplate(securityDataSource);
        this.localCache = new CacheStore.Builder().setCapacity(10000).setExpireSec(3600 * 24).build();
    }

    public SysUserInfo getUserInfoByUsername(String username) throws RuntimeException {
        Object obj = localCache.get(username);
        if (obj instanceof SysUserInfo) {
            return (SysUserInfo) obj;
        }
        SysUserInfo sysUserInfo = getUserInfoFromDb(username);
        if (sysUserInfo != null) {
            localCache.put(username, sysUserInfo);
        }
        return sysUserInfo;
    }

    public void cleanUserCache(String username) {
        localCache.remove(username);
    }

    private SysUserInfo getUserInfoFromDb(String username) throws RuntimeException {
        SysUserDO user = jdbcTemplate.queryForObject(SQL_QUERY_USER, new BeanPropertyRowMapper<>(SysUserDO.class), username);
        if (user == null) {
            return null;
        }
        List<String> permissions = null;
        List<SysRoleDO> roles = jdbcTemplate.query(SQL_QUERY_ROLE, new BeanPropertyRowMapper<>(SysRoleDO.class), user.getUserId());
        if (CollectionUtils.isNotEmpty(roles)) {
            List<Long> roleIds = roles.stream().map(SysRoleDO::getRoleId).collect(Collectors.toList());
            List<SysMenuDO> sysMenus = jdbcTemplate.query(SQL_QUERY_PERMISSION, new BeanPropertyRowMapper<>(SysMenuDO.class), StringUtils.join(roleIds, ","));
            permissions = sysMenus.stream().map(SysMenuDO::getPermission).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
        }
        return SysUserInfo.builder().sysUser(user).roles(roles).permissions(permissions).build();
    }
}
