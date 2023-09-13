package co.mgentertainment.common.security.service;

import co.mgentertainment.common.model.R;
import co.mgentertainment.common.security.config.SecureProperties;
import co.mgentertainment.common.security.model.AuthenticationRequest;
import co.mgentertainment.common.security.model.AuthenticationResponse;
import co.mgentertainment.common.utils.SecurityHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * @author larry
 * @createTime 20/08/2023
 * @description AuthenticationService
 */
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final SysUserDetailsService sysUserDetailsService;
    private final SecureProperties secureProperties;

    public R<AuthenticationResponse> authenticate(AuthenticationRequest request) {

        UserDetails userDetails = sysUserDetailsService.loadUserByUsername(request.getUsername());

        if (userDetails == null || !passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            return R.failed("用户名或密码不正确！");
        }

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = (User) authenticate.getPrincipal();
        String username = user.getUsername();
        String jwtToken = jwtTokenService.generateToken(username);
        String refreshToken = jwtTokenService.generateRefreshToken(username);
        AuthenticationResponse response = AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .publicKey(secureProperties.getPublicKey())
                .build();
        return R.ok(response, "authenticated successfully");
    }

    /**
     * 刷新token的接口,须指定请求头--> Sign: Bearer refreshTokenEncrypt
     * refreshTokenEncrypt格式：refreshToken;当前时间戳+9000以内随机数字-->整体先Base64加密，再结合secretKey做一次DES加密
     *
     * @param request
     * @return
     */
    public R<AuthenticationResponse> refreshToken(HttpServletRequest request) {
        final String lastTokenEncode = jwtTokenService.getRefreshTokenFromRequest(request);
        String lastToken = SecurityHelper.rsaDecrypt(lastTokenEncode, secureProperties.getPrivateKey(), 10);
        if (lastToken == null) {
            return R.failed("invalid or expired token");
        }
        final String username = jwtTokenService.extractUsername(lastToken);
        if (username != null) {
            User user = (User) sysUserDetailsService.loadUserByUsername(username);
            String subject = user.getUsername();
            if (jwtTokenService.verifyToken(JwtTokenService.TokenType.REFRESH, lastToken, subject)) {
                String accessToken = jwtTokenService.generateToken(subject);
                log.info("accessToken: {}", accessToken);
                return R.ok(AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(lastToken).build(), "refreshed token successfully");
            }
            return R.failed("fail to verify user token");
        }
        return R.failed("fail to extract username from token");
    }

}
