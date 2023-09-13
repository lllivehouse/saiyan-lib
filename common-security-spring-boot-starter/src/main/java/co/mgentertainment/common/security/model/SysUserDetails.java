package co.mgentertainment.common.security.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author larry
 * @createTime 17/08/2023
 * @description SysUserDetails
 */
@Getter
@Setter
public class SysUserDetails extends User {
    public static final String ROLE = "ROLE_";

    private static final long serialVersionUID = 9109143368437074458L;

    private Long userId;
    private String nickname;
    private String phone;
    private String loginIp;
    private String googleKey;
    private String avatar;
    private List<Long> roleIds;
    private List<String> permissions;

    public SysUserDetails(Long userId, String username, String password, String nickname, String phone, String loginIp, String googleKey, String avatar, List<Long> roleIds, List<String> permission,
                          boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
                          Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
        this.nickname = nickname;
        this.phone = phone;
        this.loginIp = loginIp;
        this.googleKey = googleKey;
        this.avatar = avatar;
        this.permissions = permission;
        this.roleIds = roleIds;
    }

    public static Collection<? extends GrantedAuthority> toAuthorities(List<String> permissions) {
        return Optional.ofNullable(permissions).orElse(Collections.emptyList()).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
