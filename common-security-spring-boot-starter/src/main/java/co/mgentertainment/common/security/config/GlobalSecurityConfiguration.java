package co.mgentertainment.common.security.config;

import co.mgentertainment.common.security.exception.DefaultAccessDeniedHandler;
import co.mgentertainment.common.security.exception.DefaultAuthenticationEntryPoint;
import co.mgentertainment.common.security.filter.JwtTokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * @author larry
 * @createTime 17/08/2023
 * @description SecurityConfiguration
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(SecureProperties.class)
@RequiredArgsConstructor
public class GlobalSecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;
    private final JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter;
    private final LogoutHandler logoutHandler;
    private final DefaultAccessDeniedHandler defaultAccessDeniedHandler;
    private final DefaultAuthenticationEntryPoint defaultAuthenticationEntryPoint;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(SecureProperties secureProperties) {
        return (web) -> web.ignoring().antMatchers(secureProperties.getIgnoredUrls().toArray(new String[]{})
//                "/swagger-ui.html",
//                "/swagger-ui/",
//                "/*.html",
//                "/favicon.ico",
//                "/**/*.html",
//                "/**/*.css",
//                "/**/*.js",
//                "/swagger-resources/**",
//                "/v3/api-docs/**"
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecureProperties secureProperties) throws Exception {
        return http
                // 禁用CSRF
                .csrf().disable()
                .headers().frameOptions().disable()
                .and().authorizeRequests()
                // 后端接口放行
                .antMatchers(secureProperties.getIgnoredPaths().toArray(new String[]{})
                ).permitAll()
                // 其他请求需验证
                .anyRequest().authenticated()
                // 不需要session（不创建会话）
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // userDetails校验
                .and().authenticationProvider(authenticationProvider)
                // 添加jwt登录认证过滤器
                .addFilterBefore(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // 将登录框关闭
                .formLogin().disable()
//                // 配置登录
//                .formLogin().loginPage("/login.html").loginProcessingUrl("/auth/login").successForwardUrl("/index").failureUrl("/auth/login").and()
                // 配置登出
//                .logout().logoutUrl("/auth/logout").addLogoutHandler(logoutHandler).logoutSuccessUrl("/auth/login")//.logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()).and()
                .exceptionHandling()
                // Token认证异常捕获
                .authenticationEntryPoint(defaultAuthenticationEntryPoint)
                // 授权异常捕获
                .accessDeniedHandler(defaultAccessDeniedHandler)
                .and()
                .build();
    }

}
