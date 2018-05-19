package it.polito.ai.springserver.resource.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
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

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
            .requestMatchers()
            .antMatchers("/**").and()
            .authorizeRequests()
            .antMatchers("/positions/**").authenticated();
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer config) {
    config.tokenServices((ResourceServerTokenServices) tokenServices).tokenStore(tokenStore);
  }

  @Bean
  public UserId userId(){
    return new UserId();
  }
}
