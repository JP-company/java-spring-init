package jjp.java.spring.init.interfaces.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openApi() {
    return new OpenAPI()
        .components(new Components())
        .info(this.apiInfo());
  }

  private Info apiInfo() {
    return new Info()
        .title("jjp spring init API Docs")
        .description("test")
        .version("1.0.0");
  }

}
