package com.rakuten.ecommerce.web.util;

import static com.google.common.collect.Lists.newArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@Configuration
@EnableSwagger2
/**
 * @author Kshitiz Garg
 */
public class SwaggerConfiguration {

	@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(
           RequestHandlerSelectors.any()).paths(PathSelectors.any()).build()
                .apiInfo(apiInfo()).globalOperationParameters(
                        newArrayList(new ParameterBuilder()
                                .name("Authorization")
                                .description("Its value should be 'Basic Base64Encoded(userName:password)'")
                                .modelRef(new ModelRef("String"))
                                .parameterType("header")
                                .required(true)
                                .build()));
	}
	
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Ecommerce Web Services")
            .description("Documentation and test harness for Ecommerce Web Services")
            .version("1.0")
            .build();
    }
    
}