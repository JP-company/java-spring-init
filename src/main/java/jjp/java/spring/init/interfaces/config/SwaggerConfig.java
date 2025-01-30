package jjp.java.spring.init.interfaces.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openApi() {
    return new OpenAPI()
        .components(new Components())
        .info(this.apiInfo())
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .components(this.components());
  }

  private Info apiInfo() {
    return new Info()
        .title("jjp spring init API Docs")
        .description("test")
        .version("1.0.0");
  }

  private Components components() {
    return new Components().addSecuritySchemes(
        "bearerAuth",
        new SecurityScheme()
            .name("bearerAuth")
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
    );
  }
}
