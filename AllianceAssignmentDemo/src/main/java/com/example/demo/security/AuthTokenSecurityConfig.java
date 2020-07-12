package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application.properties")
public class AuthTokenSecurityConfig extends WebSecurityConfigurerAdapter {
 
	@Autowired
    private UserRepository userRepository;
	
	//get authenticate header from property file
    @Value("${http.auth.tokenName}")
    private String authHeaderName;
    
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception 
    {
        PreAuthTokenHeaderFilter filter = new PreAuthTokenHeaderFilter(authHeaderName);
         
        filter.setAuthenticationManager(new AuthenticationManager() 
        {
            @Override
            public Authentication authenticate(Authentication authentication) 
                                                throws AuthenticationException 
            {
                String principal = (String) authentication.getPrincipal();
                
                //check if we can find this token in our user database
                User user = userRepository.findByToken(principal);
                                
                //grant access to user 
                //TODO: remove check for superToken when deploy to production 
                if (user != null)
                	authentication.setAuthenticated(true);
                
                return authentication;
            }
        });
         
        //any request to our REST API(/api/**) will need authenticate
        httpSecurity
        	.antMatcher("/api/**")
            .csrf()
                .disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .addFilter(filter)
                .authorizeRequests()
                	.anyRequest()
                    .authenticated();
    }
 
}