package it.polito.ai.springserver.swaggerDoc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig{

  @Bean
  public Docket appInternetApi() {
    ApiInfo apiInfo = new ApiInfo(
            "App Internet resource API",
            "Description of application resource API.",
            "1.0.0",
            "",
            "",
            "License of group1_applicazioni_internet_2018",
            "");
    return new Docket(DocumentationType.SWAGGER_2)
            .groupName("resource")
            .select()
            .apis(RequestHandlerSelectors.basePackage(
                    "it.polito.ai.springserver.resource"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo)
            .useDefaultResponseMessages(false);
  }

  @Bean
  public Docket appInternetRegistrationApi() {
    ApiInfo apiInfo = new ApiInfo(
            "App Internet registration API",
            "Description of application registration API.",
            "1.0.0",
            "",
            "",
            "License of group1_applicazioni_internet_2018",
            "");
    return new Docket(DocumentationType.SWAGGER_2)
            .groupName("oauth-registration")
            .select()
            .apis(RequestHandlerSelectors.basePackage(
                    "it.polito.ai.springserver.authorization"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo)
            .useDefaultResponseMessages(false);
  }

  @Bean
  public Docket appInternetOauthApi() {
    ApiInfo apiInfo = new ApiInfo(
            "App Internet authentication API",
            "Description of application authentication API.",
            "1.0.0",
            "",
            "",
            "License of group1_applicazioni_internet_2018",
            "");
    return new Docket(DocumentationType.SWAGGER_2)
            .groupName("oauth-token")
            .select()
            .apis(RequestHandlerSelectors.basePackage(
                    "org.springframework.security.oauth2"))
            .paths(PathSelectors.regex("/oauth/token"))
            .build()
            .apiInfo(apiInfo)
            .useDefaultResponseMessages(false);
  }

}
