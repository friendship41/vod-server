package com.friendship41.vodserver.config;

import com.friendship41.vodserver.config.log.ReqResLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Autowired
  private ReqResLoggingInterceptor reqResLoggingInterceptor;

  @Override
  public void addInterceptors(final InterceptorRegistry registry) {
    registry
        .addInterceptor(this.reqResLoggingInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns("/error");
  }
}
