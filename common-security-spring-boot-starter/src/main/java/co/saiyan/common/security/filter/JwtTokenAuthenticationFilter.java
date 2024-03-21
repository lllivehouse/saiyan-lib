package co.saiyan.common.security.filter;

import co.saiyan.common.security.service.JwtTokenService;
import co.saiyan.common.security.service.SysUserDetailsService;
import io.jsonwebtoken.lang.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author larry
 * @createTime 17/08/2023
 * @description JwtTokenAuthenticationFilter
 */
@RequiredArgsConstructor
@Slf4j
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final SysUserDetailsService sysUserDetailsService;

    private final JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 从 HTTP 请求中获取 token
        String token = jwtTokenService.getAccessTokenFromRequest(request);
        if (Strings.hasText(token)) {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                String username = jwtTokenService.extractUsername(token);
                if (username != null) {
                    log.info("checking username:{}", username);
                    UserDetails userDetails = sysUserDetailsService.loadUserByUsername(username);
                    String subject = userDetails.getUsername();
                    // 验证accessToken是否有效
                    if (jwtTokenService.verifyToken(JwtTokenService.TokenType.ACCESS, token, subject)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        log.info("authenticated user:" + username);
                        // 将认证信息存入 Spring 安全上下文中
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }
        // 登录无token时放行请求
        filterChain.doFilter(request, response);
    }

}