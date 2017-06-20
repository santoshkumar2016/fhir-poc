package com.nalashaa.fhir.config.spring.security.oauth2;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
//@EnableResourceServer
@Order(2)
public class ResourceServerConfigurationImmun extends ResourceServerConfigurerAdapter {

	private static final String RESOURCE_ID = "resource_immunization";
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(RESOURCE_ID).stateless(false);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		//http.antMatcher("/fhir/Immunization/**").authorizeRequests().anyRequest().access("hasRole('USER')");//access("hasRole('ADMIN')");
		http.
		anonymous().disable()
		.requestMatchers().antMatchers("/fhir/Immunization/**")
		.and().authorizeRequests()
		.antMatchers("/fhir/Immunization/**").access("hasRole('USER')")
		.and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
	}

}