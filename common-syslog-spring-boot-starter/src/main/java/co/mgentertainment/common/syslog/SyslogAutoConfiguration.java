package co.mgentertainment.common.syslog;

import co.mgentertainment.common.syslog.aspect.SysLogAspect;
import co.mgentertainment.common.syslog.dal.repository.SyslogRepository;
import co.mgentertainment.common.syslog.event.SysLogListener;
import co.mgentertainment.common.syslog.util.SpringContextHolder;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.sql.DataSource;

/**
 * @author larry
 * @date 2019/2/1 日志自动配置
 */
@EnableAsync
@ConditionalOnWebApplication
@Configuration(proxyBeanMethods = false)
public class SyslogAutoConfiguration {

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

    @Bean(name = "syslogDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource syslogDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    public SyslogRepository syslogRepository(@Qualifier("syslogDataSource") DataSource syslogDataSource) {
        return new SyslogRepository(syslogDataSource);
    }

    @Bean
    public SysLogListener sysLogListener(SyslogRepository syslogRepository) {
        return new SysLogListener(syslogRepository);
    }

    @Bean
    public SysLogAspect sysLogAspect() {
        return new SysLogAspect();
    }

}
