package com.cantatahealth.fhir.core;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.cantatahealth.fhir.provider.AllergyIntoleranceResourceProvider;
import com.cantatahealth.fhir.provider.CCDAResourceProvider;
import com.cantatahealth.fhir.provider.CareTeamResourceProvider;
import com.cantatahealth.fhir.provider.CompositionResourceProvider;
import com.cantatahealth.fhir.provider.ConditionResourceProvider;
import com.cantatahealth.fhir.provider.DeviceResourceProvider;
import com.cantatahealth.fhir.provider.DiagnosticReportResourceProvider;
import com.cantatahealth.fhir.provider.EncounterResourceProvider;
import com.cantatahealth.fhir.provider.GoalResourceProvider;
import com.cantatahealth.fhir.provider.ImmunizationResourceProvider;
import com.cantatahealth.fhir.provider.LocationResourceProvider;
import com.cantatahealth.fhir.provider.MedicationAdministrationResourceProvider;
import com.cantatahealth.fhir.provider.MedicationRequestResourceProvider;
import com.cantatahealth.fhir.provider.MedicationStatementResourceProvider;
import com.cantatahealth.fhir.provider.ObservationResourceProvider;
import com.cantatahealth.fhir.provider.PatientResourceProvider;
import com.cantatahealth.fhir.provider.ProcedureResourceProvider;

import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;

/**
 * This is the primary configuration file for the example server
 */
@Configuration
@ComponentScan(basePackages = "com.cantatahealth.fhir")
@EnableTransactionManagement()
@PropertySource(value = { "classpath:application.properties" })
public class FhirServerConfig {
	Logger logger = LoggerFactory.getLogger(FhirServerConfig.class);

	@Autowired
	private Environment environment;
	
	/**
	 * Configure patient resource provider
	 * 
	 * @return
	 */
	@Bean
	public PatientResourceProvider patientResourceProvider() {
		PatientResourceProvider patientResourceProvider = new PatientResourceProvider();
		return patientResourceProvider;
	}

	/**
	 * Configure MedicationAdministration resource provider
	 * 
	 * @return
	 */
	@Bean
	public MedicationAdministrationResourceProvider medicationAdministrationResourceProvider() {
		MedicationAdministrationResourceProvider medicationAdministrationResourceProvider = new MedicationAdministrationResourceProvider();
		return medicationAdministrationResourceProvider;
	}
	
	/**
	 * Configure AllergyIntolerance resource provider
	 * 
	 * @return
	 */
	@Bean
	public AllergyIntoleranceResourceProvider allergyIntoleranceResourceProvider() {
		AllergyIntoleranceResourceProvider allergyIntoleranceResourceProvider = new AllergyIntoleranceResourceProvider();
		return allergyIntoleranceResourceProvider;
	}
	
	/**
	 * Configure CareTeam resource provider
	 * 
	 * @return
	 */
	@Bean
	public CareTeamResourceProvider careTeamResourceProvider() {
		CareTeamResourceProvider careTeamResourceProvider = new CareTeamResourceProvider();
		return careTeamResourceProvider;
	}
	
	/**
	 * Configure Encounter resource provider
	 * @return
	 */
	@Bean
	public EncounterResourceProvider encounterResourceProvider() {
		EncounterResourceProvider encounterResourceProvider = new EncounterResourceProvider();
		return encounterResourceProvider;
	}
	
	/**
	 * Configure Condition resource provider
	 * 
	 * @return
	 */
	@Bean
	public ConditionResourceProvider conditionResourceProvider() {
		ConditionResourceProvider conditionResourceProvider = new ConditionResourceProvider();
		return conditionResourceProvider;
	}
	
	/**
	 * Configure Device resource provider
	 * 
	 * @return
	 */
	@Bean
	public DeviceResourceProvider deviceResourceProvider() {
		DeviceResourceProvider deviceResourceProvider = new DeviceResourceProvider();
		return deviceResourceProvider;
	}
	
