package com.cantatahealth.fhir.core;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
@EnableWebSecurity
public class OAuth2SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private ClientDetailsService clientDetailsService;
	
	@Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
        .withUser("test1").password("test123").roles("ADMIN").and()
        .withUser("test2").password("test123").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	http
    		//.csrf().disable()
    		.authorizeRequests()
    		//.antMatchers("/oauth/authorize").permitAll()//hasRole("USER")
    		//.antMatchers("/oauth/token").permitAll()//hasRole("USER")
			.and()
//			// Possibly more configuration ...
			.formLogin() // enable form based log in
//			// set permitAll for all URLs associated with Form Login
			.permitAll();
    	/**
		http
		//.addFilterBefore(new FHIRRedirectingFilter(), OncePerRequestFilter.class )
		.formLogin().disable()
//		.csrf().disable()
//		.anonymous().disable()
	  	.authorizeRequests()
	  	.antMatchers("/oauth/authorize").hasRole("USER")//permitAll()
	  	.antMatchers("/oauth/token").permitAll();
	  	**/
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


   
	@Bean
	@Autowired
	public TokenStore tokenStore(DataSource securityDataSource) {
		//return new InMemoryTokenStore();
		
		return new JdbcTokenStore(securityDataSource);
	}

	@Bean
	@Autowired
	public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore){
		TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
		handler.setTokenStore(tokenStore);
		handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
		handler.setClientDetailsService(clientDetailsService);
		return handler;
	}
	
	@Bean
	@Autowired
	public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception {
		TokenApprovalStore store = new TokenApprovalStore();
		store.setTokenStore(tokenStore);
		return store;
	}
	
	@Bean
	@Autowired
	public AuthorizationCodeServices authorizationCodeServicesBean(DataSource securityDataSource) throws Exception {
		AuthorizationCodeServices authCodeService = new JdbcAuthorizationCodeServices(securityDataSource);
		//authCodeService.consumeAuthorizationCode("code");
		return authCodeService;
	}
	
}
