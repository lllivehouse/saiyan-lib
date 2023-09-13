package co.mgentertainment.common.security.service;

import co.mgentertainment.common.security.dal.po.SysRoleDO;
import co.mgentertainment.common.security.dal.po.SysUserDO;
import co.mgentertainment.common.security.dal.repository.SysUserInfo;
import co.mgentertainment.common.security.dal.repository.SysUserRepository;
import co.mgentertainment.common.security.model.SysUserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author larry
 * @createTime 17/08/2023
 * @description SysUserDetailsService
 */
@RequiredArgsConstructor
public class SysUserDetailsService implements UserDetailsService {

    private final SysUserRepository sysUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isBlank(username)) {
            throw new UsernameNotFoundException("invalid username");
        }
        SysUserInfo userInfo;
        try {
            userInfo = sysUserRepository.getUserInfoByUsername(username);
        } catch (Throwable e) {
            throw new UsernameNotFoundException("not found username:" + username);
        }
        SysUserDO user = userInfo.getSysUser();
        List<Long> roleIds = userInfo.getRoles().stream().map(SysRoleDO::getRoleId).distinct().collect(Collectors.toList());
        List<String> permissions = userInfo.getPermissions();
        Collection<? extends GrantedAuthority> authorities = SysUserDetails.toAuthorities(permissions);
        return new SysUserDetails(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getNickname(),
                user.getPhone(),
                user.getLoginIp(),
                user.getGoogleKey(),
                user.getAvatar(),
                roleIds,
                permissions,
                true,
                true,
                true,
                "0".equals(user.getLockFlag()),
                authorities);
    }

    public void refreshUserCache(String username) {
        sysUserRepository.cleanUserCache(username);
    }
}
