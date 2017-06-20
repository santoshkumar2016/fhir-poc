package com.nalashaa.fhir.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.nalashaa.fhir.config.spring.db.HibernateConfiguration;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.nalashaa.fhir")
@Import(value = HibernateConfiguration.class)
//@ComponentScan(basePackages = "com.nalashaa.fhir.config.spring,"
//		+ "com.nalashaa.fhir.config.spring.db,"
//		+ "com.nalashaa.fhir.config.spring.security,"
//		+ "com.nalashaa.fhir.config.spring.security.oauth2,"
//		+ "com.nalashaa.fhir.dao,com.nalashaa.fhir.dao.impl,"
//		+ "com.nalashaa.fhir.db.model,com.nalashaa.fhir.provider,"
//		+ "com.nalashaa.fhir.service,com.nalashaa.fhir.service.impl,"
//		+ "com.nalashaa.fhir.servlet")
public class AppSpringConfig {
	
	// configure this bean for customizing error messages of spring
	@Bean
	public ResourceBundleMessageSource messageSource(){
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasenames("customMessages");
		return source;
	}

}
