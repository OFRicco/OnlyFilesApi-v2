package de.onlyfiles.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import de.onlyfiles.security.KeepUserUpdatedInterceptor;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private KeepUserUpdatedInterceptor keepUserUpdatedInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(keepUserUpdatedInterceptor);
      
    }
}
