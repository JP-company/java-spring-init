package jjp.java.spring.init.interfaces.config;

import java.util.List;
import jjp.java.spring.init.interfaces.interceptor.JwtAuthInterceptor;
import jjp.java.spring.init.interfaces.interceptor.ServerTimeInterceptor;
import jjp.java.spring.init.interfaces.interceptor.resolver.AccountArgumentResolver;
import jjp.java.spring.init.interfaces.interceptor.resolver.ServerTimeArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final JwtAuthInterceptor jwtAuthInterceptor;
  private final ServerTimeInterceptor serverTimeInterceptor;
  private final AccountArgumentResolver accountArgumentResolver;
  private final ServerTimeArgumentResolver serverTimeArgumentResolver;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(this.jwtAuthInterceptor)
        .addPathPatterns("/api/**");

    registry.addInterceptor(this.serverTimeInterceptor)
        .addPathPatterns("/api/**");
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(this.accountArgumentResolver);
    resolvers.add(this.serverTimeArgumentResolver);
  }
}
