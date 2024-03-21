package co.saiyan.common.security.config;

import co.saiyan.common.security.dal.repository.SysUserRepository;
import co.saiyan.common.security.exception.DefaultAccessDeniedHandler;
import co.saiyan.common.security.exception.DefaultAuthenticationEntryPoint;
import co.saiyan.common.security.filter.JwtTokenAuthenticationFilter;
import co.saiyan.common.security.service.AuthenticationService;
import co.saiyan.common.security.service.JwtTokenService;
import co.saiyan.common.security.service.SysUserDetailsService;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.sql.DataSource;

/**
 * @author larry
 * @createTime 17/08/2023
 * @description SecurityConfiguration
 */
@Configuration
public class SecurityProviderConfiguration {

    @Bean(name = "securityDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource securityDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    public SysUserRepository sysUserRepository(@Qualifier("securityDataSource") DataSource securityDataSource) {
        return new SysUserRepository(securityDataSource);
    }

    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SysUserDetailsService sysUserDetailsService(SysUserRepository sysUserRepository) {
        return new SysUserDetailsService(sysUserRepository);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(SysUserDetailsService sysUserDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(sysUserDetailsService);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authProvider;
    }

    @Bean
    public JwtTokenService jwtTokenService(SecureProperties secureProperties) {
        return new JwtTokenService(secureProperties);
    }

    @Bean
    public JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter(SysUserDetailsService sysUserDetailsService, JwtTokenService jwtTokenService) {
        return new JwtTokenAuthenticationFilter(sysUserDetailsService, jwtTokenService);
    }

    @Bean
    public DefaultAccessDeniedHandler defaultAccessDeniedHandler() {
        return new DefaultAccessDeniedHandler();
    }

    @Bean
    public DefaultAuthenticationEntryPoint defaultAuthenticationEntryPoint() {
        return new DefaultAuthenticationEntryPoint();
    }

    @Bean
    public LogoutHandler logoutHandler() {
        return new SecurityContextLogoutHandler();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationService authenticationService(PasswordEncoder passwordEncoder,
                                                       JwtTokenService jwtTokenService,
                                                       AuthenticationManager authenticationManager,
                                                       SysUserDetailsService sysUserDetailsService,
                                                       SecureProperties secureProperties) {
        return new AuthenticationService(passwordEncoder, jwtTokenService, authenticationManager, sysUserDetailsService, secureProperties);
    }

}
