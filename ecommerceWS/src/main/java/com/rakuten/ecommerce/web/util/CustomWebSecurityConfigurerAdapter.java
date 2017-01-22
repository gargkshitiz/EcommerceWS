package com.rakuten.ecommerce.web.util;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
 
@Configuration
@EnableWebSecurity

public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
 
    private static final String GENERAL_USER_PWD = "rakuten123";
	private static final String GENERAL_USER = "kshitiz";
	private static final String ADMIN_PWD = "admin";
	private static final String ADMIN_USER = "admin";
	private static final String USER = "USER";
	private static final String ADMIN = "ADMIN";
	public static final String ECOMMERCE_REALM="ECOMMERCE_REALM";
     
    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        // Authorization: Basic a3NoaXRpejpyYWt1dGVuMTIz
    	auth.inMemoryAuthentication().withUser(ADMIN_USER).password(ADMIN_PWD).roles(ADMIN);
    	// Authorization: Basic dW5hdXRob3JpemVkOjEyM3Jha3V0ZW4=
        auth.inMemoryAuthentication().withUser(GENERAL_USER).password(GENERAL_USER_PWD).roles(USER);
    }
     
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable()
        .authorizeRequests()
        .antMatchers("/**").access("hasRole('USER') or hasRole('ADMIN')")
        .and().httpBasic().realmName(ECOMMERCE_REALM).authenticationEntryPoint(getBasicAuthEntryPoint())
        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
     
    @Bean
    public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint(){
        return new CustomBasicAuthenticationEntryPoint();
    }
     
    /* To allow Pre-flight [OPTIONS] request from browser */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
 
}