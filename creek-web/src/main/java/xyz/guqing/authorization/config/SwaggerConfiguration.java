package xyz.guqing.authorization.config;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.*;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import xyz.guqing.authorization.properties.MySecurityAutoConfiguration;
import xyz.guqing.authorization.properties.SystemProperties;

import java.lang.reflect.Type;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

@EnableSwagger2
@Configuration
public class SwaggerConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SwaggerConfiguration.class);
    @Autowired
    private MySecurityAutoConfiguration configuration;

    private SystemProperties systemProperties;

    private final List<ResponseMessage> globalResponses = Arrays.asList(
            new ResponseMessageBuilder().code(200).message("Success").build(),
            new ResponseMessageBuilder().code(400).message("Bad request").build(),
            new ResponseMessageBuilder().code(401).message("Unauthorized").build(),
            new ResponseMessageBuilder().code(403).message("Forbidden").build(),
            new ResponseMessageBuilder().code(404).message("Not found").build(),
            new ResponseMessageBuilder().code(500).message("Internal server error").build());

    public SwaggerConfiguration(MySecurityAutoConfiguration configuration) {
        this.systemProperties = configuration.getSystemProperties();
    }

    @Bean
    public Docket controllerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("Spring Security权限管理系统接口文档")
                        .description("描述内容")
                        .version("1.0.0")
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("xyz.guqing.authorization.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
