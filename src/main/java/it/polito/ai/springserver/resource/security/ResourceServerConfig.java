package it.polito.ai.springserver.resource.security;

import it.polito.ai.springserver.resource.model.CustomerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableAsync
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@PropertySource("classpath:authorization/security.properties")
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

  @Autowired
  private TokenStore tokenStore;

  @Autowired
  private AuthorizationServerTokenServices tokenServices;

  @Value("${spring.key}")
  private String key;

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public CorsFilter corsFilter() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.applyPermitDefaultValues();
    configuration.setAllowCredentials(true);
    configuration.setAllowedOrigins(Collections.singletonList("*"));
    configuration.setAllowedHeaders(Collections.singletonList("*"));
    configuration.setAllowedMethods(Collections.singletonList("*"));
    configuration.setExposedHeaders(Collections.singletonList("content-length"));
    configuration.setMaxAge(3600L);
    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return new CorsFilter(source);
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .cors().and()
        .authorizeRequests()
        .antMatchers("/oauth/token").permitAll().and()
        .authorizeRequests()
        .anyRequest().authenticated();
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer config) {
    config.tokenServices((ResourceServerTokenServices) tokenServices).tokenStore(tokenStore);
  }

  @Bean
  public UserId userId() {
    return new UserId();
  }
}
