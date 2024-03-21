package co.saiyan.common.doc.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collections;

/**
 * swagger API文档相关配置
 * Created by larry on 2022/3/4.
 */
@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = true)
public class SwaggerAutoConfiguration {

    @Bean
    public Docket createRestApi(SwaggerProperties swaggerProperties) {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo(swaggerProperties.getTitle()))
                // 是否开启
                .enable(swaggerProperties.getEnabled())
                .securitySchemes(Collections.singletonList(new ApiKey("Authorization", "Authorization", "header")))
                .securityContexts(Collections.singletonList(
                        SecurityContext.builder()
                                .securityReferences(Collections.singletonList(
                                        SecurityReference.builder()
                                                .scopes(new AuthorizationScope[]{new AuthorizationScope("global", "accessEverything")})
                                                .reference("Authorization")
                                                .build()))
                                // 声明作用域
                                .operationSelector(o -> o.requestMappingPattern().matches("/.*"))
                                .build()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                //扫描包路径
                .paths(PathSelectors.any())
                .build()
                .groupName(swaggerProperties.getAppname())
                .pathMapping("/");
    }

    private ApiInfo apiInfo(String title) {
        return new ApiInfoBuilder()
                .title(title)
                .version("1.0")
                .description("API接口说明")
                .extensions(new ArrayList<>())
                .build();
    }
}