	/**
	 * Configure DiagnosticReport resource provider
	 * 
	 * @return
	 */
	@Bean
	public DiagnosticReportResourceProvider diagnosticReportResourceProvider() {
		DiagnosticReportResourceProvider diagnosticReportResourceProvider = new DiagnosticReportResourceProvider();
		return diagnosticReportResourceProvider;
	}
	
	/**
	 * Configure Goal resource provider
	 * 
	 * @return
	 */
	@Bean
	public GoalResourceProvider goalResourceProvider() {
		GoalResourceProvider goalResourceProvider = new GoalResourceProvider();
		return goalResourceProvider;
	}
	
	/**
	 * Configure Immunization resource provider
	 * 
	 * @return
	 */
	@Bean
	public ImmunizationResourceProvider immunizationResourceProvider() {
		ImmunizationResourceProvider immunizationResourceProvider = new ImmunizationResourceProvider();
		return immunizationResourceProvider;
	}
	
	/**
	 * Configure Location resource provider
	 * 
	 * @return
	 */
	@Bean
	public LocationResourceProvider locationResourceProvider() {
		LocationResourceProvider locationResourceProvider = new LocationResourceProvider();
		return locationResourceProvider;
	}
	
	/**
	 * Configure MedicationRequest resource provider
	 * 
	 * @return
	 */
	@Bean
	public MedicationRequestResourceProvider medicationRequestResourceProvider() {
		MedicationRequestResourceProvider medicationRequestResourceProvider = new MedicationRequestResourceProvider();
		return medicationRequestResourceProvider;
	}
	
	/**
	 * Configure MedicationStatement resource provider
	 * 
	 * @return
	 */
	@Bean
	public MedicationStatementResourceProvider medicationStatementResourceProvider() {
		MedicationStatementResourceProvider medicationStatementResourceProvider = new MedicationStatementResourceProvider();
		return medicationStatementResourceProvider;
	}
	
	/**
	 * Configure Observation resource provider
	 * 
	 * @return
	 */
	@Bean
	public ObservationResourceProvider observationResourceProvider() {
		ObservationResourceProvider observationResourceProvider = new ObservationResourceProvider();
		return observationResourceProvider;
	}
	
	/**
	 * Configure Procedure resource provider
	 * 
	 * @return
	 */
	@Bean
	public ProcedureResourceProvider procedureResourceProvider() {
		ProcedureResourceProvider procedureResourceProvider = new ProcedureResourceProvider();
		return procedureResourceProvider;
	}

	/**
	 * Configure FHIR properties 
	 */
//	@Bean()
//	public DaoConfig daoConfig() {
//		logger.info("...........Initializing DaoConfig START.........");
//		DaoConfig retVal = new DaoConfig();
//		retVal.setSubscriptionEnabled(true);
//		retVal.setSubscriptionPollDelay(5000);
//		retVal.setSubscriptionPurgeInactiveAfterMillis(DateUtils.MILLIS_PER_HOUR);
//		retVal.setAllowMultipleDelete(true);
//		logger.info("...........Initializing DaoConfig Complete.........");
//		return retVal;
//	}

	/**
	 * The following bean configures the database connection. 
	 * 
	 * A URL to a remote database could also be placed here, along with login
	 * credentials and other properties supported by BasicDataSource.
	 */
	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		logger.info("...........Initializing DataSource Start.........");
		BasicDataSource retVal = new BasicDataSource();

		try {
			
			//retVal.setMinIdle(5);
			retVal.setMaxIdle(30);
			retVal.setMaxTotal(100);
			retVal.setMaxOpenPreparedStatements(100);
			//retVal.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
			
			retVal.setDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
			retVal.setUrl(environment.getRequiredProperty("database.url"));
			retVal.setUsername(environment.getRequiredProperty("database.username"));
			retVal.setPassword(environment.getRequiredProperty("database.password"));
		} catch (Exception e) {
			logger.error("Error occurred initializing data source", e);
		}

