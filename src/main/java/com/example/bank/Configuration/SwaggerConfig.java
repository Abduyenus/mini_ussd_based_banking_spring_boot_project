package com.example.bank.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableWebMvc
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/api/.*"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("USSD Based Banking System")
                .description("This is project is made by Abdulwasse Yenus")
                .version("V1.2")
                .termsOfServiceUrl("http://terms-of-services.url")
                .license("LICENSE")
                .licenseUrl("http://url-to-license.com")
                .build();
    }

}
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.web.servlet.config.annotation.EnableWebMvc;
////import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
////import springfox.documentation.builders.ApiInfoBuilder;
////import springfox.documentation.builders.PathSelectors;
////import springfox.documentation.builders.RequestHandlerSelectors;
////import springfox.documentation.service.ApiInfo;
////import springfox.documentation.spi.DocumentationType;
////import springfox.documentation.spring.web.plugins.Docket;
////import springfox.documentation.swagger2.annotations.EnableSwagger2;
////
////@Configuration
////@EnableWebMvc
////public class SwaggerConfiguration implements WebMvcConfigurer {
////
////    @Bean
////    public Docket api(){
////        return new Docket(DocumentationType.SWAGGER_2)
////                .select()
////                .apis(RequestHandlerSelectors.any())
////                .paths(PathSelectors.regex("/api/v1/bank.*"))
////                .build()
////                .apiInfo(apiInfo());
////    }
////
////    private ApiInfo apiInfo() {
////        return new ApiInfoBuilder()
////                .title("My application title")
////                .description("This is a test of documenting EST API's")
////                .version("V1.2")
////                .termsOfServiceUrl("http://terms-of-services.url")
////                .license("LICENSE")
////                .licenseUrl("http://url-to-license.com")
////                .build();
////    }
////
////}a
//package com.example.bank.Configuration;
//
//import java.util.ArrayList;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import io.swagger.models.Contact;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//@Configuration
//@EnableSwagger2
//public class SwaggerConfiguration {
//
////    @Bean
////    public Docket api() {
////        return new Docket(DocumentationType.SWAGGER_2)
////                .select()
////                .apis(RequestHandlerSelectors.basePackage("com.example.bank.Controller")) // Adjust the base package
////                .paths(PathSelectors.any())
////                .build()
////                .apiInfo(apiInfo());
////    }
////  @Bean
////  public Docket api(){
////      return new Docket(DocumentationType.SWAGGER_2)
////              .select()
////              .apis(RequestHandlerSelectors.any())
////              .paths(PathSelectors.regex("/api/v1/bank.*"))
////              .build()
////              .apiInfo(apiInfo());
////  }
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("Bank API Documentation")
//                .description("API documentation for the Bank system")
//                .version("V1.0")
//                .termsOfServiceUrl("http://terms-of-services.url")
//                .license("Apache License Version 2.0")
//                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
//                .build();
//    }
//	
//
////	@Bean
////	public Docket api() {
////		
////		return new Docket(DocumentationType.SWAGGER_2).apiInfo(null).select().apis(RequestHandlerSelectors.any())
////				.paths(PathSelectors.any()).build();
////	}
//	@Bean
//	public Docket api() {
//	    return new Docket(DocumentationType.SWAGGER_2)
//	            .select()
//	            .apis(RequestHandlerSelectors.basePackage("com.example.bank.Controller"))
//	            .paths(PathSelectors.any())
//	            .build()
//	            .apiInfo(apiInfo());
//	}
//
//}
