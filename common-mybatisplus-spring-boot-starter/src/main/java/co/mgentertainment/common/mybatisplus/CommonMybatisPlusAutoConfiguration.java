package co.mgentertainment.common.mybatisplus;

import co.mgentertainment.common.mybatisplus.handler.MybatisPlusMetaObjectHandler;
import co.mgentertainment.common.mybatisplus.plugins.PaginationInnerInterceptor;
import co.mgentertainment.common.mybatisplus.resolver.SqlFilterArgumentResolver;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author lengleng
 * @date 2020-03-14
 * <p>
 * mybatis plus 统一配置
 */
@Configuration
public class CommonMybatisPlusAutoConfiguration implements WebMvcConfigurer {

    /**
     * SQL 过滤器避免SQL 注入
     *
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SqlFilterArgumentResolver());
    }

    /**
     * 分页插件, 对于单一数据库类型来说,都建议配置该值,避免每次分页都去抓取数据库类型
     */

    /**
     * 审计字段自动填充
     *
     * @return {@link MetaObjectHandler}
     */
    @Bean
    public MybatisPlusMetaObjectHandler mybatisPlusMetaObjectHandler() {
        return new MybatisPlusMetaObjectHandler();
    }

    @Bean("druidDataSource")
    @ConfigurationProperties("spring.datasource")
    public DruidDataSource defaultDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean("mybatisSqlSessionFactoryBean")
    @DependsOn("mybatisPlusMetaObjectHandler")
    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean(@Qualifier("druidDataSource") DruidDataSource dataSource, MybatisPlusMetaObjectHandler mybatisPlusMetaObjectHandler) throws Exception {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setMetaObjectHandler(mybatisPlusMetaObjectHandler);

        MybatisConfiguration configuration = new MybatisConfiguration();
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        configuration.addInterceptor(interceptor);

        MybatisSqlSessionFactoryBean mybatisPlus = new MybatisSqlSessionFactoryBean();
        mybatisPlus.setGlobalConfig(globalConfig);
        mybatisPlus.setConfiguration(configuration);
        mybatisPlus.setDataSource(dataSource);
        //数据源的mapper.xml所在位置(具体到xml文件)
        mybatisPlus.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(
                "classpath*:**/mapper/**/*Mapper*.xml"));
        mybatisPlus.setDefaultEnumTypeHandler(org.apache.ibatis.type.EnumOrdinalTypeHandler.class);
        return mybatisPlus;
    }

    @Bean("defaultSqlSessionTemplate")
    public SqlSessionTemplate db1SqlSessionTemplate(@Qualifier("mybatisSqlSessionFactoryBean") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 创建事务管理器
     *
     * @param dataSource
     * @return
     */
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("druidDataSource") DruidDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