		logger.info("...........Initializing DataSource Complete.........");
		return retVal;
	}

	/**
	 * 
	 * @return
	 */
	@Bean()
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		logger.info("...........Initializing LocalContainerEntityManagerFactoryBean Start.........");
		LocalContainerEntityManagerFactoryBean retVal = new LocalContainerEntityManagerFactoryBean();
		retVal.setPersistenceUnitName("CANTATA_FHIR_PU");
		retVal.setDataSource(dataSource());
		retVal.setPackagesToScan("com.cantatahealth.fhir.db.model");
		retVal.setPersistenceProvider(new HibernatePersistenceProvider());
		retVal.setJpaProperties(jpaProperties());
		logger.info("...........Initializing LocalContainerEntityManagerFactoryBean Complete.........");
		return retVal;
	}

	private Properties jpaProperties() {
		logger.info("...........Initializing JPA properties Start.........");
		Properties extraProperties = new Properties();

		extraProperties.put("hibernate.dialect", org.hibernate.dialect.SQLServer2012Dialect.class.getName());
		extraProperties.put("hibernate.format_sql", "true");
		extraProperties.put("hibernate.show_sql", "false");
		extraProperties.put("hibernate.jdbc.batch_size", "20");
		extraProperties.put("hibernate.cache.use_query_cache", "false");
		extraProperties.put("hibernate.cache.use_second_level_cache", "false");

		logger.info("...........Initializing JPA properties Complete.........");
		return extraProperties;
	}

	/**
	 * Do some fancy logging to create a nice access log that has details about
	 * each incoming request.
	 */
	@Bean(autowire = Autowire.BY_TYPE)
	public IServerInterceptor loggingInterceptor() {
		logger.info("...........Initializing Logging interceptor Start.........");
		LoggingInterceptor retVal = new LoggingInterceptor();
		retVal.setLoggerName("ca.uhn");
		retVal.setMessageFormat(
				"Source[${remoteAddr}] Operation[${operationType}] Resource[${idOrResourceName}] Params[${requestParameters}]");

		retVal.setLogExceptions(true);
		retVal.setErrorMessageFormat("ERROR - ${requestVerb} ${requestUrl}");
		logger.info("...........Initializing Logging interceptor Complete.........");
		return retVal;
	}

	@Bean()
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		logger.info("...........Initializing JpaTransactionManager Start.........");
		JpaTransactionManager retVal = new JpaTransactionManager();
		retVal.setEntityManagerFactory(entityManagerFactory);
		logger.info("...........Initializing JpaTransactionManager Complete.........");
		return retVal;
	}


	/**
	 * This interceptor adds some pretty syntax highlighting in responses when a
	 * browser is detected
	 */
	@Bean(autowire = Autowire.BY_TYPE)
	public IServerInterceptor responseHighlighterInterceptor() {
		logger.info("...........Response highlighter interceptor Initialization Start.........");
		ResponseHighlighterInterceptor retVal = new ResponseHighlighterInterceptor();
		logger.info("...........Initializing Response Highlighter Interceptor Initialization Complete.........");
		return retVal;
	}
	
	/***      CCDA COMPOSITION **********/
	
	@Bean
	public CompositionResourceProvider compositionResourceProvider() {
		CompositionResourceProvider compositionResourceProvider = new CompositionResourceProvider();
		return compositionResourceProvider;
	}
	
	/***      CCDA  **********/
	@Bean
	public CCDAResourceProvider ccdaResourceProvider() {
		CCDAResourceProvider ccdaResourceProvider = new CCDAResourceProvider();
		return ccdaResourceProvider;
	}
	
	/**
	 * This interceptor adds some pretty syntax highlighting in responses when a
	 * browser is detected
	 */
	@Bean(autowire = Autowire.BY_TYPE)
	public IServerInterceptor ccdaResponseInterceptor() {
		logger.info("............CCDA Response Interceptor Initialization Start.........");
		CCDAResponseInterceptor retVal = new CCDAResponseInterceptor();
		logger.info("...........CCDA Response Interceptor Initialization Complete.........");
		return retVal;
	}
	
	
    @Bean(name="securityDataSource")
    public DataSource securityDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("security.jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("security.jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("security.jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("security.jdbc.password"));
        return dataSource;
    }
}
