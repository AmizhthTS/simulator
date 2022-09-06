package com.smsapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.SmppServerConfiguration;
import com.smsapi.model.BindValidator;
import com.smsapi.model.DefaultSmppServerHandler;
import com.smsapi.model.SessionManager;
import com.smsapi.model.SmppServer;
import com.smsapi.model.SmppSessionBindUnbindHandler;

@Configuration
@ComponentScan("com.smsapi.service")
public class Config {

	
	@Bean("SmppServerConfiguration")
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public SmppServerConfiguration getSmppServerConfiguration() {
		
		SmppServerConfiguration configuration = new SmppServerConfiguration();
		
		configuration.setName("SmppServer");
		configuration.setPort(2775);
		configuration.setBindTimeout(30000);
		configuration.setSystemId("arora");
		configuration.setAutoNegotiateInterfaceVersion(true);
		configuration.setInterfaceVersion(SmppConstants.VERSION_3_4);
		configuration.setMaxConnectionSize(500);
		configuration.setNonBlockingSocketsEnabled(true);
		configuration.setDefaultRequestExpiryTimeout(SmppConstants.DEFAULT_REQUEST_EXPIRY_TIMEOUT);
		configuration.setDefaultWindowMonitorInterval(3000);
		configuration.setDefaultWindowSize(SmppConstants.DEFAULT_WINDOW_SIZE);
		configuration.setDefaultWindowWaitTimeout(SmppConstants.DEFAULT_REQUEST_EXPIRY_TIMEOUT*2);
		configuration.setDefaultSessionCountersEnabled(true);
		configuration.setJmxEnabled(false);

		System.out.println("SmppServerConfiguration init");
		return configuration;
	}
	
	@Bean("sessionmanager")
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public SessionManager getSessionManager() {
		
		System.out.println("SessionManager init");

		return new SessionManager();
	}
	
	@Bean("SmppSessionBindUnbindHandler")
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	@DependsOn("sessionmanager")
	public SmppSessionBindUnbindHandler getSmppSessionBindUnbindHandler(@Autowired SessionManager sessionmanager) {
		
		System.out.println("SmppSessionBindUnbindHandler init");

		return new SmppSessionBindUnbindHandler(sessionmanager);
	}
	
	@Bean("BindValidator")
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public BindValidator getBindValidator() {
		
		System.out.println("BindValidator init");

		return new BindValidator();
	}
	
	@Bean("DefaultSmppServerHandler")
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	@DependsOn({"SmppSessionBindUnbindHandler","BindValidator"})
	public DefaultSmppServerHandler getDefaultSmppServerHandler(@Autowired SmppSessionBindUnbindHandler smppSessionBindUnbindHandler,@Autowired BindValidator bindValidator)  {
		System.out.println("DefaultSmppServerHandler init");

			return new DefaultSmppServerHandler(smppSessionBindUnbindHandler,bindValidator);
	}
	
	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	@DependsOn({"SmppServerConfiguration","DefaultSmppServerHandler"})
	public SmppServer getSmppServer(@Autowired SmppServerConfiguration smppserverconfiguration,@Autowired DefaultSmppServerHandler defaultSmppServerHandler )  {
		System.out.println("SmppServer init");

			return new SmppServer(smppserverconfiguration,defaultSmppServerHandler);
	}
}
