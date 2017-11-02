package com.cantatahealth.fhir.core;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.cantatahealth.fhir.util.ResourcesLiteralsUtil;

@Configuration
public class CompositeResourceServerConfiguration {

	/**
	@Bean
	protected ResourceServerConfiguration patientResources() {

		ResourceServerConfiguration resource = new ResourceServerConfiguration() {
			// override default configurers for this resource server
			public void setConfigurers(List<ResourceServerConfigurer> configurers) {
				super.setConfigurers(configurers);
			}
		};

		resource.setConfigurers(Arrays.<ResourceServerConfigurer> asList(new ResourceServerConfigurerAdapter() {

			@Override
			public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
				//OAuth2Authentication oauthAuth = remTokenServices.loadAuthentication("accessToken");
				//oauthAuth.getOAuth2Request().getClientId();
				
				RemoteTokenServices remTokenServices = new RemoteTokenServices();
				remTokenServices.setClientId("test");
				remTokenServices.setClientSecret("test");
				remTokenServices.setCheckTokenEndpointUrl("http://localhost:9999/cantatahealth-auth/oauth/check_token");
				resources.tokenServices(remTokenServices);
				resources.resourceId(ResourcesLiteralsUtil.Resource_Patient);
			}

			@Override
			public void configure(HttpSecurity http) throws Exception {
				// http.requestMatcher(new
				// AntPathRequestMatcher("/fhir/Patient/**","GET")
				// ).requestMatchers().;
				// http.antMatcher("/fhir/Patient/**").authorizeRequests().anyRequest().access("hasRole('ADMIN')");
				// access("#oauth2.hasScope('read')");
				http
						// .formLogin().and()
						.antMatcher("/baseDstu3/Patient/**").authorizeRequests()
						.requestMatchers(new AntPathRequestMatcher("/baseDstu3/Patient/**", "GET"))
						.access("#oauth2.hasScope('Read')")
						.requestMatchers(new AntPathRequestMatcher("/baseDstu3/Patient", "POST"))
						.access("#oauth2.hasScope('Write')")
						.requestMatchers(new AntPathRequestMatcher("/baseDstu3/Patient/**", "PUT"))
						.access("#oauth2.hasScope('Update')")
						.requestMatchers(new AntPathRequestMatcher("/baseDstu3/Patient/**", "DELETE"))
						.access("#oauth2.hasScope('Delete')").and().formLogin().permitAll();

				// .antMatchers("/login").permitAll().anyRequest().hasAnyRole("ANONYMOUS,
				// USER")
				// .antMatchers(HttpMethod.GET,
				// "/fhir/Patient/**").access("#oauth2.hasScope('read')")
				// .antMatchers(HttpMethod.POST,
				// "/fhir/Patient").access("#oauth2.hasScope('write')")
				// .antMatchers(HttpMethod.PUT,
				// "/fhir/Patient/**").access("#oauth2.hasScope('write')")
				// .antMatchers(HttpMethod.DELETE,
				// "/fhir/Patient/**").access("#oauth2.hasScope('delete')")
				// .and().formLogin().permitAll();
			}

		}));
		resource.setOrder(3);

		return resource;

	}

	@Bean
	protected ResourceServerConfiguration immunizationResources() {

		ResourceServerConfiguration resource = new ResourceServerConfiguration() {
			// override default configurers for this resource server
			public void setConfigurers(List<ResourceServerConfigurer> configurers) {
				super.setConfigurers(configurers);
			}
		};

		resource.setConfigurers(Arrays.<ResourceServerConfigurer> asList(new ResourceServerConfigurerAdapter() {

			@Override
			public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
				resources.resourceId(ResourcesLiteralsUtil.Resource_Immunization);
			}

			@Override
			public void configure(HttpSecurity http) throws Exception {
				// http.antMatcher("/fhir/Immunization/**").authorizeRequests().anyRequest().access("hasRole('USER')");
				http.antMatcher("/baseDstu3/Immunization/**").authorizeRequests()
						.requestMatchers(new AntPathRequestMatcher("/baseDstu3/Immunization/**", "GET"))
						.access("#oauth2.hasScope('Read')")
						.requestMatchers(new AntPathRequestMatcher("/baseDstu3/Immunization", "POST"))
						.access("#oauth2.hasScope('Write')")
						.requestMatchers(new AntPathRequestMatcher("/baseDstu3/Immunization/**", "PUT"))
						.access("#oauth2.hasScope('Update')")
						.requestMatchers(new AntPathRequestMatcher("/baseDstu3/Immunization/**", "DELETE"))
						.access("#oauth2.hasScope('Delete')");
			}
		}));
		resource.setOrder(4);

		return resource;

	}
	**/
	@Bean
	protected ResourceServerConfiguration otherResources() {

		ResourceServerConfiguration resource = new ResourceServerConfiguration() {
			// override default configurers for this resource server
			public void setConfigurers(List<ResourceServerConfigurer> configurers) {
				super.setConfigurers(configurers);
			}
		};

		resource.setConfigurers(Arrays.<ResourceServerConfigurer> asList(new ResourceServerConfigurerAdapter() {

			@Override
			public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
				resources.resourceId(ResourcesLiteralsUtil.Resource_Others);
			}

			@Override
			public void configure(HttpSecurity http) throws Exception {
				// http.antMatcher("/fhir/Immunization/**").authorizeRequests().anyRequest().access("hasRole('USER')");
				http.antMatcher("/baseDstu3/**").authorizeRequests().anyRequest().permitAll();

			}
		}));
		resource.setOrder(5);

		return resource;

	}
}
